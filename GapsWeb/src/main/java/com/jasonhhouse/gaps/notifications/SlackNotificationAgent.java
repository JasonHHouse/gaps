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

public class SlackNotificationAgent implements NotificationAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationAgent.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String webHookUrl;
    private final OkHttpClient client;
    private final IoService ioService;

    public SlackNotificationAgent(IoService ioService) {
        this.ioService = ioService;
        this.webHookUrl = "";

        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Slack Notification Agent";
    }

    @Override
    public boolean isEnabled() {
        //ToDo Check IoService
        return true;
    }

    @Override
    public void sendMessage(NotificationType notificationType, String level, String title, String message) {
        LOGGER.info("sendMessage( {}, {}, {} )", level, title, message);

        HttpUrl url = HttpUrl.get(webHookUrl);

        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .build();

        Slack slack = new Slack(String.format("*%s*\n%s", title, message));

        String slackMessage = "";
        try {
            slackMessage = objectMapper.writeValueAsString(slack);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to turn Slack message into JSON", e);
            return;
        }

        LOGGER.info("slackMessage {}", slackMessage);
        RequestBody body = RequestBody.create(slackMessage, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("Slack message sent via {}", url);
            } else {
                LOGGER.error("Error with Slack Url: {} Body returned {}", url, response.body().toString());
            }

        } catch (IOException e) {
            LOGGER.error(String.format("Error with Slack Url: %s", url), e);
        }
    }

    private static final class Slack {
        private final Block[] blocks;

        private Slack(String message) {
            blocks = new Block[1];
            blocks[0] = new SlackNotificationAgent.Block(new Text(message));
        }

        public Block[] getBlocks() {
            return blocks;
        }
    }

    private static final class Block {
        private final String type;
        private final Text text;

        private Block(Text text) {
            this.type = "section";
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public Text getText() {
            return text;
        }
    }

    private static final class Text {
        private final String type;
        private final String text;

        private Text(String text) {
            this.type = "mrkdwn";
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }
}
