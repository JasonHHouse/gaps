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
public final class DiscordProperties extends AbstractNotificationProperties {

    @NotNull
    @Schema(required = true, description = "The full URL of your webhook to the Discord API")
    private final String webHookUrl;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DiscordProperties(@JsonProperty(value = "enabled", required = true) @NotNull Boolean enabled,
                             @JsonProperty(value = "notificationTypes", required = true) @NotNull List<NotificationType> notificationTypes,
                             @JsonProperty(value = "webHookUrl") @Nullable String webHookUrl) {
        super(enabled, notificationTypes);
        this.webHookUrl = webHookUrl == null ? "" : webHookUrl;
    }

    static DiscordProperties getDefault() {
        return new DiscordProperties(false, Collections.emptyList(), "");
    }

    @Override
    public String toString() {
        return "DiscordProperties{" +
                "webHookUrl='" + webHookUrl + '\'' +
                '}';
    }

    @NotNull
    public String getWebHookUrl() {
        return webHookUrl;
    }
}
