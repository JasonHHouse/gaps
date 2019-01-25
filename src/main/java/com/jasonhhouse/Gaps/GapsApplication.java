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
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GapsApplication implements CommandLineRunner {


    Logger logger = LoggerFactory.getLogger(GapsApplication.class);

    private final Properties properties;

    private final Set<Movie> plexMovies;

    private final Set<Movie> searched;

    private final Set<Movie> recommended;

    @Autowired
    public GapsApplication(Properties properties) {
        this.properties = properties;
        this.plexMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new HashSet<>();
    }

    public static void main(String[] args) {
        SpringApplication.run(GapsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        findAllPlexMovies();
        //plexMovies.add(new Movie("Alien", 1979));
        //plexMovies.add(new Movie("Aliens", 1986));
        //plexMovies.add(new Movie("the burbs", 1989));
        searchForPlexMovie();
        printRecommended();
    }

    private void findAllPlexMovies() {
        OkHttpClient client = new OkHttpClient();

        //Build out the plex address
        //String plexToken = "&X-Plex-Token=" + properties.getPlexUrl();
        //String url = "http://" + properties.getPlexIpAddress() + ":" + properties.getPlexPort() + "/" + plexToken;
        String url = properties.getPlexUrl();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();

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
                Movie movie = new Movie(title, Integer.parseInt(year));
                plexMovies.add(movie);
            }
            System.out.println(plexMovies.size() + " movies found in plex");

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

            String searchMovieUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + properties.getMovieDbApiKey() + "&language=en-US&page=1&include_adult=false&query=" + movie.getName() + "&year=" + movie.getYear();

            Request request = new Request.Builder()
                    .url(searchMovieUrl)
                    .build();

            String json = "";
            try (Response response = client.newCall(request).execute()) {
                json = response.body().string();

                JSONObject foundMovies = new JSONObject(json);
                JSONArray results = foundMovies.getJSONArray("results");

                if (results.length() == 0) {
                    logger.error("Results not found for " + movie);
                    logger.warn("URL: " + searchMovieUrl);
                    continue;
                }

                if (results.length() > 1) {
                    logger.warn("Results for " + movie + " came back with " + results.length() + " results. Using first result.");
                    logger.warn("URL: " + searchMovieUrl);
                }

                JSONObject result = results.getJSONObject(0);
                int id = result.getInt("id");

                String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + properties.getMovieDbApiKey() + "&language=en-US";

                request = new Request.Builder()
                        .url(movieDetailUrl)
                        .build();

                try (Response movieDetailResponse = client.newCall(request).execute()) {
                    String movieDetailJson = movieDetailResponse.body().string();

                    JSONObject movieDetails = new JSONObject(movieDetailJson);
                    if (!movieDetails.has("belongs_to_collection") || movieDetails.isNull("belongs_to_collection")) {
                        //No collection found, just add movie to searched and continue
                        searched.add(movie);
                        continue;
                    }

                    int collectionId = movieDetails.getJSONObject("belongs_to_collection").getInt("id");
                    String collectionUrl = "https://api.themoviedb.org/3/collection/" + collectionId + "?api_key=" + properties.getMovieDbApiKey() + "&language=en-US";

                    request = new Request.Builder()
                            .url(collectionUrl)
                            .build();

                    try (Response collectionResponse = client.newCall(request).execute()) {
                        String collectionJson = collectionResponse.body().string();

                        JSONObject collection = new JSONObject(collectionJson);
                        JSONArray parts = collection.getJSONArray("parts");
                        for (int i = 0; i < parts.length(); i++) {
                            JSONObject part = parts.getJSONObject(i);
                            String title = part.getString("original_title");
                            int year;
                            try {
                                year = Integer.parseInt(part.getString("release_date").substring(0, 4));
                            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                logger.warn("No year found for " + title + ". Value returned was '" + part.getString("release_date") + "'. Skipping adding the movie to recommended.");
                                continue;
                            }
                            Movie movieFromCollection = new Movie(title, year);

                            if (!searched.contains(movieFromCollection) && !plexMovies.contains(movieFromCollection)) {
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
                logger.error("Body: " + json);
            } catch (JSONException e) {
                logger.error("Error parsing movie " + movie, e);
                logger.error("URL: " + searchMovieUrl);
                logger.error("Body: " + json);
            } finally {
                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    logger.error("Error sleeping", e);
                }

                count++;
                if(count % 20 == 0) {
                    System.out.println("Processed " + count + " files");
                }
            }

        }
    }

    private void printRecommended() {
        System.out.println("Recommended Movies");
        for (Movie movie : recommended) {
            System.out.println(movie);
        }
    }

}


/*
query for all movies from plex
Build an arraylist plex movies of movie name and year
Build an empty hash set of movie objects called searched
Build an empty arraylist of v movie objects
Iterate through plex movie list
    if movie is in searched
        continue

    else
        search for movie

        if movie has collections

            Look up each collection
            Iterate over each collection

                if movie in plex movie
                    add movie to searched

                else
                    add to recommended

        put movie into searched

print out recommended


 */
