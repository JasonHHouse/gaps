package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TmdbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbService.class);

    public @NotNull String testTmdbKey(String key) {
        LOGGER.info("testTmdbKey( " + key + " )");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("authentication")
                .addPathSegment("token")
                .addPathSegment("new")
                .addQueryParameter("api_key", key)
                .build();

        LOGGER.info("url: " + url);

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonBody = response.body().string();

            LOGGER.info("jsonBody: " + jsonBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(jsonBody);
            boolean success = responseJson.get("success").asBoolean();
            if (success) {
                LOGGER.info("TMDB Key valid " + key);
            } else {
                LOGGER.warn("TMDB Key invalid " + key);
            }

            return jsonBody;
        } catch (IOException e) {
            LOGGER.error("Error connecting to TMDB with url " + url);
            return "{}";
        }
    }
}
