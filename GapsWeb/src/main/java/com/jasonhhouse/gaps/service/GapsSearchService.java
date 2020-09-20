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
import com.jasonhhouse.gaps.movie.BasicMovie;
import com.jasonhhouse.gaps.MovieFromCollection;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.SearchCancelledException;
import com.jasonhhouse.gaps.SearchResults;
import com.jasonhhouse.gaps.UrlGenerator;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.plex.libs.PlexLibrary;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GapsSearchService implements GapsSearch {

    public static final String COLLECTION_ID = "belongs_to_collection";
    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String RELEASE_DATE = "release_date";
    public static final String PARTS = "parts";
    public static final String MOVIE_RESULTS = "movie_results";
    public static final String FINISHED_SEARCHING_URL = "/finishedSearching";
    private static final Logger LOGGER = LoggerFactory.getLogger(GapsSearchService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final AtomicBoolean cancelSearch;

    private final UrlGenerator urlGenerator;

    private final SimpMessagingTemplate template;

    private final AtomicInteger tempTvdbCounter;

    private final FileIoService fileIoService;

    private final TmdbService tmdbService;

    private final NotificationService notificationService;

    @Autowired
    public GapsSearchService(@Qualifier("real") UrlGenerator urlGenerator, SimpMessagingTemplate template, FileIoService fileIoService, TmdbService tmdbService, NotificationService notificationService) {
        this.template = template;
        this.tmdbService = tmdbService;
        this.urlGenerator = urlGenerator;
        this.fileIoService = fileIoService;
        this.notificationService = notificationService;

        tempTvdbCounter = new AtomicInteger();
        cancelSearch = new AtomicBoolean(true);
    }

    @Override
    public void run(String machineIdentifier, Integer key) {
        LOGGER.info("run( {}, {} )", machineIdentifier, key);

        PlexProperties plexProperties = fileIoService.readProperties();
        Optional<PlexServer> optionalPlexServer = plexProperties.getPlexServers().stream().filter(tempPlexServer -> tempPlexServer.getMachineIdentifier().equals(machineIdentifier)).findFirst();
        PlexServer plexServer;
        if (optionalPlexServer.isPresent()) {
            plexServer = optionalPlexServer.get();
        } else {
            LOGGER.error("Plex server not found with machineIdentifier {} and key {}", machineIdentifier, key);
            return;
        }

        Optional<PlexLibrary> optionalPlexLibrary = plexServer.getPlexLibraries().stream().filter(tempPlexLibrary -> tempPlexLibrary.getKey().equals(key)).findAny();

        PlexLibrary plexLibrary;
        if (optionalPlexLibrary.isPresent()) {
            plexLibrary = optionalPlexLibrary.get();
        } else {
            LOGGER.error("Plex library not found with machineIdentifier {} and key {}", machineIdentifier, key);
            return;
        }

        notificationService.recommendedMoviesSearchStarted(plexServer, plexLibrary);

        if (StringUtils.isEmpty(plexProperties.getMovieDbApiKey())) {
            Payload payload = tmdbService.testTmdbKey(plexProperties.getMovieDbApiKey());
            if (payload != Payload.TMDB_KEY_VALID) {
                LOGGER.error(payload.getReason());
                template.convertAndSend(FINISHED_SEARCHING_URL, payload);
                return;
            }
        }

        cancelSearch.set(false);

        final Set<BasicMovie> recommended = new LinkedHashSet<>();
        final List<BasicMovie> searched = new ArrayList<>();
        final List<BasicMovie> everyBasicMovie = new ArrayList<>(fileIoService.readMovieIdsFromFile());
        final List<BasicMovie> ownedBasicMovies = new ArrayList<>(fileIoService.readOwnedMovies(machineIdentifier, key));
        final AtomicInteger searchedMovieCount = new AtomicInteger(0);

        if (CollectionUtils.isEmpty(ownedBasicMovies)) {
            String reason = "Owned movies cannot be empty";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.OWNED_MOVIES_CANNOT_BE_EMPTY);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        try {
            StopWatch watch = new StopWatch();
            watch.start();
            searchForMovies(plexProperties, machineIdentifier, key, ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount);
            watch.stop();
            LOGGER.info("Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(watch.getTime()));
            LOGGER.info("Times used TVDB ID: {}", tempTvdbCounter);
        } catch (SearchCancelledException e) {
            String reason = "Search cancelled";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.OWNED_MOVIES_CANNOT_BE_EMPTY);
            notificationService.recommendedMoviesSearchFailed(plexServer, plexLibrary, reason);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } catch (IOException e) {
            String reason = "Search failed";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.SEARCH_FAILED);
            notificationService.recommendedMoviesSearchFailed(plexServer, plexLibrary, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } finally {
            cancelSearch.set(true);
        }

        notificationService.recommendedMoviesSearchFinished(plexServer, plexLibrary);

        //Always write to log
        fileIoService.writeRecommendedToFile(recommended, machineIdentifier, key);
        fileIoService.writeMovieIdsToFile(new TreeSet<>(everyBasicMovie));

        template.convertAndSend(FINISHED_SEARCHING_URL, Payload.SEARCH_SUCCESSFUL);

        LOGGER.info("Recommended");
        for (BasicMovie basicMovie : recommended) {
            String strMovie = basicMovie.toString();
            LOGGER.info(strMovie);
        }
    }

    @Override
    public void cancelSearch() {
        LOGGER.info("cancelSearch()");
        cancelSearch.set(true);
    }

    @Override
    public boolean isSearching() {
        LOGGER.info("isSearching() {}", !cancelSearch.get());
        return !cancelSearch.get();
    }

    /**
     * With all of the movies to search, now the connections to MovieDB need to be made. First we must search for
     * movie keys by movie name and year. With the movie key we can get full properties of a movie. Once we have the
     * full properties that contains the collection id, we can search that collection id for it's list of movies. We
     * compare the full collection list to the movies found in plex, any missing we add to the recommended list. To
     * optimize some network calls, we add movies found in a collection and in plex to our already searched list, so we
     * don't re-query collections again and again.
     */
    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private void searchForMovies(PlexProperties plexProperties, String machineIdentifier, Integer key, List<BasicMovie> ownedBasicMovies, List<BasicMovie> everyBasicMovie, Set<BasicMovie> recommended, List<BasicMovie> searched,
                                 AtomicInteger searchedMovieCount) throws SearchCancelledException, IOException {
        LOGGER.info("searchForMovies()");
        OkHttpClient client = new OkHttpClient();

        if (StringUtils.isEmpty(plexProperties.getMovieDbApiKey())) {
            plexProperties = fileIoService.readProperties();

            if (StringUtils.isEmpty(plexProperties.getMovieDbApiKey())) {
                final String error = "No MovieDb Key found. Need to configure key first.";
                LOGGER.error(error);
                throw new IllegalStateException(error);
            }
        }

        for (BasicMovie basicMovie : ownedBasicMovies) {
            String languageCode = "en-US";

            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search cancelled");
            }

            //Print the count first to handle the continue if block or the regular searching case
            if (searchedMovieCount.get() % 10 == 0) {
                LOGGER.info("{}% Complete. Processed {} files of {}.", ((int) ((searchedMovieCount.get()) / ((double) (ownedBasicMovies.size())) * 100)), searchedMovieCount.get(), ownedBasicMovies.size());
            }
            searchedMovieCount.incrementAndGet();

            if (searched.contains(basicMovie)) {
                continue;
            }

            HttpUrl searchMovieUrl;
            try {
                //If TMDB is available, skip the search
                //If IMDB is available use find
                //Otherwise, fall back to movie title and year search
                LOGGER.info(basicMovie.toString());
                if (basicMovie.getTmdbId() != -1 && basicMovie.getCollectionId() != -1) {
                    LOGGER.info("Used Collection ID to get {}", basicMovie.getName());
                    tempTvdbCounter.incrementAndGet();
                    handleCollection(plexProperties, machineIdentifier, key, ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
                    continue;
                } else if (basicMovie.getTmdbId() != -1) {
                    LOGGER.info("Used TVDB ID to get {}", basicMovie.getName());
                    tempTvdbCounter.incrementAndGet();
                    searchMovieDetails(plexProperties, machineIdentifier, key, ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
                    continue;
                } else if (StringUtils.isNotBlank(basicMovie.getImdbId())) {
                    LOGGER.info("Used 'find' to search for {}", basicMovie.getName());
                    String imdbId = URLEncoder.encode(basicMovie.getImdbId(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateFindMovieUrl(plexProperties.getMovieDbApiKey(), imdbId, languageCode);
                } else {
                    LOGGER.info("Used 'search' to search for {}", basicMovie.getName());
                    String name = URLEncoder.encode(basicMovie.getName(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateSearchMovieUrl(plexProperties.getMovieDbApiKey(), name, String.valueOf(basicMovie.getYear()), languageCode);
                }

                Request request = new Request.Builder()
                        .url(searchMovieUrl)
                        .build();

                String json = "";
                try (Response response = client.newCall(request).execute()) {

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        json = responseBody.string();
                    }

                    if (StringUtils.isEmpty(json)) {
                        LOGGER.error("Body returned null from TheMovieDB for: {}", basicMovie);
                        continue;
                    }

                    JsonNode foundMovies = objectMapper.readTree(json);
                    ArrayNode results;

                    if (foundMovies.has(MOVIE_RESULTS) &&
                            foundMovies.get(MOVIE_RESULTS).getNodeType().equals(JsonNodeType.ARRAY)) {
                        //Results from 'find'
                        results = (ArrayNode) foundMovies.get(MOVIE_RESULTS);
                    } else {
                        //Results from 'search'
                        results = (ArrayNode) foundMovies.get("results");
                    }

                    if (results == null) {
                        LOGGER.error("Results returned null from TheMovieDB for: {}", basicMovie);
                        continue;
                    }

                    if (results.size() == 0) {
                        LOGGER.error("Results not found for {}", basicMovie);
                        LOGGER.error("URL: {}", searchMovieUrl);
                        continue;
                    }

                    if (results.size() > 1) {
                        LOGGER.info("Results for {} came back with {}} results. Using first result.", basicMovie, results.size());
                        LOGGER.info("{} URL: {}", basicMovie, searchMovieUrl);
                    }

                    JsonNode result = results.get(0);
                    int id = result.get(ID).intValue();
                    basicMovie.setTmdbId(id);

                    int indexOfMovie = everyBasicMovie.indexOf(basicMovie);
                    if (indexOfMovie != -1) {
                        LOGGER.info("Merging movie data");
                        everyBasicMovie.get(indexOfMovie).setTmdbId(basicMovie.getTmdbId());
                    } else {
                        BasicMovie newBasicMovie = new BasicMovie.Builder(basicMovie.getName(), basicMovie.getYear())
                                .setTmdbId(basicMovie.getTmdbId())
                                .setImdbId(basicMovie.getImdbId())
                                .setCollectionTitle(basicMovie.getCollectionTitle())
                                .setCollectionId(basicMovie.getCollectionId())
                                .build();
                        everyBasicMovie.add(newBasicMovie);
                    }

                    searchMovieDetails(plexProperties, machineIdentifier, key, ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
                } catch (JsonProcessingException e) {
                    LOGGER.error(String.format("Error parsing movie %s.", basicMovie), e);
                    LOGGER.error("URL: {}", searchMovieUrl);
                } catch (IOException e) {
                    LOGGER.error(String.format("Error searching for movie %s.", basicMovie), e);
                    LOGGER.error("URL: {}", searchMovieUrl);
                } finally {
                    try {
                        //can't have too many connections to the movie database in a specific time, have to wait
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        LOGGER.error("Error sleeping", e);
                    }
                }
            } finally {
                try {
                    //can't have too many connections to the movie database in a specific time, have to wait
                    Thread.sleep(350);
                } catch (InterruptedException e) {
                    LOGGER.error("Error sleeping", e);
                }
            }
        }
    }

    private void searchMovieDetails(PlexProperties plexProperties, String machineIdentifier, Integer key, List<BasicMovie> ownedBasicMovies, List<BasicMovie> everyBasicMovie, Set<BasicMovie> recommended, List<BasicMovie> searched,
                                    AtomicInteger searchedMovieCount, BasicMovie basicMovie, OkHttpClient client, String languageCode) {
        LOGGER.info("searchMovieDetails()");
        HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(plexProperties.getMovieDbApiKey(), String.valueOf(basicMovie.getTmdbId()), languageCode);

        Request request = new Request.Builder()
                .url(movieDetailUrl)
                .build();

        try (Response movieDetailResponse = client.newCall(request).execute()) {
            String movieDetailJson = "";
            ResponseBody responseBody = movieDetailResponse.body();
            if (responseBody != null) {
                movieDetailJson = responseBody.string();
            }

            if (StringUtils.isEmpty(movieDetailJson)) {
                LOGGER.error("Body returned null from TheMovieDB for details on {}", basicMovie.getName());
                return;
            }

            JsonNode movieDetails = objectMapper.readTree(movieDetailJson);

            if (!movieDetails.has(COLLECTION_ID) || movieDetails.get(COLLECTION_ID).isNull()) {
                //No collection found, just add movie to searched and continue
                LOGGER.info("No collection found for {}", basicMovie.getName());
                searched.add(basicMovie);
                return;
            }

            int collectionId = movieDetails.get(COLLECTION_ID).get(ID).intValue();
            String collectionName = movieDetails.get(COLLECTION_ID).get(NAME).textValue();
            basicMovie.setCollectionId(collectionId);
            basicMovie.setCollectionTitle(collectionName);

            int indexOfMovie = everyBasicMovie.indexOf(basicMovie);
            if (indexOfMovie != -1) {
                LOGGER.info("Merging movie data");
                everyBasicMovie.get(indexOfMovie).setTmdbId(basicMovie.getTmdbId());
                everyBasicMovie.get(indexOfMovie).setCollectionId(basicMovie.getCollectionId());
                everyBasicMovie.get(indexOfMovie).setCollectionTitle(basicMovie.getCollectionTitle());
            } else {
                BasicMovie newBasicMovie = new BasicMovie.Builder(basicMovie.getName(), basicMovie.getYear())
                        .setTmdbId(basicMovie.getTmdbId())
                        .setImdbId(basicMovie.getImdbId())
                        .setCollectionTitle(basicMovie.getCollectionTitle())
                        .setCollectionId(basicMovie.getCollectionId())
                        .build();
                everyBasicMovie.add(newBasicMovie);
            }

            handleCollection(plexProperties, machineIdentifier, key, ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting movie details %s", basicMovie), e);
        }
    }

    private void handleCollection(PlexProperties plexProperties, String machineIdentifier, Integer key, List<BasicMovie> ownedBasicMovies, List<BasicMovie> everyBasicMovie, Set<BasicMovie> recommended, List<BasicMovie> searched,
                                  AtomicInteger searchedMovieCount, BasicMovie basicMovie, OkHttpClient client, String languageCode) {
        LOGGER.info("handleCollection()");
        HttpUrl collectionUrl = urlGenerator.generateCollectionUrl(plexProperties.getMovieDbApiKey(), String.valueOf(basicMovie.getCollectionId()), languageCode);

        Request request = new Request.Builder()
                .url(collectionUrl)
                .build();

        try (Response collectionResponse = client.newCall(request).execute()) {
            String collectionJson = "";
            ResponseBody responseBody = collectionResponse.body();
            if (responseBody != null) {
                collectionJson = responseBody.string();
            }

            if (StringUtils.isEmpty(collectionJson)) {
                LOGGER.error("Body returned null from TheMovieDB for collection information about {}", basicMovie.getName());
                return;
            }

            JsonNode collection = objectMapper.readTree(collectionJson);

            if (collection.has("status_code") && collection.get("status_code").intValue() == 34) {
                LOGGER.warn(collection.get("status_message").textValue());
                return;
            }

            int indexOfMovie = everyBasicMovie.indexOf(basicMovie);

            List<MovieFromCollection> moviesInCollection = new ArrayList<>();
            if (collection.has(PARTS)) {
                JsonNode parts = collection.get(PARTS);
                parts.iterator().forEachRemaining(jsonNode -> {
                    String title = jsonNode.get(TITLE).textValue();
                    int year = 0;
                    if (jsonNode.has(RELEASE_DATE)) {
                        String oldDate = jsonNode.get(RELEASE_DATE).textValue();

                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            year = calendar.get(Calendar.YEAR);
                        } catch (ParseException e) {
                            LOGGER.warn("Could not parse date");
                        }
                    }
                    String id = jsonNode.get(ID).textValue();

                    BasicMovie collectionBasicMovie = new BasicMovie.Builder(title, year).build();
                    LOGGER.info(collectionBasicMovie.toString());

                    Boolean owned = ownedBasicMovies.contains(collectionBasicMovie);
                    moviesInCollection.add(new MovieFromCollection(title, id, owned));
                });
            }

            LOGGER.info("MoviesInCollection: {}", Arrays.toString(moviesInCollection.toArray()));

            if (indexOfMovie != -1) {
                LOGGER.info("Movie found: {}", basicMovie);
                int id = collection.get(ID).intValue();
                String name = collection.get(NAME).textValue();
                everyBasicMovie.get(indexOfMovie).setCollectionId(id);
                everyBasicMovie.get(indexOfMovie).setCollectionTitle(name);
                basicMovie.setCollectionTitle(name);
                basicMovie.setCollectionId(id);
                basicMovie.getMoviesInCollection().addAll(moviesInCollection);
            } else {
                LOGGER.info("Movie not found: {}", basicMovie);
                int collectionId = collection.get(ID).intValue();
                String collectionName = collection.get(NAME).textValue();
                BasicMovie newBasicMovie = new BasicMovie.Builder(basicMovie.getName(), basicMovie.getYear())
                        .setTmdbId(basicMovie.getTmdbId())
                        .setImdbId(basicMovie.getImdbId())
                        .setCollectionTitle(collectionName)
                        .setCollectionId(collectionId)
                        .setMoviesInCollection(moviesInCollection)
                        .setLanguage(basicMovie.getLanguage())
                        .setOverview(basicMovie.getOverview())
                        .setPosterUrl(basicMovie.getPosterUrl())
                        .build();
                everyBasicMovie.add(newBasicMovie);

                basicMovie.setCollectionTitle(collectionName);
                basicMovie.setCollectionId(collectionId);
            }

            ArrayNode parts = (ArrayNode) collection.get(PARTS);
            for (JsonNode part : parts) {
                int tmdbId = part.get(ID).intValue();
                //Files can't have : so need to remove to find matches correctly
                String title = part.get(TITLE).textValue();
                int year;
                try {
                    if (part.has(RELEASE_DATE) && StringUtils.isNotEmpty(part.get(RELEASE_DATE).textValue())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                        LocalDate date = LocalDate.parse(part.get(RELEASE_DATE).textValue(), formatter);
                        year = date.getYear();
                    } else {
                        LOGGER.warn("No year found for {}. Value returned was empty. Not adding the movie to recommended list.", title);
                        continue;
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                    LOGGER.warn("No year found for {}. Value returned was empty. Not adding the movie to recommended list.", title);
                    continue;
                }

                String posterUrl = "";
                try {
                    posterUrl = part.get("poster_url").textValue();
                } catch (Exception e) {
                    LOGGER.info("No poster found for {}.", title);
                }

                BasicMovie basicMovieFromCollection = new BasicMovie.Builder(title, year)
                        .setTmdbId(tmdbId)
                        .setCollectionId(basicMovie.getCollectionId())
                        .setCollectionTitle(basicMovie.getCollectionTitle())
                        .setPosterUrl(posterUrl)
                        .setMoviesInCollection(moviesInCollection)
                        .build();

                if (ownedBasicMovies.contains(basicMovieFromCollection)) {
                    LOGGER.info("Skip owned movie: {}", basicMovieFromCollection);
                    continue;
                }

                indexOfMovie = everyBasicMovie.indexOf(basicMovieFromCollection);
                if (indexOfMovie == -1) {
                    LOGGER.info("Adding collection movie");
                    everyBasicMovie.add(basicMovieFromCollection);
                } else {
                    LOGGER.info("Merging collection movie");
                    everyBasicMovie.get(indexOfMovie).setTmdbId(tmdbId);
                }

                if (ownedBasicMovies.contains(basicMovieFromCollection)) {
                    LOGGER.info("Owned movie found: {}", basicMovieFromCollection);
                    searched.add(basicMovieFromCollection);
                    sendEmptySearchUpdate(ownedBasicMovies.size(), searchedMovieCount);
                } else if (!searched.contains(basicMovieFromCollection) && year != 0 && year < Year.now().getValue()) {
                    LOGGER.info("Missing movie found: {}", basicMovieFromCollection);

                    // Get recommended Movie details from MovieDB API
                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(plexProperties.getMovieDbApiKey(), String.valueOf(basicMovieFromCollection.getTmdbId()), languageCode);

                    Request newReq = new Request.Builder()
                            .url(movieDetailUrl)
                            .build();

                    try (Response movieDetailResponse = client.newCall(newReq).execute()) {
                        String movieDetailJson = "";
                        ResponseBody movieDetailResponseBody = movieDetailResponse.body();
                        if (movieDetailResponseBody != null) {
                            movieDetailJson = movieDetailResponseBody.string();
                        }

                        LOGGER.info(movieDetailJson);

                        if (StringUtils.isEmpty(movieDetailJson)) {
                            LOGGER.error("Body returned null from TheMovieDB for details on {}", basicMovie.getName());
                            return;
                        }

                        JsonNode movieDet = objectMapper.readTree(movieDetailJson);

                        // Get the release year from movie release date
                        if (movieDet.has(RELEASE_DATE)) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            LocalDate date = LocalDate.parse(movieDet.get(RELEASE_DATE).textValue(), formatter);
                            year = date.getYear();
                        } else {
                            LOGGER.warn("No year found for {}. Value returned was '{}'. Not adding the movie to recommended list.", title, movieDet.get(RELEASE_DATE));
                            continue;
                        }

                        if (collection.has(NAME)) {
                            basicMovie.setCollectionTitle(collection.get(NAME).textValue());
                            basicMovieFromCollection.setCollectionTitle(collection.get(NAME).textValue());
                        }

                        // Add movie with imbd_id and other details for RSS to recommended list
                        BasicMovie recommendedBasicMovie = new BasicMovie.Builder(movieDet.get(TITLE).textValue(), year)
                                .setTmdbId(movieDet.get(ID).intValue())
                                .setImdbId(movieDet.get("imdb_id").textValue())
                                .setCollectionId(basicMovie.getCollectionId())
                                .setCollectionTitle(basicMovie.getCollectionTitle())
                                .setPosterUrl("https://image.tmdb.org/t/p/w185/" + movieDet.get("poster_path").textValue())
                                .setOverview(movieDet.get("overview").textValue())
                                .setMoviesInCollection(moviesInCollection)
                                .build();

                        if (ownedBasicMovies.contains(recommendedBasicMovie)) {
                            LOGGER.info("Skip owned movie: {}", recommendedBasicMovie);
                            continue;
                        }

                        if (recommended.add(recommendedBasicMovie)) {
                            // Write current list of recommended movies to file.
                            fileIoService.writeRssFile(machineIdentifier, key, new HashSet<>(recommended));

                            LOGGER.info("/newMovieFound:{}", recommendedBasicMovie);

                            //Send message over websocket
                            SearchResults searchResults = new SearchResults(searchedMovieCount.get(), ownedBasicMovies.size(), recommendedBasicMovie);
                            template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }

                } else {
                    sendEmptySearchUpdate(ownedBasicMovies.size(), searchedMovieCount);
                }
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting collections %s.", basicMovie), e);
        }

        searched.add(basicMovie);
    }

    private void sendEmptySearchUpdate(int totalMovieCount, AtomicInteger searchedMovieCount) throws JsonProcessingException {
        //Send message over websocket
        //No new movie, just updated counts
        SearchResults searchResults = new SearchResults(searchedMovieCount.get(), totalMovieCount, null);
        template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
    }

}
