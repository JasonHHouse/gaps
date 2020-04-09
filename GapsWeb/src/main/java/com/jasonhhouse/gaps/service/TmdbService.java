/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.Payload;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TmdbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbService.class);

    public @NotNull Payload testTmdbKey(String key) {
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
            ResponseBody responseBody = response.body();
            if(responseBody == null) {
                LOGGER.warn("Empty response body");
                return Payload.TMDB_KEY_INVALID.setExtras(key);
            }
            String jsonBody = responseBody.string();

            LOGGER.info("jsonBody: " + jsonBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(jsonBody);
            boolean success = responseJson.get("success").asBoolean();
            Payload payload;
            if (success) {
                payload = Payload.TMDB_KEY_VALID.setExtras(key);
            } else {
                LOGGER.warn("TMDB Key invalid " + key);
                payload = Payload.TMDB_KEY_INVALID.setExtras(key);
            }

            return payload;
        } catch (IOException e) {
            LOGGER.error("Error connecting to TMDB with url " + url);
            return Payload.TMDB_KEY_VALID.setExtras(key + System.lineSeparator() + url);
        }
    }
}
