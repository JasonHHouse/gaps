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
import com.jasonhhouse.gaps.properties.SlackProperties;
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

import static com.jasonhhouse.gaps.notifications.NotificationStatus.TIMEOUT;

public final class SlackNotificationAgent extends AbstractNotificationAgent<SlackProperties> {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationAgent.class);
    @NotNull
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @NotNull
    private final OkHttpClient client;

    public SlackNotificationAgent(@NotNull IO ioService) {
        super(ioService);

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public @NotNull Integer getId() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Slack Notification Agent";
    }

    @Override
    public @NotNull Boolean sendMessage(@NotNull NotificationType notificationType, @NotNull String level, @NotNull String title, @NotNull String message) {
        LOGGER.info("sendMessage( {}, {}, {} )", level, title, message);

        if (sendPrepMessage(notificationType)) {
            return false;
        }

        HttpUrl url = HttpUrl.get(t.getWebHookUrl());

        Headers headers = new Headers.Builder()
                .add("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();

        Slack slack = new Slack(String.format("*%s*%n%s", title, message));

        String slackMessage = "";
        try {
            slackMessage = objectMapper.writeValueAsString(slack);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to turn Slack message into JSON", e);
            return false;
        }

        LOGGER.info("Sending slackMessage");
        RequestBody body = RequestBody.create(slackMessage, MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("Slack message sent via {}", url);
                return true;
            } else {
                LOGGER.error("Error with Slack Url: {} Body returned {}", url, response.body());
                return false;
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with Slack Url: %s", url), e);
            return false;
        }
    }

    @Override
    public @NotNull SlackProperties getNotificationProperties() {
        return ioService.readProperties().getSlackProperties();
    }

    private static final class Slack {
        @NotNull
        private final Block[] blocks;

        private Slack(@NotNull String message) {
            blocks = new Block[1];
            blocks[0] = new SlackNotificationAgent.Block(new Text(message));
        }

        public @NotNull Block[] getBlocks() {
            return blocks;
        }
    }

    private static final class Block {
        @NotNull
        private final String type;
        @NotNull
        private final Text text;

        private Block(@NotNull Text text) {
            this.type = "section";
            this.text = text;
        }

        public @NotNull String getType() {
            return type;
        }

        public @NotNull Text getText() {
            return text;
        }
    }

    private static final class Text {
        @NotNull
        private final String type;
        @NotNull
        private final String value;

        private Text(@NotNull String value) {
            this.type = "mrkdwn";
            this.value = value;
        }

        public @NotNull String getType() {
            return type;
        }

        @JsonProperty("text")
        public @NotNull String getValue() {
            return value;
        }
    }
}
