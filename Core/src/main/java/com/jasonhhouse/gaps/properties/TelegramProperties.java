/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonhhouse.gaps.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TelegramProperties extends AbstractNotificationProperties {

    @NotNull
    @Schema(required = true, description = "The UID of your Telegram Bot. Ask BotFather for help about bots.")
    private final String botId;
    @NotNull
    @Schema(required = true, description = "The UID of your own chats. Ask IDBot, /getid")
    private final String chatId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TelegramProperties(@JsonProperty(value = "enabled", required = true) @NotNull Boolean enabled,
                              @JsonProperty(value = "notificationTypes", required = true) @NotNull List<NotificationType> notificationTypes,
                              @JsonProperty(value = "botId") @Nullable String botId,
                              @JsonProperty(value = "chatId") @Nullable String chatId) {
        super(enabled, notificationTypes);

        this.botId = botId == null ? "" : botId;
        this.chatId = chatId == null ? "" : chatId;
    }

    static TelegramProperties getDefault() {
        return new TelegramProperties(false, Collections.emptyList(), "", "");
    }

    @NotNull
    public String getBotId() {
        return botId;
    }

    @NotNull
    public String getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "TelegramProperties{" +
                "botId='" + botId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", enabled='" + enabled + '\'' +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
