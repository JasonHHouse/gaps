/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.properties.GotifyProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jasonhhouse.gaps.notifications.NotificationStatus.FAILED_TO_PARSE_JSON;
import static com.jasonhhouse.gaps.notifications.NotificationStatus.SEND_MESSAGE;
import static com.jasonhhouse.gaps.notifications.NotificationStatus.TIMEOUT;

public final class GotifyNotificationAgent extends AbstractNotificationAgent<GotifyProperties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GotifyNotificationAgent.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client;

    public GotifyNotificationAgent(FileIoService fileIoService) {
        super(fileIoService);

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getName() {
        return "Gotify Notification Agent";
    }

    @Override
    public boolean sendMessage(NotificationType notificationType, String level, String title, String message) {
        LOGGER.info(SEND_MESSAGE, level, title, message);

        if (sendPrepMessage(notificationType)) {
            return false;
        }

        HttpUrl url = HttpUrl.parse(String.format("%s/message?token=%s", t.getAddress(), t.getToken()));

        Gotify gotify = new Gotify(title, message);

        String gotifyMessage = "";
        try {
            gotifyMessage = objectMapper.writeValueAsString(gotify);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format(FAILED_TO_PARSE_JSON, getName()), e);
            return false;
        }

        LOGGER.info("Gotify {}", gotifyMessage);
        RequestBody body = RequestBody.create(gotifyMessage, MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("Gotify message sent via {}", url);
                return true;
            } else {
                LOGGER.error("Error with Gotify Url: {} Body returned {}", url, response.body());
                return false;
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with Gotify Url: %s", url), e);
            return false;
        }
    }

    @NotNull
    @Override
    public GotifyProperties getNotificationProperties() {
        return fileIoService.readProperties().getGotifyProperties();
    }

    public static final class Gotify {
        private final String title;
        private final String message;

        public Gotify(String title, String message) {
            this.message = message;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }
    }
}

