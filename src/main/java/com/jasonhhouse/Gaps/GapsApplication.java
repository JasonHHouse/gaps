/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.Gaps;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@SpringBootApplication
public class GapsApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(GapsApplication.class);

    private final Properties properties;

    private final Set<Movie> plexMovies;

    private final Set<Movie> searched;

    private final Set<Movie> recommended;

    @Autowired
    public GapsApplication(Properties properties) {
        this.properties = properties;
        this.plexMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new TreeSet<>();
    }

    public static void main(String[] args) {
        SpringApplication.run(GapsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        findAllPlexMovies();
        searchForPlexMovie();
        printRecommended();
    }

    private void findAllPlexMovies() {
        OkHttpClient client = new OkHttpClient();
        String url = properties.getPlexMovieAllUrl();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : null;

            if (body == null) {
                logger.error("Body returned null from Plex");
                return;
            }

            InputStream fileIS = new ByteArrayInputStream(body.getBytes());
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/MediaContainer/Video";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String title = node.getAttributes().getNamedItem("title").getNodeValue();
                if (node.getAttributes().getNamedItem("year") == null) {
                    logger.warn("Year not found for " + title);
                    continue;
                }
                String year = node.getAttributes().getNamedItem("year").getNodeValue();
                Movie movie = new Movie(title, Integer.parseInt(year), "");
                plexMovies.add(movie);
            }
            logger.info(plexMovies.size() + " movies found in plex");

        } catch (IOException e) {
            logger.error("Error connecting to Plex to get Movie list", e);
        } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
            logger.error("Error parsing XML from Plex", e);
        }
    }

    private void searchForPlexMovie() {
        OkHttpClient client = new OkHttpClient();

        int count = 0;
        for (Movie movie : plexMovies) {
            if (searched.contains(movie)) {
                continue;
            }

            String searchMovieUrl;
            try {
                searchMovieUrl = "https://api.themoviedb.org/3/search/movie?api_key=" +
                        properties.getMovieDbApiKey() +
                        "&language=en-US&page=1&include_adult=false&query=" +
                        URLEncoder.encode(movie.getName(), "UTF-8") +
                        "&year=" +
                        movie.getYear();

                Request request = new Request.Builder()
                        .url(searchMovieUrl)
                        .build();

                String json;
                try (Response response = client.newCall(request).execute()) {
                    json = response.body() != null ? response.body().string() : null;

                    if (json == null) {
                        logger.error("Body returned null from TheMovieDB for " + movie.getName());
                        continue;
                    }

                    JSONObject foundMovies = new JSONObject(json);
                    JSONArray results = foundMovies.getJSONArray("results");

                    if (results.length() == 0) {
                        logger.error("Results not found for " + movie);
                        logger.warn("URL: " + searchMovieUrl);
                        continue;
                    }

                    if (results.length() > 1) {
                        logger.warn("Results for " + movie + " came back with " + results.length() + " results. Using first result.");
                        logger.warn(movie + " URL: " + searchMovieUrl);
                    }

                    JSONObject result = results.getJSONObject(0);
                    int id = result.getInt("id");

                    String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + properties.getMovieDbApiKey() + "&language=en-US";

                    request = new Request.Builder()
                            .url(movieDetailUrl)
                            .build();

                    try (Response movieDetailResponse = client.newCall(request).execute()) {

                        String movieDetailJson = movieDetailResponse.body() != null ? movieDetailResponse.body().string() : null;

                        if (movieDetailJson == null) {
                            logger.error("Body returned null from TheMovieDB for details on " + movie.getName());
                            continue;
                        }

                        JSONObject movieDetails = new JSONObject(movieDetailJson);
                        if (!movieDetails.has("belongs_to_collection") || movieDetails.isNull("belongs_to_collection")) {
                            //No collection found, just add movie to searched and continue
                            searched.add(movie);
                            continue;
                        }

                        int collectionId = movieDetails.getJSONObject("belongs_to_collection").getInt("id");
                        String collectionName = movieDetails.getJSONObject("belongs_to_collection").getString("name");
                        String collectionUrl = "https://api.themoviedb.org/3/collection/" + collectionId + "?api_key=" + properties.getMovieDbApiKey() + "&language=en-US";

                        request = new Request.Builder()
                                .url(collectionUrl)
                                .build();

                        try (Response collectionResponse = client.newCall(request).execute()) {
                            String collectionJson = collectionResponse.body() != null ? collectionResponse.body().string() : null;

                            if (collectionJson == null) {
                                logger.error("Body returned null from TheMovieDB for collection information about " + movie.getName());
                                continue;
                            }

                            JSONObject collection = new JSONObject(collectionJson);
                            JSONArray parts = collection.getJSONArray("parts");
                            for (int i = 0; i < parts.length(); i++) {
                                JSONObject part = parts.getJSONObject(i);
                                String title = part.getString("original_title");
                                int year;
                                try {
                                    year = Integer.parseInt(part.getString("release_date").substring(0, 4));
                                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                    logger.warn("No year found for " + title + ". Value returned was '" + part.getString("release_date") + "'. Skipping adding the movie to recommended list.");
                                    continue;
                                }
                                Movie movieFromCollection = new Movie(title, year, collectionName);

                                if (plexMovies.contains(movieFromCollection)) {
                                    searched.add(movieFromCollection);
                                } else if (!searched.contains(movieFromCollection) && year != 0 && year < 2019) {
                                    recommended.add(movieFromCollection);
                                }
                            }

                            searched.add(movie);

                        } catch (IOException e) {
                            logger.error("Error getting collections " + movie, e);
                        }

                    } catch (IOException e) {
                        logger.error("Error getting movie details " + movie, e);
                    }

                } catch (IOException e) {
                    logger.error("Error searching for movie " + movie, e);
                    logger.error("URL: " + searchMovieUrl);
                } catch (JSONException e) {
                    logger.error("Error parsing movie " + movie, e);
                    logger.error("URL: " + searchMovieUrl);
                } finally {
                    try {
                        //can't have too many connections to the movie database in a specific time, have to wait
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        logger.error("Error sleeping", e);
                    }

                    count++;
                    if (count % 20 == 0) {
                        logger.info("Processed " + count + " files of " + plexMovies.size() + ". " + ((int) ((count) / ((double) (plexMovies.size())) * 100)) + "% Complete");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("Error parsing movie URL " + movie, e);
            }
        }
    }

    private void printRecommended() {
        System.out.println(recommended.size() + " Recommended Movies");
        for (Movie movie : recommended) {
            System.out.println(movie.toString());
        }
    }

}