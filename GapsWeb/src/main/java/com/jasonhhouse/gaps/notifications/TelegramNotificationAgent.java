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
    private static final long TIMEOUT = 2500;
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
    public void sendMessage(String level, String message) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.telegram.org")
                .addPathSegment(String.format("bot%s", botId))
                .addPathSegment("sendMessage")
                .build();

        String telegramMessage = String.format("{\"chat_id\":\"%s\", \"text\":\"%s\", \"parse_mode\":\"HTML\"}", chatId, message);
        LOGGER.info("telegramMessage {}", telegramMessage);
        RequestBody body = RequestBody.create(telegramMessage, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.error("Telegram message sent via {}", url);
            } else {
                LOGGER.error("Error with telegram Url: {} Body returned {}", url, response.body().toString());
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with telegram Url: %s", url), e);
        }
    }
}
