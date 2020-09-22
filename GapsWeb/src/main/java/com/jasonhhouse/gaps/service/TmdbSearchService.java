/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.jasonhhouse.gaps.MovieFromCollection;
import com.jasonhhouse.gaps.SearchResults;
import com.jasonhhouse.gaps.UrlGenerator;
import com.jasonhhouse.gaps.exception.SearchCancelledException;
import com.jasonhhouse.gaps.movie.BasicMovie;
import com.jasonhhouse.gaps.movie.GapsMovie;
import com.jasonhhouse.gaps.movie.InputMovie;
import com.jasonhhouse.gaps.movie.OutputMovie;
import com.jasonhhouse.gaps.movie.TmdbMovie;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TmdbSearchService extends AbstractOutputSearchService<TmdbFileOutputIo> {

    public static final String COLLECTION_ID = "belongs_to_collection";
    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String RELEASE_DATE = "release_date";
    public static final String PARTS = "parts";
    public static final String MOVIE_RESULTS = "movie_results";

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbSearchService.class);

    @NotNull
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @NotNull
    private final UrlGenerator urlGenerator;

    @NotNull
    private final SimpMessagingTemplate template;

    @NotNull
    private final AtomicInteger tempTvdbCounter;

    @NotNull
    private final FileIoService fileIoService;

    @NotNull
    private final TmdbQueryService tmdbQueryService;

    @NotNull
    private final NotificationService notificationService;

    @NotNull
    private final String tmdbApiKey;

    @NotNull
    private final TmdbOutputFileConfig tmdbOutputFileConfig;

    @Autowired
    public TmdbSearchService(@NotNull @Qualifier("real") UrlGenerator urlGenerator,
                             @NotNull SimpMessagingTemplate template,
                             @NotNull FileIoService fileIoService,
                             @NotNull TmdbQueryService tmdbQueryService,
                             @NotNull NotificationService notificationService,
                             @NotNull TmdbFileOutputIo outputIo) {
        super(new AtomicBoolean(true), outputIo);
        this.template = template;
        this.tmdbQueryService = tmdbQueryService;
        this.urlGenerator = urlGenerator;
        this.fileIoService = fileIoService;
        this.notificationService = notificationService;
        tempTvdbCounter = new AtomicInteger();
        tmdbApiKey = fileIoService.readProperties().getMovieDbApiKey();
        this.tmdbOutputFileConfig = new TmdbOutputFileConfig();
    }

    @Override
    public void searchForMovies(@NotNull List<InputMovie> ownedBasicMovies,
                                @NotNull List<GapsMovie> everyBasicMovie,
                                @NotNull Set<OutputMovie> recommended,
                                @NotNull List<GapsMovie> searched,
                                @NotNull AtomicInteger searchedMovieCount) throws SearchCancelledException {
        LOGGER.info("searchForMovies()");
        OkHttpClient client = new OkHttpClient();

        for (InputMovie basicMovie : ownedBasicMovies) {
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
                    handleCollection(ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
                    continue;
                } else if (basicMovie.getTmdbId() != -1) {
                    LOGGER.info("Used TVDB ID to get {}", basicMovie.getName());
                    tempTvdbCounter.incrementAndGet();
                    searchMovieDetails(ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
                    continue;
                } else if (StringUtils.isNotBlank(basicMovie.getImdbId())) {
                    LOGGER.info("Used 'find' to search for {}", basicMovie.getName());
                    String imdbId = URLEncoder.encode(basicMovie.getImdbId(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateFindMovieUrl(tmdbApiKey, imdbId, languageCode);
                } else {
                    LOGGER.info("Used 'search' to search for {}", basicMovie.getName());
                    String name = URLEncoder.encode(basicMovie.getName(), StandardCharsets.UTF_8);
                    searchMovieUrl = urlGenerator.generateSearchMovieUrl(tmdbApiKey, name, String.valueOf(basicMovie.getYear()), languageCode);
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

                    searchMovieDetails(ownedBasicMovies, everyBasicMovie, recommended, searched, searchedMovieCount, basicMovie, client, languageCode);
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

    private void searchMovieDetails(List<InputMovie> ownedInputMovies,
                                    List<GapsMovie> everyGapsMovie,
                                    Set<OutputMovie> recommended,
                                    List<GapsMovie> searched,
                                    AtomicInteger searchedMovieCount,
                                    InputMovie inputMovie,
                                    OkHttpClient client,
                                    String languageCode) {
        LOGGER.info("searchMovieDetails()");
        HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(tmdbApiKey, String.valueOf(inputMovie.getTmdbId()), languageCode);

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
                LOGGER.error("Body returned null from TheMovieDB for details on {}", inputMovie.getName());
                return;
            }

            JsonNode movieDetails = objectMapper.readTree(movieDetailJson);

            if (!movieDetails.has(COLLECTION_ID) || movieDetails.get(COLLECTION_ID).isNull()) {
                //No collection found, just add movie to searched and continue
                LOGGER.info("No collection found for {}", inputMovie.getName());
                searched.add(inputMovie);
                return;
            }

            int collectionId = movieDetails.get(COLLECTION_ID).get(ID).intValue();
            String collectionName = movieDetails.get(COLLECTION_ID).get(NAME).textValue();
            inputMovie.setCollectionId(collectionId);
            inputMovie.setCollectionTitle(collectionName);

            int indexOfMovie = everyGapsMovie.indexOf(inputMovie);
            if (indexOfMovie != -1) {
                LOGGER.info("Merging movie data");
                everyGapsMovie.get(indexOfMovie).setTmdbId(inputMovie.getTmdbId());
                everyGapsMovie.get(indexOfMovie).setCollectionId(inputMovie.getCollectionId());
                everyGapsMovie.get(indexOfMovie).setCollectionTitle(inputMovie.getCollectionTitle());
            } else {
                TmdbMovie newBasicMovie = new TmdbMovie.Builder(inputMovie.getName(), inputMovie.getYear())
                        .setTmdbId(inputMovie.getTmdbId())
                        .setImdbId(inputMovie.getImdbId())
                        .setCollectionTitle(inputMovie.getCollectionTitle())
                        .setCollectionId(inputMovie.getCollectionId())
                        .build();
                everyGapsMovie.add(newBasicMovie);
            }

            handleCollection(ownedInputMovies, everyGapsMovie, recommended, searched, searchedMovieCount, inputMovie, client, languageCode);

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting movie details %s", inputMovie), e);
        }
    }

    private void handleCollection(@NotNull List<InputMovie> ownedInputMovies,
                                  @NotNull List<GapsMovie> everyGapsMovie,
                                  @NotNull Set<OutputMovie> recommended,
                                  @NotNull List<GapsMovie> searched,
                                  @NotNull AtomicInteger searchedMovieCount,
                                  @NotNull InputMovie inputMovie,
                                  @NotNull OkHttpClient client,
                                  @NotNull String languageCode) {
        LOGGER.info("handleCollection()");
        HttpUrl collectionUrl = urlGenerator.generateCollectionUrl(tmdbApiKey, String.valueOf(inputMovie.getCollectionId()), languageCode);

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
                LOGGER.error("Body returned null from TheMovieDB for collection information about {}", inputMovie.getName());
                return;
            }

            JsonNode collection = objectMapper.readTree(collectionJson);

            if (collection.has("status_code") && collection.get("status_code").intValue() == 34) {
                LOGGER.warn(collection.get("status_message").textValue());
                return;
            }

            int indexOfMovie = everyGapsMovie.indexOf(inputMovie);

            List<MovieFromCollection> moviesFromCollections = new ArrayList<>();
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

                    TmdbMovie collectionBasicMovie = new TmdbMovie.Builder(title, year).build();
                    LOGGER.info(collectionBasicMovie.toString());

                    Boolean owned = ownedInputMovies.contains(collectionBasicMovie);
                    moviesFromCollections.add(new MovieFromCollection(title, id, owned));
                });
            }

            LOGGER.info("MoviesInCollection: {}", Arrays.toString(moviesFromCollections.toArray()));

            if (indexOfMovie != -1) {
                LOGGER.info("Movie found: {}", inputMovie);
                int id = collection.get(ID).intValue();
                String name = collection.get(NAME).textValue();
                everyGapsMovie.get(indexOfMovie).setCollectionId(id);
                everyGapsMovie.get(indexOfMovie).setCollectionTitle(name);
                inputMovie.setCollectionTitle(name);
                inputMovie.setCollectionId(id);
            } else {
                LOGGER.info("Movie not found: {}", inputMovie);
                int collectionId = collection.get(ID).intValue();
                String collectionName = collection.get(NAME).textValue();
                TmdbMovie newBasicMovie = new TmdbMovie.Builder(inputMovie.getName(), inputMovie.getYear())
                        .setMoviesFromCollections(moviesFromCollections)
                        .setTmdbId(inputMovie.getTmdbId())
                        .setImdbId(inputMovie.getImdbId())
                        .setCollectionTitle(collectionName)
                        .setCollectionId(collectionId)
                        .setLanguage(inputMovie.getLanguage())
                        .setOverview(inputMovie.getOverview())
                        .setPosterUrl(inputMovie.getPosterUrl())
                        .build();
                everyGapsMovie.add(newBasicMovie);

                inputMovie.setCollectionTitle(collectionName);
                inputMovie.setCollectionId(collectionId);
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

                TmdbMovie basicMovieFromCollection = new TmdbMovie.Builder(title, year)
                        .setMoviesFromCollections(moviesFromCollections)
                        .setTmdbId(tmdbId)
                        .setCollectionId(inputMovie.getCollectionId())
                        .setCollectionTitle(inputMovie.getCollectionTitle())
                        .setPosterUrl(posterUrl)
                        .build();

                if (ownedInputMovies.contains(basicMovieFromCollection)) {
                    LOGGER.info("Skip owned movie: {}", basicMovieFromCollection);
                    continue;
                }

                indexOfMovie = everyGapsMovie.indexOf(basicMovieFromCollection);
                if (indexOfMovie == -1) {
                    LOGGER.info("Adding collection movie");
                    everyGapsMovie.add(basicMovieFromCollection);
                } else {
                    LOGGER.info("Merging collection movie");
                    everyGapsMovie.get(indexOfMovie).setTmdbId(tmdbId);
                }

                if (ownedInputMovies.contains(basicMovieFromCollection)) {
                    LOGGER.info("Owned movie found: {}", basicMovieFromCollection);
                    searched.add(basicMovieFromCollection);
                    sendEmptySearchUpdate(ownedInputMovies.size(), searchedMovieCount);
                } else if (!searched.contains(basicMovieFromCollection) && year != 0 && year < Year.now().getValue()) {
                    LOGGER.info("Missing movie found: {}", basicMovieFromCollection);

                    // Get recommended Movie details from MovieDB API
                    HttpUrl movieDetailUrl = urlGenerator.generateMovieDetailUrl(tmdbApiKey, String.valueOf(basicMovieFromCollection.getTmdbId()), languageCode);

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
                            LOGGER.error("Body returned null from TheMovieDB for details on {}", inputMovie.getName());
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
                            inputMovie.setCollectionTitle(collection.get(NAME).textValue());
                            basicMovieFromCollection.setCollectionTitle(collection.get(NAME).textValue());
                        }

                        // Add movie with imbd_id and other details for RSS to recommended list
                        TmdbMovie recommendedOutputMovie = new TmdbMovie.Builder(movieDet.get(TITLE).textValue(), year)
                                .setMoviesFromCollections(moviesFromCollections)
                                .setTmdbId(movieDet.get(ID).intValue())
                                .setImdbId(movieDet.get("imdb_id").textValue())
                                .setCollectionId(inputMovie.getCollectionId())
                                .setCollectionTitle(inputMovie.getCollectionTitle())
                                .setPosterUrl("https://image.tmdb.org/t/p/w185/" + movieDet.get("poster_path").textValue())
                                .setOverview(movieDet.get("overview").textValue())
                                .build();

                        if (ownedInputMovies.contains(recommendedOutputMovie)) {
                            LOGGER.info("Skip owned movie: {}", recommendedOutputMovie);
                            continue;
                        }

                        if (recommended.add(recommendedOutputMovie)) {
                            // Write current list of recommended movies to file.
                            outputIo.writeRss(tmdbOutputFileConfig, new HashSet<>(recommended));

                            LOGGER.info("/newMovieFound:{}", recommendedOutputMovie);

                            //Send message over websocket
                            SearchResults searchResults = new SearchResults(searchedMovieCount.get(), ownedInputMovies.size(), recommendedOutputMovie);
                            template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }

                } else {
                    sendEmptySearchUpdate(ownedInputMovies.size(), searchedMovieCount);
                }
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error getting collections %s.", inputMovie), e);
        }

        searched.add(inputMovie);
    }

    private void sendEmptySearchUpdate(int totalMovieCount, AtomicInteger searchedMovieCount) throws JsonProcessingException {
        //Send message over websocket
        //No new movie, just updated counts
        SearchResults searchResults = new SearchResults(searchedMovieCount.get(), totalMovieCount, null);
        template.convertAndSend("/newMovieFound", objectMapper.writeValueAsString(searchResults));
    }

    @Override
    public @NotNull Boolean writeRss(@NotNull Set<OutputMovie> recommended) {
        return outputIo.writeRss(tmdbOutputFileConfig, recommended);
    }
}
