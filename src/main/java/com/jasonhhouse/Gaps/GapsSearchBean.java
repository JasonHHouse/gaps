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

import okhttp3.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GapsSearchBean implements GapsSearch {

    private final Logger logger = LoggerFactory.getLogger(GapsSearchBean.class);

    private final Set<Movie> searched;

    private final List<Movie> recommended;

    private final Set<Movie> ownedMovies;

    private final AtomicInteger totalMovieCount;

    private final AtomicInteger searchedMovieCount;

    private final AtomicBoolean cancelSearch;

    private final UrlGenerator urlGenerator;

    @Autowired
    public GapsSearchBean(@Qualifier("real") UrlGenerator urlGenerator) {
        this.ownedMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new ArrayList<>();
        this.urlGenerator = urlGenerator;

        totalMovieCount = new AtomicInteger();
        searchedMovieCount = new AtomicInteger();
        cancelSearch = new AtomicBoolean(true);
    }

    @NotNull
    @Override
    public CompletableFuture<ResponseEntity> run(@NotNull Gaps gaps) {
        searched.clear();
        ownedMovies.clear();
        recommended.clear();
        totalMovieCount.set(0);
        searchedMovieCount.set(0);
        cancelSearch.set(false);

        if (isGapsPropertyValid(gaps)) {
            String reason = "No search property defined. Must search from at least one type: Folder or Plex";
            cancelSearch.set(true);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        defaultValues(gaps);

        try {
            String sessionId = null;
            // Get TMDB Authorization from user,
            // requires user input so needs to be done early before user walks away
            if (StringUtils.isNotEmpty(gaps.getMovieDbListId())) {
                sessionId = getTmdbAuthorization(gaps);
            }

            if (BooleanUtils.isTrue(gaps.getSearchFromPlex())) {
                findAllPlexMovies(gaps);
            } else {
                logger.info("Not searching from Plex");
            }

            //ToDo
            /*if (properties.getFolder().getSearchFromFolder()) {
                findAllFolderMovies();
            }*/

            searchForMovies(gaps);

            if (gaps.getWriteToFile()) {
                writeToFile();
            }

            //Always write to log
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

    private boolean isGapsPropertyValid(Gaps gaps) {
        return BooleanUtils.isNotTrue(gaps.getSearchFromPlex()) && BooleanUtils.isNotTrue(gaps.getSearchFromFolder());
    }

    private void defaultValues(Gaps gaps) {
        if (gaps.getConnectTimeout() == null) {
            gaps.setConnectTimeout(180);
        }

        if (gaps.getReadTimeout() == null) {
            gaps.setReadTimeout(180);
        }

        if (gaps.getWriteTimeout() == null) {
            gaps.setWriteTimeout(180);
        }

        if (gaps.getWriteToFile() == null) {
            gaps.setWriteToFile(true);
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
    public @NotNull Set<PlexLibrary> getPlexLibraries(@NotNull HttpUrl url) {
        logger.info("Searching for Plex Libraries...");

        //ToDo
        //Need to control time out here, using gaps object
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Set<PlexLibrary> plexLibraries = new TreeSet<>();


        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
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

                    NamedNodeMap map = node.getAttributes();
                    Node namedItem = map.getNamedItem("type");
                    if (namedItem == null) {
                        String reason = "Error finding 'type' inside /MediaContainer/Directory";
                        logger.error(reason);
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                    }

                    String type = namedItem.getNodeValue();

                    if (type.equals("movie")) {
                        NamedNodeMap attributes = node.getAttributes();
                        Node titleNode = attributes.getNamedItem("title");
                        Node keyNode = attributes.getNamedItem("key");

                        if (titleNode == null) {
                            String reason = "Error finding 'title' inside /MediaContainer/Directory";
                            logger.error(reason);
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                        }

                        if (keyNode == null) {
                            String reason = "Error finding 'key' inside /MediaContainer/Directory";
                            logger.error(reason);
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                        }

                        String title = titleNode.getNodeValue().replaceAll(":", "");
                        Integer key = Integer.valueOf(keyNode.getNodeValue().trim());

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
        logger.debug("cancelSearch()");
        cancelSearch.set(true);
        searched.clear();
        ownedMovies.clear();
        recommended.clear();
        totalMovieCount.set(0);
        searchedMovieCount.set(0);
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

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("authentication")
                .addPathSegment("token")
                .addPathSegment("new")
                .addQueryParameter("api_key", gaps.getMovieDbApiKey())
                .build();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody.create("{}", mediaType);
        RequestBody body;
        Request request = new Request.Builder()
                .url(url)
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

        url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("authentication")
                .addPathSegment("session")
                .addPathSegment("new")
                .addQueryParameter("api_key", gaps.getMovieDbApiKey())
                .build();

        // Create the sesssion ID for MovieDB using the approved token
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"request_token\":\"" + request_token + "\"}", mediaType);
        request = new Request.Builder()
                .url(url)
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

                body = RequestBody.create("{\"media_id\":" + m.getMedia_id() + "}", mediaType);

                HttpUrl url = new HttpUrl.Builder()
                        .scheme("https")
                        .host("api.themoviedb.org")
                        .addPathSegment("3")
                        .addPathSegment("list")
                        .addPathSegment(gaps.getMovieDbListId())
                        .addPathSegment("add_item")
                        .addQueryParameter("session_id", sessionId)
                        .addQueryParameter("api_key", gaps.getMovieDbApiKey())
                        .build();

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
        List<HttpUrl> urls = gaps.getMovieUrls();

        if (CollectionUtils.isEmpty(urls)) {
            logger.info("No URLs added to plexMovieUrls. Check your application.yaml file if needed.");
            return;
        }

        for (HttpUrl url : urls) {
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

                    if (StringUtils.isBlank(body)) {
                        String reason = "Body returned empty from Plex";
                        logger.error(reason);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
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
                        continue;
                    }

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);

                        Node nodeTitle = node.getAttributes().getNamedItem("title");

                        if (nodeTitle == null) {
                            String reason = "Missing title from Video element in Plex";
                            logger.error(reason);
                            throw new NullPointerException(reason);
                        }

                        //Files can't have : so need to remove to find matches correctly
                        String title = nodeTitle.getNodeValue().replaceAll(":", "");
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
            } catch (IllegalArgumentException | NullPointerException e) {
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
            logger.error("No MovieDb Key added to movieDbApiKey. Need to submit movieDbApiKey on each request.");
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

            HttpUrl searchMovieUrl;
            try {
                searchMovieUrl = urlGenerator.generateSearchMovieUrl(gaps.getMovieDbApiKey(), URLEncoder.encode(movie.getName(), "UTF-8"), String.valueOf(movie.getYear()));

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

                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gaps.getMovieDbApiKey(), String.valueOf(id));

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

                        handleCollection(movie, gaps, client, movieDetails);

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

    private void handleCollection(Movie movie, Gaps gaps, OkHttpClient client, JSONObject movieDetails) {
        int collectionId = movieDetails.getJSONObject("belongs_to_collection").getInt("id");
        String collectionName = movieDetails.getJSONObject("belongs_to_collection").getString("name");

        HttpUrl collectionUrl = urlGenerator.generateCollectionUrl(gaps.getMovieDbApiKey(), String.valueOf(collectionId));

        Request request = new Request.Builder()
                .url(collectionUrl)
                .build();

        try (Response collectionResponse = client.newCall(request).execute()) {
            String collectionJson = collectionResponse.body() != null ? collectionResponse.body().string() : null;

            if (collectionJson == null) {
                logger.error("Body returned null from TheMovieDB for collection information about " + movie.getName());
                return;
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

        } catch (IOException e) {
            logger.error("Error getting collections " + movie, e);
        }

        searched.add(movie);
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

    public static class UserInputThreadCountdown implements Runnable {

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

        private void runTimer() throws IOException {
            long timePassedStart;
            do {
                timePassedStart = (new Date().getTime() - start.getTime()) / 1000;
            } while (timePassedStart < time_limit);
            System.in.close();
        }
    }
}