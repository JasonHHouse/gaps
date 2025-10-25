/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.properties.PushBulletProperties;
import com.jasonhhouse.gaps.service.IO;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jasonhhouse.gaps.notifications.NotificationStatus.FAILED_TO_PARSE_JSON;
import static com.jasonhhouse.gaps.notifications.NotificationStatus.SEND_MESSAGE;
import static com.jasonhhouse.gaps.notifications.NotificationStatus.TIMEOUT;

public final class PushBulletNotificationAgent extends AbstractNotificationAgent<PushBulletProperties> {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PushBulletNotificationAgent.class);

    @NotNull
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @NotNull
    private final OkHttpClient client;

    public PushBulletNotificationAgent(@NotNull IO ioService) {
        super(ioService);

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public @NotNull Integer getId() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "PushBullet Notification Agent";
    }

    @Override
    public @NotNull Boolean sendMessage(@NotNull NotificationType notificationType, @NotNull String level, @NotNull String title, @NotNull String message) {
        LOGGER.info(SEND_MESSAGE, level, title, message);

        if (sendPrepMessage(notificationType)) {
            return false;
        }

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.pushbullet.com")
                .addPathSegment("v2")
                .addPathSegment("pushes")
                .build();

        Headers headers = new Headers.Builder()
                .add("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .add("Access-Token", t.getAccessToken())
                .build();

        PushBullet pushBullet = new PushBullet(t.getChannelTag(), title, message);

        String pushBulletMessage = "";
        try {
            pushBulletMessage = objectMapper.writeValueAsString(pushBullet);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format(FAILED_TO_PARSE_JSON, getName()), e);
            return false;
        }

        LOGGER.info("Sending pushBulletMessage");
        RequestBody body = RequestBody.create(pushBulletMessage, MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("PushBullet message sent via {}", url);
                return true;
            } else {
                LOGGER.error("Error with PushBullet Url: {} Body returned {}", url, response.body());
                return false;
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with PushBullet Url: %s", url), e);
            return false;
        }
    }

    @Override
    public @NotNull PushBulletProperties getNotificationProperties() {
        return ioService.readProperties().getPushBulletProperties();
    }

    public static final class PushBullet {
        @NotNull
        private final String type;
        @NotNull
        private final String channelTag;
        @NotNull
        private final String title;
        @NotNull
        private final String body;

        public PushBullet(@NotNull String channelTag, @NotNull String title, @NotNull String body) {
            this.channelTag = channelTag;
            this.title = title;
            this.body = body;
            type = "note";
        }

        @JsonProperty("channel_tag")
        public @NotNull String getChannelTag() {
            return channelTag;
        }

        public @NotNull String getType() {
            return type;
        }

        public @NotNull String getTitle() {
            return title;
        }

        public @NotNull String getBody() {
            return body;
        }
    }
}
