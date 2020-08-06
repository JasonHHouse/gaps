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

import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegramNotificationAgent implements NotificationAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationAgent.class);

    private final String botId;
    private final String chatId;
    private final OkHttpClient client;
    private final IoService ioService;

    public TelegramNotificationAgent(IoService ioService) {
        this.ioService = ioService;

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        botId = "1302139119:AAEuphtEuopXaTpitiGlWCY-3l33SBkPe2o"; //Get from IoService
        chatId = "1041081317"; //Get from IoService
    }

    @Override
    public boolean isEnabled() {
        //ToDo Check IoService
        return true;
    }

    @Override
    public void sendMessage(String level, String title, String message) {
        LOGGER.info("sendMessage( {}, {}, {} )", level, title, message);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.telegram.org")
                .addPathSegment(String.format("bot%s", botId))
                .addPathSegment("sendMessage")
                .build();

        String telegramMessage = String.format("{\"chat_id\":\"%s\", \"text\":\"%s\", \"parse_mode\":\"HTML\"}", chatId, String.format("<strong>%s</strong>\n%s", title, message));
        LOGGER.info("telegramMessage {}", telegramMessage);
        RequestBody body = RequestBody.create(telegramMessage, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("Telegram message sent via {}", url);
            } else {
                LOGGER.error("Error with Telegram Url: {} Body returned {}", url, response.body().toString());
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with Telegram Url: %s", url), e);
        }
    }
}
