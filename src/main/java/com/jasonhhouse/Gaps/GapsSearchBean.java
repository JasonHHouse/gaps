package com.jasonhhouse.Gaps;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class GapsSearchBean implements GapsSearch {

    private final Logger logger = LoggerFactory.getLogger(GapsSearchBean.class);

    private final Set<Movie> searched;

    private final Set<Movie> recommended;

    private final Set<Movie> ownedMovies;

    private final AtomicInteger totalMovieCount;

    private final AtomicInteger searchedMovieCount;

    private final AtomicBoolean cancelSearch;

    public GapsSearchBean() {
        this.ownedMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new TreeSet<>();

        totalMovieCount = new AtomicInteger();
        searchedMovieCount = new AtomicInteger();
        cancelSearch = new AtomicBoolean(true);
    }

    @NotNull
    @Override
    public CompletableFuture run(@NotNull Gaps gaps) {
        searched.clear();
        totalMovieCount.set(0);
        searchedMovieCount.set(0);
        cancelSearch.set(false);

        try {
            String sessionId = null;
            // Get TMDB Authorization from user,
            // requires user input so needs to be done early before user walks away
            if (StringUtils.isNotEmpty(gaps.getMovieDbListId())) {
                sessionId = getTmdbAuthorization(gaps);
            }

            if (gaps.getSearchFromPlex()) {
                findAllPlexMovies(gaps);
            }

            //ToDo
            /*if (properties.getFolder().getSearchFromFolder()) {
                findAllFolderMovies();
            }*/

            searchForMovies(gaps);

            if (gaps.getWriteToFile()) {
                writeToFile();
            }

            //Always write to command line
            printRecommended();

            if (StringUtils.isNotEmpty(gaps.getMovieDbListId())) {
                createTmdbList(gaps, sessionId);
            }

            return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(recommended, HttpStatus.OK));
        } catch (SearchCancelledException e) {
            String reason = "Search cancelled";
            logger.error(reason, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } finally {
            cancelSearch.set(true);
        }
    }

    @NotNull
    @Override
    public Integer getTotalMovieCount() {
        return totalMovieCount.get();
    }

    @NotNull
    @Override
    public Integer getSearchedMovieCount() {
        return searchedMovieCount.get();
    }

    @Override
    public @NotNull CopyOnWriteArrayList<Movie> getRecommendedMovies() {
        return new CopyOnWriteArrayList<>(recommended);
    }

    @Override
    public @NotNull Set<PlexLibrary> getPlexLibraries(@NotNull String address, @NotNull int port, @NotNull String token) {
        logger.info("Searching for Plex Libraries...");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Set<PlexLibrary> plexLibraries = new TreeSet<>();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(address)
                .port(port)
                .addPathSegment("library")
                .addPathSegment("sections")
                .addQueryParameter("X-Plex-Token", token)
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (body == null) {
                    String reason = "Body returned null from Plex. Url: " + url;
                    logger.error(reason);
                    throw new IllegalStateException(reason);
                }

                InputStream fileIS = new ByteArrayInputStream(body.getBytes());
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDocument = builder.parse(fileIS);
                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/MediaContainer/Directory";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

                if (nodeList.getLength() == 0) {
                    String reason = "No libraries found in url: " + url;
                    logger.warn(reason);
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    String type = node.getAttributes().getNamedItem("type").getNodeValue();

                    if (type.equals("movie")) {
                        String title = node.getAttributes().getNamedItem("title").getNodeValue().replaceAll(":", "");
                        Integer key = Integer.valueOf(node.getAttributes().getNamedItem("key").getNodeValue());

                        PlexLibrary plexLibrary = new PlexLibrary(key, title);
                        plexLibraries.add(plexLibrary);

                    }
                }

            } catch (IOException e) {
                String reason = "Error connecting to Plex to get library list: " + url;
                logger.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = "Error parsing XML from Plex: " + url;
                logger.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with plex Url: " + url;
            logger.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        logger.info(plexLibraries.size() + " Plex libraries found");

        return plexLibraries;
    }


    @Override
    public void cancelSearch() {
        cancelSearch.set(true);
    }

    @Override
    public boolean isSearching() {
        return !cancelSearch.get();
    }

    private void findAllFolderMovies() {
        //ToDo
        /*if (CollectionUtils.isEmpty(properties.getFolder().getFolders())) {
            logger.error("folders property cannot be empty when searchFromFolder is true");
            return;
        }

        if (CollectionUtils.isEmpty(properties.getFolder().getMovieFormats())) {
            logger.error("movie formats property cannot be empty when searchFromFolder is true");
            return;
        }

        for (String strFolder : properties.getFolder().getFolders()) {
            File folder = new File(strFolder);
            searchFolders(folder);
        }*/

    }

    private void searchFolders(File folder) {
        //ToDo
        /*if (!folder.exists()) {
            logger.warn("Folder in folders property does not exist: " + folder);
            return;
        }

        if (!folder.isDirectory()) {
            logger.warn("Folder in folders property is not a directory: " + folder);
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            logger.warn("Folder in folders property is empty: " + folder);
            return;
        }

        for (File file : files) {
            if (file.isDirectory() && properties.getFolder().getRecursive()) {
                searchFolders(file);
                continue;
            }

            String extension = FilenameUtils.getExtension(file.toString());

            if (properties.getFolder().getMovieFormats().contains(extension)) {
                String fullMovie = FilenameUtils.getBaseName(file.toString());
                Pattern pattern = Pattern.compile(properties.getFolder().getYearRegex());
                Matcher matcher = pattern.matcher(fullMovie);

                if (!matcher.find()) {
                    logger.warn("No regex matches found for " + fullMovie);
                    continue;
                }

                String year = matcher.group(matcher.groupCount()).replaceAll("[)(]", "");
                String title = fullMovie.substring(0, fullMovie.indexOf(" ("));

                Movie movie = new Movie(-1, title, Integer.parseInt(year), "");
                ownedMovies.add(movie);
            } else {
                logger.warn("Skipping file " + file);
            }


        }*/
    }

    /**
     * Using TMDB api (V3), get access to user list and add recommended movies to
     */
    private @Nullable String getTmdbAuthorization(@NotNull Gaps gaps) {
        // Create the request_token request
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody.create(mediaType, "{}");
        RequestBody body;
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/token/new?api_key=" + gaps.getMovieDbApiKey())
                .get()
                .build();

        String request_token;
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseJson = new JSONObject(response.body().string());
            request_token = responseJson.getString("request_token");

            // Have user click link to authorize the token
            logger.info("\n############################################\n" +
                    "Click the link below to authorize TMDB list access: \n" +
                    "https://www.themoviedb.org/authenticate/" + request_token + "\n" +
                    "Press enter to continue\n" +
                    "############################################\n");
            new Thread(new UserInputThreadCountdown()).start();
            System.in.read();
        } catch (Exception e) {
            logger.error("Unable to authenticate tmdb, and add movies to list. ", e);
            return null;
        }

        // Create the sesssion ID for MovieDB using the approved token
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create(mediaType, "{\"request_token\":\"" + request_token + "\"}");
        request = new Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/session/new?api_key=" + gaps.getMovieDbApiKey())
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject sessionResponse = new JSONObject(response.body().string());
            return sessionResponse.getString("session_id"); // TODO: Save sessionID to file for reuse
        } catch (IOException e) {
            logger.error("Unable to create session id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Using TMDB api (V3), get access to user list and add recommended movies to
     */
    private void createTmdbList(@NotNull Gaps gaps, @Nullable String sessionId) {
        OkHttpClient client;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body;

        // Add item to TMDB list specified by user
        int counter = 0;
        if (sessionId != null)
            for (Movie m : recommended) {
                client = new OkHttpClient();

                body = RequestBody.create(mediaType, "{\"media_id\":" + m.getMedia_id() + "}");
                String url = "https://api.themoviedb.org/3/list/" + gaps.getMovieDbListId()
                        + "/add_item?session_id=" + sessionId + "&api_key=" + gaps.getMovieDbApiKey();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json;charset=utf-8")
                        .build();

                try {

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        counter++;
                } catch (IOException e) {
                    logger.error("Unable to add movie: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        logger.info(counter + " Movies added to list. \nList located at: https://www.themoviedb.org/list/" + gaps.getMovieDbListId());
    }

    /**
     * Connect to plex via the URL and parse all of the movies from the returned XML creating a HashSet of movies the
     * user has.
     */
    private void findAllPlexMovies(@NotNull Gaps gaps) throws SearchCancelledException {
        logger.info("Searching for Plex Movies...");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(gaps.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(gaps.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(gaps.getReadTimeout(), TimeUnit.SECONDS)
                .build();
        List<String> urls = gaps.getMovieUrls();

        if (CollectionUtils.isEmpty(urls)) {
            logger.info("No URLs added to plexMovieUrls. Check your application.yaml file if needed.");
            return;
        }

        for (String url : urls) {
            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search was cancelled");
            }

            try {
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

                    if (nodeList.getLength() == 0) {
                        String reason = "No movies found in url: " + url;
                        logger.warn(reason);
                    }

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        //Files can't have : so need to remove to find matches correctly
                        String title = node.getAttributes().getNamedItem("title").getNodeValue().replaceAll(":", "");
                        if (node.getAttributes().getNamedItem("year") == null) {
                            logger.warn("Year not found for " + title);
                            continue;
                        }
                        String year = node.getAttributes().getNamedItem("year").getNodeValue();
                        Movie movie = new Movie(-1, title, Integer.parseInt(year), "");
                        ownedMovies.add(movie);
                        totalMovieCount.incrementAndGet();
                    }
                    logger.info(ownedMovies.size() + " movies found in plex");

                } catch (IOException e) {
                    String reason = "Error connecting to Plex to get Movie list: " + url;
                    logger.error(reason, e);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
                } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                    String reason = "Error parsing XML from Plex: " + url;
                    logger.error(reason, e);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
                }
            } catch (IllegalArgumentException e) {
                String reason = "Error with plex Url: " + url;
                logger.error(reason, e);
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
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
    private void searchForMovies(@NotNull Gaps gaps) throws SearchCancelledException {
        logger.info("Searching for Movie Collections...");
        OkHttpClient client = new OkHttpClient();

        if (StringUtils.isEmpty(gaps.getMovieDbApiKey())) {
            logger.error("No MovieDb Key added to movieDbApiKey. Check your application.yaml file.");
            return;
        }

        for (Movie movie : ownedMovies) {

            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search was cancelled");
            }

            //Print the count first to handle the continue if block or the regular searching case
            if (searchedMovieCount.get() % 10 == 0) {
                logger.info(((int) ((searchedMovieCount.get()) / ((double) (totalMovieCount.get())) * 100)) + "% Complete. Processed " + searchedMovieCount.get() + " files of " + totalMovieCount.get() + ". ");
            }
            searchedMovieCount.incrementAndGet();

            if (searched.contains(movie)) {
                continue;
            }

            String searchMovieUrl;
            try {
                searchMovieUrl = "https://api.themoviedb.org/3/search/movie?api_key=" +
                        gaps.getMovieDbApiKey() +
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

                    String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + gaps.getMovieDbApiKey() + "&language=en-US";

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
                        String collectionUrl = "https://api.themoviedb.org/3/collection/" + collectionId + "?api_key=" + gaps.getMovieDbApiKey() + "&language=en-US";

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
                                //Files can't have : so need to remove to find matches correctly
                                String title = part.getString("original_title").replaceAll(":", "");
                                int year;
                                try {
                                    year = Integer.parseInt(part.getString("release_date").substring(0, 4));
                                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                    logger.warn("No year found for " + title + ". Value returned was '" + part.getString("release_date") + "'. Not adding the movie to recommended list.");
                                    continue;
                                }
                                Movie movieFromCollection = new Movie(media_id, title, year, collectionName);

                                if (ownedMovies.contains(movieFromCollection)) {
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
                    logger.error("Error parsing movie " + movie + ". " + e.getMessage());
                    logger.error("URL: " + searchMovieUrl);
                } finally {
                    try {
                        //can't have too many connections to the movie database in a specific time, have to wait
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        logger.error("Error sleeping", e);
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
        } catch (FileNotFoundException e) {
            logger.error("Can't find file gaps_recommended_movies.txt", e);
        } catch (IOException e) {
            logger.error("Can't write to file gaps_recommended_movies.txt", e);
        }
    }

    public class UserInputThreadCountdown implements java.lang.Runnable {

        int time_limit = 60;

        Date start;

        @Override
        public void run() {
            start = new Date();
            try {
                this.runTimer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void runTimer() throws IOException {
            long timePassedstart = 0;
            do {
                timePassedstart = (new Date().getTime() - start.getTime()) / 1000;
            } while (timePassedstart < time_limit);
            System.in.close();

        }

    }

}
