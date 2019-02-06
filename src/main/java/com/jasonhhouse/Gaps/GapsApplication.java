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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
/**
 * Search for all missing movies in your plex collection by MovieDB collection.
 */
@SpringBootApplication
public class GapsApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(GapsApplication.class);

    private final Properties properties;

    private Set<Movie> plexMovies;

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

        if (properties.getWriteToFile()) {
            writeToFile();
        }
        //Always write to command line
        printRecommended();

        if (properties.getMovieDbListId() != null) {
            createTmdbList();
        }
    }

    private void createTmdbList() {
        // create-request-token
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody.create(mediaType, "{}");
        RequestBody body;
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/token/new?api_key=" + properties.getMovieDbApiKey())
                .get()
                .build();

        String request_token;
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseJson = new JSONObject(response.body().string());
            request_token = responseJson.getString("request_token");

            // Have user click link:
            logger.info("############################################");
            logger.info("Click the link below to authorize TMDB list access: " + "https://www.themoviedb.org/authenticate/" + request_token);
            logger.info("Press enter to continue");
            logger.info("############################################");
            System.in.read();
        } catch (Exception e) {
            logger.error("Unable to authenticate tmdb, and add movies to list. ", e);
            return;
        }

        // create session id
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create(mediaType, "{\"request_token\":\"" + request_token + "\"}");
        request = new Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/session/new?api_key=" + properties.getMovieDbApiKey())
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        Response response = null;
        String session_id = null;
        try {
            response = client.newCall(request).execute();
            JSONObject sessionResponse = new JSONObject(response.body().string());
            session_id = sessionResponse.getString("session_id");
        } catch (IOException e) {
            logger.error("Unable to create session id: " + e.getMessage());
            return;
        }

        // Add item to list
        int counter = 0;
        if (session_id != null)
            for (Movie m : recommended) {
                client = new OkHttpClient();

                body = RequestBody.create(mediaType, "{\"media_id\":" + m.getMedia_id() + "}");
                String url = "https://api.themoviedb.org/3/list/" + properties.getMovieDbListId()
                        + "/add_item?session_id=" + session_id + "&api_key=" + properties.getMovieDbApiKey();
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json;charset=utf-8")
                        .build();

                try {

                    response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        counter++;
                } catch (IOException e) {
                    logger.error("Unable to add movie: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        logger.info(counter + " Movies added to list. \nList located at: https://www.themoviedb.org/list/" + properties.getMovieDbListId());
    }

    /**
     * Connect to plex via the URL and parse all of the movies from the returned XML creating a HashSet of movies the
     * user has.
     */
    private void findAllPlexMovies() {
        logger.info("Searching for Plex Movies...");
        OkHttpClient client = new OkHttpClient();
        List<String> urls = properties.getMovieUrls();

        if (CollectionUtils.isEmpty(urls)) {
            logger.error("No URLs added to plexMovieUrls. Check your application.yaml file.");
            return;
        }

        for (String url : urls) {
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
                Movie movie = new Movie(-1, title, Integer.parseInt(year), "");
                    plexMovies.add(movie);
                }
                logger.info(plexMovies.size() + " movies found in plex");

            } catch (IOException e) {
                logger.error("Error connecting to Plex to get Movie list", e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                logger.error("Error parsing XML from Plex", e);
            }
        }
    }

    /**
     * With all of the movies to search, now the connections to MovieDB need to be made. First we must search for
     * movie keys by movie name and year. With the movie key we can get full properties of a movie. Once we have the
     * full properties that contains the collection id, we can search that collection id for it's list of movies. We
     * compare the full collection list to the movies found in plex, any missing we add to the recommended list. To
     * optimize some network calls, we add movies found in a collection and in plex to our already searched list, so we
     * don't re-query collections again and again.
     */
    private void searchForPlexMovie() {
        logger.info("Searching for Movie Collections...");
        OkHttpClient client = new OkHttpClient();

        if (StringUtils.isEmpty(properties.getMovieDbApiKey())) {
            logger.error("No MovieDb Key added to movieDbApiKey. Check your application.yaml file.");
            return;
        }

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
                        logger.error("URL: " + searchMovieUrl);
                        continue;
                    }

                    if (results.length() > 1) {
                        logger.debug("Results for " + movie + " came back with " + results.length() + " results. Using first result.");
                        logger.debug(movie + " URL: " + searchMovieUrl);
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
                                int media_id = part.getInt("id");
                                String title = part.getString("original_title");
                                int year;
                                try {
                                    year = Integer.parseInt(part.getString("release_date").substring(0, 4));
                                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                    logger.warn("No year found for " + title + ". Value returned was '" + part.getString("release_date") + "'. Skipping adding the movie to recommended list.");
                                    continue;
                                }
                                Movie movieFromCollection = new Movie(media_id, title, year, collectionName);

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
                    if (count % 10 == 0) {
                        logger.info(((int) ((count) / ((double) (plexMovies.size())) * 100)) + "% Complete. Processed " + count + " files of " + plexMovies.size() + ". ");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("Error parsing movie URL " + movie, e);
            }
        }
    }

    /**
     * Prints out all recommended files to the terminal or command line
     */
    private void printRecommended() {
        System.out.println(recommended.size() + " Recommended Movies");
        for (Movie movie : recommended) {
            System.out.println(movie.toString());
        }
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    private void writeToFile() {
        File file = new File("gaps_recommended_movies.txt");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                logger.error("Can't delete existing file gaps_recommended_movies.txt");
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                logger.error("Can't create file gaps_recommended_movies.txt");
                return;
            }
        } catch (IOException e) {
            logger.error("Can't create file gaps_recommended_movies.txt", e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream("gaps_recommended_movies.txt")) {
            for (Movie movie : recommended) {
                String output = movie.toString() + System.lineSeparator();
                outputStream.write(output.getBytes());
            }
            return;
        } catch (FileNotFoundException e) {
            logger.error("Can't find file gaps_recommended_movies.txt", e);
            return;
        } catch (IOException e) {
            logger.error("Can't write to file gaps_recommended_movies.txt", e);
            return;
        }
    }

}
