package com.jasonhhouse.Gaps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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

/*
    public List<Section> getSections(String address) {
        HttpGet request = new HttpGet(address + "/library/sections");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            System.err.println("Failed to connect to server.");
            e.printStackTrace();
        }

        RootElement root = new RootElement("MediaContainer");

        List<Section> sections = SwingUtilities2.Section.appendArrayListener(root, 0);

        try {
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sections;
    }

    public List<Movie> getAllMoviesForSection(String address, int section) {
        HttpGet request = new HttpGet(address + "/library/sections/" + section + "/all");
        HttpClient client = new DefaultHttpClient();

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RootElement root = new RootElement("MediaContainer");
        List<Movie> movies = Movie.appendArrayListener(root, 0);

        try {
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("PlexAPI", "Found " + movies.size() + " movies.");

        return movies;
    }*/

    private void findAllPlexMovies() {
        OkHttpClient client = new OkHttpClient();

        //Build out the plex address
        //String plexToken = "&X-Plex-Token=" + properties.getPlexToken();
        //String url = "http://" + properties.getPlexIpAddress() + ":" + properties.getPlexPort() + "/" + plexToken;
        String url = "https://72-83-212-34.06b5f3bbe8be4ceaafe87782b1eadff4.plex.direct:19427/library/sections/1/all/?X-Plex-Token=mQw4uawxTyYEmqNUrvBz";

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
                if(node.getAttributes().getNamedItem("year") == null) {
                    logger.warn("Year not found for " + title);
                    continue;
                }
                String year = node.getAttributes().getNamedItem("year").getNodeValue();
                Movie movie = new Movie(title, Integer.parseInt(year));
                plexMovies.add(movie);
            }
        } catch (IOException e) {
            logger.error("Error connecting to Plex to get Movie list", e);
        } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
            logger.error("Error parsing XML from Plex", e);
        }
    }

    private void searchForPlexMovie() {
        OkHttpClient client = new OkHttpClient();

        for (Movie movie : plexMovies) {
            if (searched.contains(movie)) {
                continue;
            }

            String searchMovieUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + properties.getMovieDbApiKey() + "&language=en-US&page=1&include_adult=false&query=" + movie.getName() + "&year=" + movie.getYear();

            Request request = new Request.Builder()
                    .url(searchMovieUrl)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();

                JSONObject foundMovies = new JSONObject(json);
                JSONArray results = foundMovies.getJSONArray("results");

                if (results.length() == 0) {
                    logger.error("Results not found for " + movie);
                    continue;
                }

                if (results.length() > 1) {
                    logger.warn("Results for " + movie + " came back with more than one result");
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
                            int year = Integer.parseInt(part.getString("release_date").substring(0, 4));
                            Movie movieFromCollection = new Movie(title, year);

                            if (searched.contains(movieFromCollection)) {
                                continue;
                            } else if (plexMovies.contains(movieFromCollection)) {
                                continue;
                            } else {
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
            } catch (JSONException e) {
                logger.error("Error parsing movie " + movie, e);
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
