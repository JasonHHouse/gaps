/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Gaps;
import com.jasonhhouse.gaps.GapsSearch;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.SearchCancelledException;
import com.jasonhhouse.gaps.SearchResults;
import com.jasonhhouse.gaps.UrlGenerator;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class GapsSearchService implements GapsSearch {

    private final Logger logger = LoggerFactory.getLogger(GapsSearchService.class);

    private final Set<Movie> readMovies;

    private final List<Movie> everyMovie;

    private final Set<Movie> searched;

    private final List<Movie> recommended;

    private final Set<Movie> ownedMovies;

    private final AtomicInteger totalMovieCount;

    private final AtomicInteger searchedMovieCount;

    private final AtomicBoolean cancelSearch;

    private final UrlGenerator urlGenerator;

    private final SimpMessagingTemplate template;

    @Autowired
    public GapsSearchService(@Qualifier("real") UrlGenerator urlGenerator, SimpMessagingTemplate template) {
        this.template = template;
        this.ownedMovies = new HashSet<>();
        this.readMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new ArrayList<>();
        this.everyMovie = new ArrayList<>();
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
        readMovies.clear();
        recommended.clear();
        everyMovie.clear();
        totalMovieCount.set(0);
        searchedMovieCount.set(0);
        cancelSearch.set(false);

        StopWatch watch = new StopWatch();
        watch.start();

        if (isGapsPropertyValid(gaps)) {
            String reason = "No search property defined. Must search from at least one type: Folder or Plex";
            cancelSearch.set(true);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        defaultValues(gaps);

        //populate read movies
        readMovieIdsFromFile();

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
            writeMovieIdsToFile();

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

            watch.stop();
            System.out.println("Time Elapsed: " + watch.getTime());
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

                body = RequestBody.create("{\"media_id\":" + m.getTvdbId() + "}", mediaType);

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
                HttpUrl httpUrl = urlGenerator.generatePlexUrl(url);

                Request request = new Request.Builder()
                        .url(httpUrl)
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
                        int year = Integer.parseInt(node.getAttributes().getNamedItem("year").getNodeValue());

                        String guid = "";
                        if (node.getAttributes().getNamedItem("guid") != null) {
                            guid = node.getAttributes().getNamedItem("guid").getNodeValue();
                        }

                        Movie movie;
                        Movie searchMovie = new Movie(title, year);
                        int indexOfMovie = everyMovie.indexOf(searchMovie);
                        logger.debug("everyMovie.size():" + everyMovie.size());
                        logger.debug("indexOfMovie:" + indexOfMovie);
                        logger.debug("searchMovie:" + searchMovie);
                        if (indexOfMovie != -1) {
                            logger.info("Using existing movie information");
                            movie = everyMovie.get(indexOfMovie);
                        } else {
                            if (guid.contains("com.plexapp.agents.themoviedb")) {
                                guid = guid.replace("com.plexapp.agents.themoviedb://", "");
                                guid = guid.replace("?lang=en", "");

                                movie = new Movie(Integer.parseInt(guid), title, year, "");
                            } else if (guid.contains("com.plexapp.agents.imdb://")) {
                                guid = guid.replace("com.plexapp.agents.imdb://", "");
                                guid = guid.replace("?lang=en", "");

                                movie = new Movie(guid, title, year, "");
                            } else {
                                logger.warn("Cannot handle guid value of " + guid);
                                movie = new Movie(title, year, "");
                            }
                        }

                        logger.debug("guid:" + guid);

                        ownedMovies.add(movie);
                        totalMovieCount.incrementAndGet();
                    }
                    logger.debug(ownedMovies.size() + " movies found in plex");

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
                //If TMDB is available, skip the search
                //If IMDB is available use find
                //Otherwise fall back to movie title and year search
                if (movie.getTvdbId() != -1) {
                    searchMovieDetails(gaps, movie, movie.getTvdbId(), client);
                    continue;
                } else if (movie.getImdbId() != null) {
                    logger.info("Used 'find' to search for " + movie.getName());
                    searchMovieUrl = urlGenerator.generateFindMovieUrl(gaps.getMovieDbApiKey(), URLEncoder.encode(movie.getImdbId(), "UTF-8"));
                } else {
                    logger.info("Used 'search' to search for " + movie.getName());
                    searchMovieUrl = urlGenerator.generateSearchMovieUrl(gaps.getMovieDbApiKey(), URLEncoder.encode(movie.getName(), "UTF-8"), String.valueOf(movie.getYear()));
                }

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
                    JSONArray results;

                    if (foundMovies.has("movie_results")) {
                        //Results from 'find'
                        results = foundMovies.getJSONArray("movie_results");
                    } else {
                        //Results from 'search'
                        results = foundMovies.getJSONArray("results");
                    }

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

                    searchMovieDetails(gaps, movie, id, client);
                } catch (IOException e) {
                    logger.error("Error searching for movie " + movie, e);
                    logger.error("URL: " + searchMovieUrl);
                    e.printStackTrace();
                } catch (JSONException e) {
                    logger.error("Error parsing movie " + movie + ". " + e.getMessage());
                    logger.error("URL: " + searchMovieUrl);
                    e.printStackTrace();
                } finally {
                    try {
                        //can't have too many connections to the movie database in a specific time, have to wait
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        logger.error("Error sleeping", e);
                        e.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("Error parsing movie URL " + movie, e);
                e.printStackTrace();
            }
        }
    }

    private void searchMovieDetails(Gaps gaps, Movie movie, int tmdbId, OkHttpClient client) {
        HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gaps.getMovieDbApiKey(), String.valueOf(tmdbId));

        Request request = new Request.Builder()
                .url(movieDetailUrl)
                .build();

        try (Response movieDetailResponse = client.newCall(request).execute()) {

            String movieDetailJson = movieDetailResponse.body() != null ? movieDetailResponse.body().string() : null;

            if (movieDetailJson == null) {
                logger.error("Body returned null from TheMovieDB for details on " + movie.getName());
                return;
            }

            JSONObject movieDetails = new JSONObject(movieDetailJson);

            if (!movieDetails.has("belongs_to_collection") || movieDetails.isNull("belongs_to_collection")) {
                //No collection found, just add movie to searched and continue
                searched.add(movie);
                return;
            }

            handleCollection(movie, gaps, client, movieDetails);

        } catch (IOException e) {
            logger.error("Error getting movie details " + movie, e);
        }

        int indexOfMovie = everyMovie.indexOf(movie);
        if (indexOfMovie != -1) {
            logger.debug("Merging movie data");
            everyMovie.get(indexOfMovie).merge(movie);
        } else {
            everyMovie.add(new Movie(tmdbId, movie.getImdbId(), movie.getName(), movie.getYear(), movie.getCollection(), null));
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
                int tvdbId = part.getInt("id");
                //Files can't have : so need to remove to find matches correctly
                String title = part.getString("title").replaceAll(":", "");
                int year;
                String releaseDate = null;
                try {
                    releaseDate = part.optString("release_date");
                    if (StringUtils.isNotEmpty(releaseDate)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                        LocalDate date = LocalDate.parse(releaseDate, formatter);
                        year = date.getYear();
                    } else {
                        logger.warn("No year found for " + title + ". Value returned was '" + releaseDate + "'. Not adding the movie to recommended list.");
                        continue;
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                    logger.warn("No year found for " + title + ". Value returned was '" + releaseDate + "'. Not adding the movie to recommended list.");
                    continue;
                }

                String posterUrl = "";
                try {
                    posterUrl = part.optString("poster_url");
                } catch (Exception e) {
                    logger.warn("No poster found for" + title + ".");
                }

                Movie movieFromCollection = new Movie(tvdbId, title, year, collectionName, posterUrl);

                int indexOfMovie = everyMovie.indexOf(new Movie(title, year));
                if (indexOfMovie == -1) {
                    logger.debug("Adding collection movie");
                    everyMovie.add(movieFromCollection);
                } else {
                    logger.debug("Merging collection movie");
                    everyMovie.get(indexOfMovie).merge(movie);
                }

                if (ownedMovies.contains(movieFromCollection)) {
                    searched.add(movieFromCollection);
                    sendEmptySearchUpdate();
                } else if (!searched.contains(movieFromCollection) && year != 0 && year < Year.now().getValue()) {
                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gaps.getMovieDbApiKey(), String.valueOf(movieFromCollection.getTvdbId()));

                    Request newReq = new Request.Builder()
                            .url(movieDetailUrl)
                            .build();

                    try (Response movieDetailResponse = client.newCall(newReq).execute()) {

                        String movieDetailJson = movieDetailResponse.body() != null ? movieDetailResponse.body().string() : null;

                        if (movieDetailJson == null) {
                            logger.error("Body returned null from TheMovieDB for details on " + movie.getName());
                            return;
                        }

                        JSONObject movieDet = new JSONObject(movieDetailJson);
                        releaseDate = part.optString("release_date");
                        if (StringUtils.isNotEmpty(releaseDate)) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            LocalDate date = LocalDate.parse(releaseDate, formatter);
                            year = date.getYear();
                        } else {
                            logger.warn("No year found for " + title + ". Value returned was '" + releaseDate + "'. Not adding the movie to recommended list.");
                            continue;
                        }
                        recommended.add(new Movie(movieDet.getInt("id"), movieDet.getString("imdb_id"), movieDet.getString("title"), year));
                        writeRssFile(recommended);
                        //Send message over websocket
                        SearchResults searchResults = new SearchResults(getSearchedMovieCount(), getTotalMovieCount(), movieFromCollection);
                        template.convertAndSend("/topic/newMovieFound", searchResults);
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }

                } else {
                    sendEmptySearchUpdate();
                }
            }

        } catch (IOException e) {
            logger.error("Error getting collections " + movie, e);
        }

        searched.add(movie);
    }

    private void writeRssFile(List<Movie> recommended) {
        JSONArray jsonRecommended = new JSONArray();

        try {
            File file = new File("rssFeed.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            for (Movie mov : recommended) {

                JSONObject obj = new JSONObject();
                obj.put("imdb_id", mov.getImdbId());
                obj.put("tvdb_id", mov.getTvdbId());
                obj.put("title", mov.getName());
                obj.put("release_date", mov.getYear());
                obj.put("poster_path", mov.getPosterUrl());
                jsonRecommended.put(obj);
            }

            jsonRecommended.write(writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private void sendEmptySearchUpdate() {
        //Send message over websocket
        //No new movie, just updated counts
        SearchResults searchResults = new SearchResults(getSearchedMovieCount(), getTotalMovieCount(), null);
        template.convertAndSend("/topic/newMovieFound", searchResults);
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
    private void writeMovieIdsToFile() {
        final String fileName = "/usr/data/movieIds.json";
        File file = new File(fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                logger.error("Can't delete existing file " + fileName);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                logger.error("Can't create file " + fileName);
                return;
            }
        } catch (IOException e) {
            logger.error("Can't create file " + fileName, e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            JSONArray movies = new JSONArray();
            for (Movie movie : everyMovie) {
                movies.put(movie.toJSON());
            }
            outputStream.write(movies.toString().getBytes());
        } catch (FileNotFoundException e) {
            logger.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            logger.error("Can't write to file " + fileName, e);
        }
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    private void readMovieIdsFromFile() {
        final String fileName = "/usr/data/movieIds.json";
        File file = new File(fileName);
        if (!file.exists()) {
            logger.warn("Can't find json file '" + fileName + "'. Most likely first run.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            logger.debug(fullFile.toString());

            JSONArray movies = new JSONArray(fullFile.toString());
            for (int i = 0; i < movies.length(); i++) {
                Movie movie = jsonToMovie(movies.getJSONObject(i));
                everyMovie.add(movie);
            }

            logger.debug("everyMovie.size():" + everyMovie.size());

        } catch (FileNotFoundException e) {
            logger.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            logger.error("Can't write to file " + fileName, e);
        } catch (JSONException e) {
            logger.error("Error parsing JSON file " + fileName, e);
        }
    }

    private Movie jsonToMovie(JSONObject jsonMovie) throws JSONException {
        int tvdbId = jsonMovie.getInt(Movie.TVDB_ID);
        String imdbId = jsonMovie.optString(Movie.IMDB_ID);
        String name = jsonMovie.getString(Movie.NAME);
        int year = jsonMovie.getInt(Movie.YEAR);

        return new Movie(tvdbId, imdbId, name, year);
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