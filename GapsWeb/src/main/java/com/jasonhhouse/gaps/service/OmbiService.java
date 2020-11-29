package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.properties.OmbiProperties;
import com.jasonhhouse.gaps.properties.PlexProperties;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OmbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmbiService.class);
    private static final long TIMEOUT = 5000;

    private final OmbiUrlGenerator ombiUrlGenerator;
    private final FileIoService fileIoService;
    private final ObjectMapper objectMapper;

    public OmbiService(OmbiUrlGenerator ombiUrlGenerator, FileIoService fileIoService) {
        this.ombiUrlGenerator = ombiUrlGenerator;
        this.fileIoService = fileIoService;
        objectMapper = new ObjectMapper();
    }

    public Payload testConnection(@NotNull OmbiProperties ombiProperties) {
        LOGGER.debug("testConnection()");

        HttpUrl url = ombiUrlGenerator.generateStatusUrl(ombiProperties);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .addHeader("ApiKey", ombiProperties.getApiKey())
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                LOGGER.info("Ombi response code: {}", response.code());

                if (response.code() == 200) {
                    return Payload.OMBI_TEST_CONNECTION_SUCCEEDED;
                } else {
                    return Payload.OMBI_TEST_CONNECTION_FAILED;
                }

            } catch (IOException e) {
                String reason = String.format("Error connecting to Ombi: %s", url);
                LOGGER.error(reason, e);
                return Payload.OMBI_TEST_CONNECTION_FAILED.setExtras("url:" + url);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with Ombi Url: " + url;
            LOGGER.error(reason, e);
            return Payload.OMBI_TEST_CONNECTION_FAILED.setExtras("url:" + url);
        }

    }

    public Payload sendMoviesToOmbi(@NotNull List<BasicMovie> recommendedMovies) {
        LOGGER.debug("sendMoviesToOmbi()");

        PlexProperties plexProperties = fileIoService.readProperties();
        OmbiProperties ombiProperties = plexProperties.getOmbiProperties();

        List<BasicMovie> alreadyRequestedMovies = getRequestedMovies(ombiProperties);

        recommendedMovies.removeAll(alreadyRequestedMovies);

        HttpUrl url = ombiUrlGenerator.generateMovieRequestsUrl(ombiProperties);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        for (BasicMovie basicMovie : recommendedMovies) {
            try {
                RequestMovie requestMovie = new RequestMovie(basicMovie.getTmdbId(), "en");

                RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsBytes(requestMovie), MediaType.parse("application/json"));

                Request request = new Request.Builder()
                        .addHeader("ApiKey", ombiProperties.getApiKey())
                        .post(requestBody)
                        .url(url)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    LOGGER.info("Ombi response code: {}", response.code());

                    String body = response.body() != null ? response.body().string() : null;

                    if (StringUtils.isBlank(body)) {
                        String reason = "Body returned empty from Ombi";
                        LOGGER.error(reason);
                        return Collections.emptyList();
                    }

                    List<BasicMovie> basicMovies = new ArrayList<>();

                    return basicMovies;
                } catch (IOException e) {
                    String reason = String.format("Error connecting to Ombi: %s", url);
                    LOGGER.error(reason, e);
                    return Collections.emptyList();
                }
            } catch (IllegalArgumentException e) {
                String reason = "Error with Ombi Url: " + url;
                LOGGER.error(reason, e);
                return Collections.emptyList();
            } catch (JsonProcessingException e) {
                String reason = "Error writing movie to JSON";
                LOGGER.error(reason, e);
                return Collections.emptyList();
            }
        }
    }

    private @NotNull List<BasicMovie> getRequestedMovies(@NotNull OmbiProperties ombiProperties) {

        HttpUrl url = ombiUrlGenerator.generateMovieRequestsUrl(ombiProperties);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .addHeader("ApiKey", ombiProperties.getApiKey())
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                LOGGER.info("Ombi response code: {}", response.code());

                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
                    String reason = "Body returned empty from Ombi";
                    LOGGER.error(reason);
                    return Collections.emptyList();
                }

                List<BasicMovie> basicMovies = new ArrayList<>();

                ArrayNode ombiRequestedMovies = (ArrayNode) objectMapper.readTree(body);
                for (JsonNode jsonNode : ombiRequestedMovies) {
                    String title = jsonNode.get("title").textValue();
                    LocalDateTime dateTime = LocalDateTime.parse(jsonNode.get("releaseDate").textValue());
                    Integer year = dateTime.getYear();
                    Integer tmdbId = jsonNode.get("theMovieDbId").intValue();
                    String imdbId = jsonNode.get("imdbId").textValue();
                    BasicMovie basicMovie = new BasicMovie.Builder(title, year)
                            .setImdbId(imdbId)
                            .setTmdbId(tmdbId)
                            .build();
                    basicMovies.add(basicMovie);
                }

                return basicMovies;
            } catch (IOException e) {
                String reason = String.format("Error connecting to Ombi: %s", url);
                LOGGER.error(reason, e);
                return Collections.emptyList();
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with Ombi Url: " + url;
            LOGGER.error(reason, e);
            return Collections.emptyList();
        }
    }

    private static final class RequestMovie {
        @NotNull
        private final Integer theMovieDbId;

        @NotNull
        private final String languageCode;

        private RequestMovie(@NotNull Integer theMovieDbId,
                             @NotNull String languageCode) {
            this.theMovieDbId = theMovieDbId;
            this.languageCode = languageCode;
        }

        public @NotNull Integer getTheMovieDbId() {
            return theMovieDbId;
        }

        public @NotNull String getLanguageCode() {
            return languageCode;
        }
    }
}
