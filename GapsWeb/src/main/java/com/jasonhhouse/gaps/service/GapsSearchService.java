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
import com.jasonhhouse.gaps.MovieFromCollection;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.SearchCancelledException;
import com.jasonhhouse.gaps.SearchResults;
import com.jasonhhouse.gaps.UrlGenerator;
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

    private final IoService ioService;

    private final TmdbService tmdbService;

    private final GapsService gapsService;

    @Autowired
    public GapsSearchService(@Qualifier("real") UrlGenerator urlGenerator, SimpMessagingTemplate template, IoService ioService, TmdbService tmdbService, GapsService gapsService) {
        this.template = template;
        this.tmdbService = tmdbService;
        this.gapsService = gapsService;
        this.urlGenerator = urlGenerator;
        this.ioService = ioService;

        tempTvdbCounter = new AtomicInteger();
        cancelSearch = new AtomicBoolean(true);
    }

    @Override
    public void run(String machineIdentifier, Integer key) {
        LOGGER.info("run( {}, {} )", machineIdentifier, key);

        if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
            Payload payload = tmdbService.testTmdbKey(gapsService.getPlexSearch().getMovieDbApiKey());
            if (payload != Payload.TMDB_KEY_VALID) {
                LOGGER.error(payload.getReason());
                template.convertAndSend(FINISHED_SEARCHING_URL, payload);
                return;
            }
        }

        cancelSearch.set(false);

        final Set<Movie> recommended = new LinkedHashSet<>();
        final List<Movie> searched = new ArrayList<>();
        final List<Movie> everyMovie = new ArrayList<>(ioService.readMovieIdsFromFile());
        final List<Movie> ownedMovies = new ArrayList<>(ioService.readOwnedMovies(machineIdentifier, key));
        final AtomicInteger searchedMovieCount = new AtomicInteger(0);

        if (CollectionUtils.isEmpty(ownedMovies)) {
            String reason = "Owned movies cannot be empty";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.OWNED_MOVIES_CANNOT_BE_EMPTY);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        try {
            StopWatch watch = new StopWatch();
            watch.start();
            searchForMovies(machineIdentifier, key, ownedMovies, everyMovie, recommended, searched, searchedMovieCount);
            watch.stop();
            LOGGER.info("Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(watch.getTime()));
            LOGGER.info("Times used TVDB ID: {}", tempTvdbCounter);
        } catch (SearchCancelledException e) {
            String reason = "Search cancelled";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.OWNED_MOVIES_CANNOT_BE_EMPTY);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } catch (IOException e) {
            String reason = "Search failed";
            LOGGER.error(reason);
            template.convertAndSend(FINISHED_SEARCHING_URL, Payload.SEARCH_FAILED);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
        } finally {
            cancelSearch.set(true);
        }

        //Always write to log
        ioService.writeRecommendedToFile(recommended, machineIdentifier, key);
        ioService.writeMovieIdsToFile(new TreeSet<>(everyMovie));

        template.convertAndSend(FINISHED_SEARCHING_URL, Payload.SEARCH_SUCCESSFUL);

        LOGGER.info("Recommended");
        for (Movie movie : recommended) {
            String strMovie = movie.toString();
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
        LOGGER.info("isSearching()");
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
    private void searchForMovies(String machineIdentifier, Integer key, List<Movie> ownedMovies, List<Movie> everyMovie, Set<Movie> recommended, List<Movie> searched,
                                 AtomicInteger searchedMovieCount) throws SearchCancelledException, IOException {
        LOGGER.info("searchForMovies()");
        OkHttpClient client = new OkHttpClient();

        if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
            gapsService.updatePlexSearch(ioService.readProperties());

            if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
                final String error = "No MovieDb Key found. Need to configure key first.";
                LOGGER.error(error);
                throw new IllegalStateException(error);
            }
        }

        for (Movie movie : ownedMovies) {
            String languageCode = "en-US";

            //Cancel search if needed
            if (cancelSearch.get()) {
                throw new SearchCancelledException("Search cancelled");
            }

            //Print the count first to handle the continue if block or the regular searching case
            if (searchedMovieCount.get() % 10 == 0) {
                LOGGER.info("{}% Complete. Processed {} files of {}.", ((int) ((searchedMovieCount.get()) / ((double) (ownedMovies.size())) * 100)), searchedMovieCount.get(), ownedMovies.size());
            }
            searchedMovieCount.incrementAndGet();

            if (searched.contains(movie)) {
                continue;
            }

            HttpUrl searchMovieUrl;
            try {
                //If TMDB is available, skip the search
                //If IMDB is available use find
                //Otherwise, fall back to movie title and year search
                LOGGER.info(movie.toString());
                if (movie.getTvdbId() != -1 && movie.getCollectionId() != -1) {
                    LOGGER.info("Used Collection ID to get {}", movie.getName());
                    tempTvdbCounter.incrementAndGet();
                    handleCollection(machineIdentifier, key, ownedMovies, everyMovie, recommended, searched, searchedMovieCount, movie, client, languageCode);
                    continue;
                } else if (movie.getTvdbId() != -1) {
                    LOGGER.info("Used TVDB ID to get {}", movie.getName());
                    tempTvdbCounter.incrementAndGet();
                    searchMovieDetails(machineIdentifier, key, ownedMovies, everyMovie, recommended, searched, searchedMovieCount, movie, client, languageCode);
                    continue;
                } else if (StringUtils.isNotBlank(movie.getImdbId())) {
                    LOGGER.info("Used 'find' to search for {}", movie.getName());
                    String imdbId = URLEncoder.encode(movie.getImdbId(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateFindMovieUrl(gapsService.getPlexSearch().getMovieDbApiKey(), imdbId, languageCode);
                } else {
                    LOGGER.info("Used 'search' to search for {}", movie.getName());
                    String name = URLEncoder.encode(movie.getName(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateSearchMovieUrl(gapsService.getPlexSearch().getMovieDbApiKey(), name, String.valueOf(movie.getYear()), languageCode);
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
                        LOGGER.error("Body returned null from TheMovieDB for: {}", movie);
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
                        LOGGER.error("Results returned null from TheMovieDB for: {}", movie);
                        continue;
                    }

                    if (results.size() == 0) {
                        LOGGER.error("Results not found for {}", movie);
                        LOGGER.error("URL: {}", searchMovieUrl);
                        continue;
                    }

                    if (results.size() > 1) {
                        LOGGER.info("Results for {} came back with {}} results. Using first result.", movie, results.size());
                        LOGGER.info("{} URL: {}", movie, searchMovieUrl);
                    }

                    JsonNode result = results.get(0);
                    int id = result.get(ID).intValue();
                    movie.setTvdbId(id);

                    int indexOfMovie = everyMovie.indexOf(movie);
                    if (indexOfMovie != -1) {
                        LOGGER.info("Merging movie data");
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

                    searchMovieDetails(machineIdentifier, key, ownedMovies, everyMovie, recommended, searched, searchedMovieCount, movie, client, languageCode);
                } catch (JsonProcessingException e) {
                    LOGGER.error(String.format("Error parsing movie %s.", movie), e);
                    LOGGER.error("URL: {}", searchMovieUrl);
                } catch (IOException e) {
                    LOGGER.error(String.format("Error searching for movie %s.", movie), e);
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

    private void searchMovieDetails(String machineIdentifier, Integer key, List<Movie> ownedMovies, List<Movie> everyMovie, Set<Movie> recommended, List<Movie> searched,
                                    AtomicInteger searchedMovieCount, Movie movie, OkHttpClient client, String languageCode) {
        LOGGER.info("searchMovieDetails()");
        HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movie.getTvdbId()), languageCode);

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
                LOGGER.error("Body returned null from TheMovieDB for details on {}", movie.getName());
                return;
            }

            JsonNode movieDetails = objectMapper.readTree(movieDetailJson);

            if (!movieDetails.has(COLLECTION_ID) || movieDetails.get(COLLECTION_ID).isNull()) {
                //No collection found, just add movie to searched and continue
                LOGGER.info("No collection found for {}", movie.getName());
                searched.add(movie);
                return;
            }

            int collectionId = movieDetails.get(COLLECTION_ID).get(ID).intValue();
            String collectionName = movieDetails.get(COLLECTION_ID).get(NAME).textValue();
            movie.setCollectionId(collectionId);
            movie.setCollection(collectionName);

            int indexOfMovie = everyMovie.indexOf(movie);
            if (indexOfMovie != -1) {
                LOGGER.info("Merging movie data");
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

            handleCollection(machineIdentifier, key, ownedMovies, everyMovie, recommended, searched, searchedMovieCount, movie, client, languageCode);

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting movie details %s", movie), e);
        }
    }

    private void handleCollection(String machineIdentifier, Integer key, List<Movie> ownedMovies, List<Movie> everyMovie, Set<Movie> recommended, List<Movie> searched,
                                  AtomicInteger searchedMovieCount, Movie movie, OkHttpClient client, String languageCode) {
        LOGGER.info("handleCollection()");
        HttpUrl collectionUrl = urlGenerator.generateCollectionUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movie.getCollectionId()), languageCode);

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
                LOGGER.error("Body returned null from TheMovieDB for collection information about {}", movie.getName());
                return;
            }

            JsonNode collection = objectMapper.readTree(collectionJson);

            if (collection.has("status_code") && collection.get("status_code").intValue() == 34) {
                LOGGER.warn(collection.get("status_message").textValue());
                return;
            }

            int indexOfMovie = everyMovie.indexOf(movie);

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

                    Movie collectionMovie = new Movie.Builder(title, year).build();
                    LOGGER.info(collectionMovie.toString());

                    Boolean owned = ownedMovies.contains(collectionMovie);
                    moviesInCollection.add(new MovieFromCollection(title, id, owned));
                });
            }

            LOGGER.info("MoviesInCollection: {}", Arrays.toString(moviesInCollection.toArray()));

            if (indexOfMovie != -1) {
                LOGGER.info("Movie found: {}", movie);
                int id = collection.get(ID).intValue();
                String name = collection.get(NAME).textValue();
                everyMovie.get(indexOfMovie).setCollectionId(id);
                everyMovie.get(indexOfMovie).setCollection(name);
                movie.setCollection(name);
                movie.setCollectionId(id);
                movie.getMoviesInCollection().addAll(moviesInCollection);
            } else {
                LOGGER.info("Movie not found: {}", movie);
                int collectionId = collection.get(ID).intValue();
                String collectionName = collection.get(NAME).textValue();
                Movie newMovie = new Movie.Builder(movie.getName(), movie.getYear())
                        .setTvdbId(movie.getTvdbId())
                        .setImdbId(movie.getImdbId())
                        .setCollection(collectionName)
                        .setCollectionId(collectionId)
                        .setMoviesInCollection(moviesInCollection)
                        .setLanguage(movie.getLanguage())
                        .setOverview(movie.getOverview())
                        .setPosterUrl(movie.getPosterUrl())
                        .build();
                everyMovie.add(newMovie);

                movie.setCollection(collectionName);
                movie.setCollectionId(collectionId);
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

                Movie movieFromCollection = new Movie.Builder(title, year)
                        .setTvdbId(tmdbId)
                        .setCollectionId(movie.getCollectionId())
                        .setCollection(movie.getCollection())
                        .setPosterUrl(posterUrl)
                        .setMoviesInCollection(moviesInCollection)
                        .build();

                if (ownedMovies.contains(movieFromCollection)) {
                    LOGGER.info("Skip owned movie: {}", movieFromCollection);
                    continue;
                }

                indexOfMovie = everyMovie.indexOf(movieFromCollection);
                if (indexOfMovie == -1) {
                    LOGGER.info("Adding collection movie");
                    everyMovie.add(movieFromCollection);
                } else {
                    LOGGER.info("Merging collection movie");
                    everyMovie.get(indexOfMovie).setTvdbId(tmdbId);
                }

                if (ownedMovies.contains(movieFromCollection)) {
                    LOGGER.info("Owned movie found: {}", movieFromCollection);
                    searched.add(movieFromCollection);
                    sendEmptySearchUpdate(ownedMovies.size(), searchedMovieCount);
                } else if (!searched.contains(movieFromCollection) && year != 0 && year < Year.now().getValue()) {
                    LOGGER.info("Missing movie found: {}", movieFromCollection);

                    // Get recommended Movie details from MovieDB API
                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(gapsService.getPlexSearch().getMovieDbApiKey(), String.valueOf(movieFromCollection.getTvdbId()), languageCode);

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
                            LOGGER.error("Body returned null from TheMovieDB for details on {}", movie.getName());
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
                            movie.setCollection(collection.get(NAME).textValue());
                            movieFromCollection.setCollection(collection.get(NAME).textValue());
                        }

                        // Add movie with imbd_id and other details for RSS to recommended list
                        Movie recommendedMovie = new Movie.Builder(movieDet.get(TITLE).textValue(), year)
                                .setTvdbId(movieDet.get(ID).intValue())
                                .setImdbId(movieDet.get("imdb_id").textValue())
                                .setCollectionId(movie.getCollectionId())
                                .setCollection(movie.getCollection())
                                .setPosterUrl("https://image.tmdb.org/t/p/w185/" + movieDet.get("poster_path").textValue())
                                .setOverview(movieDet.get("overview").textValue())
                                .setMoviesInCollection(moviesInCollection)
                                .build();

                        if (ownedMovies.contains(recommendedMovie)) {
                            LOGGER.info("Skip owned movie: {}", recommendedMovie);
                            continue;
                        }

                        if (recommended.add(recommendedMovie)) {
                            // Write current list of recommended movies to file.
                            ioService.writeRssFile(machineIdentifier, key, new HashSet<>(recommended));

                            LOGGER.info("/newMovieFound:{}", recommendedMovie);

                            //Send message over websocket
                            SearchResults searchResults = new SearchResults(searchedMovieCount.get(), ownedMovies.size(), recommendedMovie);
                            template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }

                } else {
                    sendEmptySearchUpdate(ownedMovies.size(), searchedMovieCount);
                }
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting collections %s.", movie), e);
        }

        searched.add(movie);
    }

    private void sendEmptySearchUpdate(int totalMovieCount, AtomicInteger searchedMovieCount) throws JsonProcessingException {
        //Send message over websocket
        //No new movie, just updated counts
        SearchResults searchResults = new SearchResults(searchedMovieCount.get(), totalMovieCount, null);
        template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
    }

}
