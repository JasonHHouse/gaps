/*
 *
 *  Copyright 2025 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.radarr_v3.Movie;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RadarrV3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(RadarrV3Service.class);

    private static final long TIMEOUT = 2500;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public @NotNull List<Movie> getMovies(@NotNull String address, @NotNull Integer port, @NotNull String apiKey) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(address)
                .port(port)
                .addPathSegment("api")
                .addPathSegment("v3")
                .addPathSegment("movie")
                .addQueryParameter("apikey", apiKey)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
                    String reason = "Body returned empty from Plex";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
                }

                return objectMapper.readValue(body, new TypeReference<List<Movie>>() {
                });

            } catch (IOException e) {
                String reason = String.format("Error connecting to RadarrV3 to get movies: %s", url);
                LOGGER.error(reason, e);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with RadarrV3 Url: " + url;
            LOGGER.error(reason, e);
        }

        return Collections.emptyList();
    }
}
