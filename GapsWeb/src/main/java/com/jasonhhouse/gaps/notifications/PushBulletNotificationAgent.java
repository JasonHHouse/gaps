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
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushBulletNotificationAgent implements NotificationAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushBulletNotificationAgent.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String channel_tag;
    private final String accessToken;
    private final OkHttpClient client;
    private final IoService ioService;

    public PushBulletNotificationAgent(IoService ioService) {
        this.ioService = ioService;
        this.channel_tag = "gaps";
        this.accessToken = "o.iv0WmtnLWI3xfhTSNhuuQvNmovUFHszt";

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }


    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getName() {
        return "PushBullet Notification Agent";
    }

    @Override
    public boolean isEnabled() {
        //ToDo Check IoService
        return true;
    }

    @Override
    public boolean sendMessage(NotificationType notificationType, String level, String title, String message) {
        LOGGER.info("sendMessage( {}, {}, {} )", level, title, message);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.pushbullet.com")
                .addPathSegment("v2")
                .addPathSegment("pushes")
                .build();

        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .add("Access-Token", accessToken)
                .build();

        PushBullet pushBullet = new PushBullet();
        pushBullet.setBody(message);
        pushBullet.setTitle(title);
        pushBullet.setChannel_tag(channel_tag);

        String pushBulletMessage = "";
        try {
            pushBulletMessage = objectMapper.writeValueAsString(pushBullet);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to turn PushBullet message into JSON", e);
            return false;
        }

        LOGGER.info("pushBulletMessage {}", pushBulletMessage);
        RequestBody body = RequestBody.create(pushBulletMessage, MediaType.get("application/json"));

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
                LOGGER.error("Error with PushBullet Url: {} Body returned {}", url, response.body().toString());
                return false;
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with PushBullet Url: %s", url), e);
            return false;
        }
    }

    public static final class PushBullet {
        private final String type;
        private String channel_tag;
        private String title;
        private String body;

        public PushBullet() {
            type = "note";
        }

        public String getChannel_tag() {
            return channel_tag;
        }

        public void setChannel_tag(String channel_tag) {
            this.channel_tag = channel_tag;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
