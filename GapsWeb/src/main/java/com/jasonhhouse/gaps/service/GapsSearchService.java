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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.jasonhhouse.gaps.GapsSearch;
import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.SearchCancelledException;
import com.jasonhhouse.gaps.SearchResults;
import com.jasonhhouse.gaps.UrlGenerator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class GapsSearchService implements GapsSearch {

    public static final String ID_IDX_START = "://";

    public static final String ID_IDX_END = "?";

    public static final String COLLECTION_ID = "belongs_to_collection";

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsSearchService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Movie> everyMovie;

    private final Set<Movie> searched;

    private final Set<Movie> recommended;

    private final Set<Movie> ownedMovies;

    private final AtomicInteger totalMovieCount;

    private final AtomicInteger searchedMovieCount;

    private final AtomicBoolean cancelSearch;

    private final UrlGenerator urlGenerator;

    private final SimpMessagingTemplate template;

    private final AtomicInteger tempTvdbCounter;

    private final IoService ioService;

    private final GapsService gapsService;

    @Autowired
    public GapsSearchService(@Qualifier("real") UrlGenerator urlGenerator, SimpMessagingTemplate template, IoService ioService, GapsService gapsService) {
        this.template = template;
        this.gapsService = gapsService;
        this.ownedMovies = new HashSet<>();
        this.searched = new HashSet<>();
        this.recommended = new HashSet<>();
        this.everyMovie = new ArrayList<>();
        this.urlGenerator = urlGenerator;
        this.ioService = ioService;

        totalMovieCount = new AtomicInteger();
        tempTvdbCounter = new AtomicInteger();
        searchedMovieCount = new AtomicInteger();
        cancelSearch = new AtomicBoolean(true);
    }

    @Override
    @Async
    public void run() {
        LOGGER.info("run( )");

        searched.clear();
        ownedMovies.clear();
        recommended.clear();
        everyMovie.clear();
        everyMovie.addAll(ioService.readMovieIdsFromFile());
        totalMovieCount.set(0);
        searchedMovieCount.set(0);
        cancelSearch.set(false);

        try {
            //ToDo
            String sessionId = null;
//            // Get TMDB Authorization from user,
//            // requires user input so needs to be done early before user walks away
            //if (StringUtils.isNotEmpty(gaps.getMovieDbListId())) {
            //    sessionId = getTmdbAuthorization(gaps);
            //}

            findAllPlexMovies();

            StopWatch watch = new StopWatch();
            watch.start();
            searchForMovies();
            watch.stop();
            System.out.println("Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(watch.getTime()) + " seconds.");
            System.out.println("Times used TVDB ID: " + tempTvdbCounter);

            /*if (StringUtils.isNotEmpty(gaps.getMovieDbListId())) {
                createTmdbList(sessionId);
            }*/

        } catch (SearchCancelledException e) {
            String reason = "Search cancelled";
            LOGGER.error(reason, e);
            template.convertAndSend("/finishedSearching", false);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } finally {
            cancelSearch.set(true);
        }

        ioService.writeToFile(recommended);

        //Always write to log
        ioService.writeRecommendedToFile(recommended);
        ioService.writeMovieIdsToFile(new HashSet<>(everyMovie));

        template.convertAndSend("/finishedSearching", true);
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
    public void cancelSearch() {
        LOGGER.debug("cancelSearch()");
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

    /**
     * Using TMDB api (V3), get access to user list and add recommended movies to
     */
  /*  private @Nullable String getTmdbAuthorization() {
        // Create the request_token request
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("authentication")
                .addPathSegment("token")
                .addPathSegment("new")
                .addQueryParameter("api_key", gapsService.getPlexSearch().getMovieDbApiKey())
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
            JsonNode responseJson = objectMapper.readTree(response.body().string());
            request_token = responseJson.get("request_token").asText();

            // Have user click link to authorize the token
            LOGGER.info("\n############################################\n" +
                    "Click the link below to authorize TMDB list access: \n" +
                    "https://www.themoviedb.org/authenticate/" + request_token + "\n" +
                    "Press enter to continue\n" +
                    "############################################\n");
            new Thread(new UserInputThreadCountdown()).start();
            System.in.read();
        } catch (Exception e) {
            LOGGER.error("Unable to authenticate tmdb, and add movies to list. ", e);
            return null;
        }

        url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("authentication")
                .addPathSegment("session")
                .addPathSegment("new")
                .addQueryParameter("api_key", gapsService.getPlexSearch().getMovieDbApiKey())
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
            JsonNode sessionResponse = objectMapper.readTree(response.body().string());
            return sessionResponse.get("session_id").asText(); // TODO: Save sessionID to file for reuse
        } catch (IOException e) {
            LOGGER.error("Unable to create session id: " + e.getMessage());
            return null;
        }
    }*/

    /**
     * Using TMDB api (V3), get access to user list and add recommended movies to
     */
    //ToDo
   /* private void createTmdbList(@Nullable String sessionId) {
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
                    LOGGER.error("Unable to add movie: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        LOGGER.info(counter + " Movies added to list. \nList located at: https://www.themoviedb.org/list/" + gaps.getMovieDbListId());
    }*/

    /**
     * Connect to plex via the URL and parse all the movies from the returned XML creating a HashSet of movies the
     * user has.
     */
    private void findAllPlexMovies() throws SearchCancelledException {
        LOGGER.info("findAllPlexMovies( )");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();


        List<String> urls = generatePlexUrls();

        if (CollectionUtils.isEmpty(urls)) {
            LOGGER.info("No URLs added to plexMovieUrls. Check your application.yaml file if needed.");
            return;
        }

        for (String url : urls) {
            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search cancelled");
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
                        LOGGER.error(reason);
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
                        LOGGER.warn(reason);
                        continue;
                    }

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);

                        Node nodeTitle = node.getAttributes().getNamedItem("title");

                        if (nodeTitle == null) {
                            String reason = "Missing title from Video element in Plex";
                            LOGGER.error(reason);
                            throw new NullPointerException(reason);
                        }

                        //Files can't have : so need to remove to find matches correctly
                        String title = nodeTitle.getNodeValue().replaceAll(":", "");
                        if (node.getAttributes().getNamedItem("year") == null) {
                            LOGGER.warn("Year not found for " + title);
                            continue;
                        }
                        int year = Integer.parseInt(node.getAttributes().getNamedItem("year").getNodeValue());

                        String guid = "";
                        if (node.getAttributes().getNamedItem("guid") != null) {
                            guid = node.getAttributes().getNamedItem("guid").getNodeValue();
                        }

                        Movie movie;
                        Movie searchMovie = new Movie.Builder(title, year).build();
                        int indexOfMovie = everyMovie.indexOf(searchMovie);
                        if (indexOfMovie != -1) {
                            LOGGER.debug("Using existing movie information");
                            movie = everyMovie.get(indexOfMovie);
                        } else {
                            if (guid.contains("com.plexapp.agents.themoviedb")) {
                                guid = guid.substring(guid.indexOf(ID_IDX_START) + ID_IDX_START.length(), guid.indexOf(ID_IDX_END));
                                movie = new Movie.Builder(title, year).setTvdbId(Integer.parseInt(guid)).build();
                            } else if (guid.contains("com.plexapp.agents.imdb://")) {
                                guid = guid.substring(guid.indexOf(ID_IDX_START) + ID_IDX_START.length(), guid.indexOf(ID_IDX_END));
                                movie = new Movie.Builder(title, year).setImdbId(guid).build();
                            } else {
                                LOGGER.warn("Cannot handle guid value of " + guid);
                                movie = new Movie.Builder(title, year).build();
                            }
                        }

                        LOGGER.debug("guid:" + guid);

                        ownedMovies.add(movie);
                        totalMovieCount.incrementAndGet();
                    }
                    LOGGER.debug(ownedMovies.size() + " movies found in plex");

                } catch (IOException e) {
                    String reason = "Error connecting to Plex to get Movie list: " + url;
                    LOGGER.error(reason, e);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
                } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                    String reason = "Error parsing XML from Plex: " + url;
                    LOGGER.error(reason, e);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                String reason = "Error with plex Url: " + url;
                LOGGER.error(reason, e);
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
    private void searchForMovies() throws SearchCancelledException {
        LOGGER.info("Searching for Movie Collections...");
        OkHttpClient client = new OkHttpClient();

        if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
            LOGGER.error("No MovieDb Key added to movieDbApiKey. Need to submit movieDbApiKey on each request.");
            return;
        }

        for (Movie movie : ownedMovies) {

            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search was cancelled");
            }

            //Print the count first to handle the continue if block or the regular searching case
            if (searchedMovieCount.get() % 10 == 0) {
                LOGGER.info(((int) ((searchedMovieCount.get()) / ((double) (totalMovieCount.get())) * 100)) + "% Complete. Processed " + searchedMovieCount.get() + " files of " + totalMovieCount.get() + ". ");
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
                if (movie.getTvdbId() != -1 && movie.getCollectionId() != -1) {
                    LOGGER.debug("Used Collection ID to get " + movie.getName());
                    tempTvdbCounter.incrementAndGet();
                    handleCollection(movie, client);
                    continue;
                } else if (movie.getTvdbId() != -1) {
                    LOGGER.debug("Used TVDB ID to get " + movie.getName());
                    tempTvdbCounter.incrementAndGet();
                    searchMovieDetails(movie, client);
                    continue;
                } else if (StringUtils.isNotBlank(movie.getImdbId())) {
                    LOGGER.debug("Used 'find' to search for " + movie.getName());
                    searchMovieUrl = urlGenerator.generateFindMovieUrl(gapsService.getPlexSearch().getMovieDbApiKey(), URLEncoder.encode(movie.getImdbId(), "UTF-8"));
                } else {
                    LOGGER.debug("Used 'search' to search for " + movie.getName());
                    searchMovieUrl = urlGenerator.generateSearchMovieUrl(gapsService.getPlexSearch().getMovieDbApiKey(), URLEncoder.encode(movie.getName(), "UTF-8"), String.valueOf(movie.getYear()));
                }

                Request request = new Request.Builder()
                        .url(searchMovieUrl)
                        .build();

                String json;
                try (Response response = client.newCall(request).execute()) {
                    json = response.body() != null ? response.body().string() : null;

                    if (json == null) {
                        LOGGER.error("Body returned null from TheMovieDB for " + movie.getName());
                        continue;
                    }

                    JsonNode foundMovies = objectMapper.readTree(json);
                    ArrayNode results;

                    if (foundMovies.has("movie_results") &&
                            foundMovies.get("movie_results").getNodeType().equals(JsonNodeType.ARRAY)) {
                        //Results from 'find'
                        results = (ArrayNode) foundMovies.get("movie_results");
                    } else {
                        //Results from 'search'
                        results = (ArrayNode) foundMovies.get("results");
                    }

                    if (results.size() == 0) {
                        LOGGER.error("Results not found for " + movie);
                        LOGGER.error("URL: " + searchMovieUrl);
                        continue;
                    }

                    if (results.size() > 1) {
                        LOGGER.debug("Results for " + movie + " came back with " + results.size() + " results. Using first result.");
                        LOGGER.debug(movie + " URL: " + searchMovieUrl);
                    }

                    JsonNode result = results.get(0);
                    int id = result.get("id").asInt();
                    movie.setTvdbId(id);

                    int indexOfMovie = everyMovie.indexOf(movie);
                    if (indexOfMovie != -1) {
                        LOGGER.debug("Merging movie data");
                        everyMovie.get(indexOfMovie).setTvdbId(movie.getTvdbId());
                    } else {
                        Movie newMovie = new Movie.Builder(movie.getName(), movie.getYear())
                                .setTvdbId(movie.getTvdbId())
                                .setImdbId(movie.getImdbId())
                                .setCollection(movie.getCollection())
                                .setCollectionId(movie.getCollectionId())
                                .build();
                        everyMovie.add(newMovie);
                    }

                    searchMovieDetails(movie, client);
                } catch (JsonProcessingException e) {
                    LOGGER.error("Error parsing movie " + movie + ". " + e.getMessage());
                    LOGGER.error("URL: " + searchMovieUrl);
                    e.printStackTrace();
                } catch (IOException e) {
                    LOGGER.error("Error searching for movie " + movie, e);
                    LOGGER.error("URL: " + searchMovieUrl);
                    e.printStackTrace();
                } finally {
                    try {
                        //can't have too many connections to the movie database in a specific time, have to wait
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        LOGGER.error("Error sleeping", e);
                        e.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Error parsing movie URL " + movie, e);
                e.printStackTrace();
            } finally {
                try {
                    //can't have too many connections to the movie database in a specific time, have to wait
                    Thread.sleep(350);
                } catch (InterruptedException e) {
                    LOGGER.error("Error sleeping", e);
                    e.printStackTrace();
                }
            }
        }
    }

    private void searchMovieDetails(Movie movie, OkHttpClient client) {
        HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movie.getImdbId()));

        Request request = new Request.Builder()
                .url(movieDetailUrl)
                .build();

        try (Response movieDetailResponse = client.newCall(request).execute()) {

            String movieDetailJson = movieDetailResponse.body() != null ? movieDetailResponse.body().string() : null;

            if (movieDetailJson == null) {
                LOGGER.error("Body returned null from TheMovieDB for details on " + movie.getName());
                return;
            }

            JsonNode movieDetails = objectMapper.readTree(movieDetailJson);

            if (!movieDetails.has("belongs_to_collection") || movieDetails.get("belongs_to_collection").isNull()) {
                //No collection found, just add movie to searched and continue
                searched.add(movie);
                return;
            }

            int collectionId = movieDetails.get(COLLECTION_ID).get("id").asInt();
            String collectionName = movieDetails.get(COLLECTION_ID).get("name").asText();
            movie.setCollectionId(collectionId);
            movie.setCollection(collectionName);

            int indexOfMovie = everyMovie.indexOf(movie);
            if (indexOfMovie != -1) {
                LOGGER.debug("Merging movie data");
                everyMovie.get(indexOfMovie).setTvdbId(movie.getTvdbId());
                everyMovie.get(indexOfMovie).setCollectionId(movie.getCollectionId());
                everyMovie.get(indexOfMovie).setCollection(movie.getCollection());
            } else {
                Movie newMovie = new Movie.Builder(movie.getName(), movie.getYear())
                        .setTvdbId(movie.getTvdbId())
                        .setImdbId(movie.getImdbId())
                        .setCollection(movie.getCollection())
                        .setCollectionId(movie.getCollectionId())
                        .build();
                everyMovie.add(newMovie);
            }

            handleCollection(movie, client);

        } catch (IOException e) {
            LOGGER.error("Error getting movie details " + movie, e);
        }
    }

    private void handleCollection(Movie movie, OkHttpClient client) {
        HttpUrl collectionUrl = urlGenerator.generateCollectionUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movie.getCollectionId()));

        Request request = new Request.Builder()
                .url(collectionUrl)
                .build();

        try (Response collectionResponse = client.newCall(request).execute()) {
            String collectionJson = collectionResponse.body() != null ? collectionResponse.body().string() : null;

            if (collectionJson == null) {
                LOGGER.error("Body returned null from TheMovieDB for collection information about " + movie.getName());
                return;
            }

            JsonNode collection = objectMapper.readTree(collectionJson);

            int indexOfMovie = everyMovie.indexOf(movie);

            if (collection.has("status_code") && collection.get("status_code").asInt() == 34) {
                LOGGER.warn(collection.get("status_message").asText());
                return;
            } else if (indexOfMovie != -1) {
                int id = collection.get("id").asInt();
                everyMovie.get(indexOfMovie).setCollectionId(id);
                String name = collection.get("name").asText();
                everyMovie.get(indexOfMovie).setCollection(name);
            } else {
                Movie newMovie = new Movie.Builder(movie.getName(), movie.getYear())
                        .setTvdbId(movie.getTvdbId())
                        .setImdbId(movie.getImdbId())
                        .setCollection(collection.get("name").asText())
                        .setCollectionId(collection.get("id").asInt())
                        .build();
                everyMovie.add(newMovie);
            }

            ArrayNode parts = (ArrayNode) collection.get("parts");
            for (JsonNode part : parts) {
                int tvdbId = part.get("id").asInt();
                //Files can't have : so need to remove to find matches correctly
                String title = part.get("title").asText().replaceAll(":", "");
                int year;
                try {
                    if (part.has("release_date") && StringUtils.isNotEmpty(part.get("release_date").asText())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                        LocalDate date = LocalDate.parse(part.get("release_date").asText(), formatter);
                        year = date.getYear();
                    } else {
                        LOGGER.warn("No year found for " + title + ". Value returned was empty. Not adding the movie to recommended list.");
                        continue;
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                    LOGGER.warn("No year found for " + title + ". Value returned was empty. Not adding the movie to recommended list.");
                    continue;
                }

                String posterUrl = "";
                try {
                    posterUrl = part.get("poster_url").asText();
                } catch (Exception e) {
                    LOGGER.debug("No poster found for" + title + ".");
                }

                Movie movieFromCollection = new Movie.Builder(title, year).setTvdbId(tvdbId)
                        .setCollectionId(movie.getCollectionId())
                        .setCollection(movie.getCollection())
                        .setPosterUrl(posterUrl)
                        .build();

                indexOfMovie = everyMovie.indexOf(new Movie.Builder(title, year).build());
                if (indexOfMovie == -1) {
                    LOGGER.debug("Adding collection movie");
                    everyMovie.add(movieFromCollection);
                } else {
                    LOGGER.debug("Merging collection movie");
                    everyMovie.get(indexOfMovie).setTvdbId(tvdbId);
                }

                if (ownedMovies.contains(movieFromCollection)) {
                    searched.add(movieFromCollection);
                    sendEmptySearchUpdate();
                } else if (!searched.contains(movieFromCollection) && year != 0 && year < Year.now().getValue()) {
                    // Get recommended Movie details from MovieDB API
                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movieFromCollection.getTvdbId()));

                    Request newReq = new Request.Builder()
                            .url(movieDetailUrl)
                            .build();

                    try (Response movieDetailResponse = client.newCall(newReq).execute()) {

                        String movieDetailJson = movieDetailResponse.body() != null ? movieDetailResponse.body().string() : null;

                        LOGGER.debug(movieDetailJson);

                        if (movieDetailJson == null) {
                            LOGGER.error("Body returned null from TheMovieDB for details on " + movie.getName());
                            return;
                        }

                        JsonNode movieDet = objectMapper.readTree(movieDetailJson);

                        // Get the release year from movie release date
                        if (movieDet.has("release_date")) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            LocalDate date = LocalDate.parse(movieDet.get("release_date").asText(), formatter);
                            year = date.getYear();
                        } else {
                            LOGGER.warn("No year found for " + title + ". Value returned was '" + movieDet.get("release_date") + "'. Not adding the movie to recommended list.");
                            continue;
                        }

                        if (collection.has("name")) {
                            movie.setCollection(collection.get("name").asText());
                            movieFromCollection.setCollection(collection.get("name").asText());
                        }

                        // Add movie with imbd_id and other details for RSS to recommended list
                        Movie recommendedMovie = new Movie.Builder(movieDet.get("title").asText(), year)
                                .setTvdbId(movieDet.get("id").asInt())
                                .setImdbId(movieDet.get("imdb_id").asText())
                                .setCollectionId(movie.getCollectionId())
                                .setCollection(movie.getCollection())
                                .build();
                        recommended.add(recommendedMovie);

                        // Write current list of recommended movies to file.
                        ioService.writeRssFile(recommended);

                        LOGGER.info("/newMovieFound:" + movieFromCollection.toString());

                        //Send message over websocket
                        SearchResults searchResults = new SearchResults(getSearchedMovieCount(), getTotalMovieCount(), movieFromCollection);
                        template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }

                } else {
                    sendEmptySearchUpdate();
                }
            }

        } catch (IOException e) {
            LOGGER.error("Error getting collections " + movie + ". " + e.getMessage());
        }

        searched.add(movie);
    }

    private void sendEmptySearchUpdate() throws JsonProcessingException {
        //Send message over websocket
        //No new movie, just updated counts
        SearchResults searchResults = new SearchResults(getSearchedMovieCount(), getTotalMovieCount(), null);
        template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
    }


   /* public static class UserInputThreadCountdown implements Runnable {

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

    }*/

    private List<String> generatePlexUrls() {
        LOGGER.info(gapsService.getPlexSearch().getLibraries().toString());
        List<String> urls = gapsService.getPlexSearch()
                .getLibraries()
                .stream()
                .filter(PlexLibrary::getSelected)
                .map(plexLibrary -> "http://" + gapsService.getPlexSearch().getAddress() + ":" + gapsService.getPlexSearch().getPort() + "/library/sections/" + plexLibrary.getKey() + "/all/?X-Plex-Token=" + gapsService.getPlexSearch().getPlexToken())
                .collect(Collectors.toList());
        LOGGER.info("URLS: " + urls.size());
        return urls;
    }
}
