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
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PushBulletProperties extends AbstractNotificationProperties {
    @NotNull
    private final String channel_tag;

    @NotNull
    private final String accessToken;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PushBulletProperties(@JsonProperty(value = "enabled", required = true) @NotNull Boolean enabled,
                                @JsonProperty(value = "notificationTypes", required = true) @NotNull List<NotificationType> notificationTypes,
                                @JsonProperty(value = "channel_tag") @Nullable String channel_tag,
                                @JsonProperty(value = "accessToken") @Nullable String accessToken) {
        super(enabled, notificationTypes);
        this.channel_tag = channel_tag == null ? "" : channel_tag;
        this.accessToken = accessToken == null ? "" : accessToken;
    }

    static PushBulletProperties getDefault() {
        return new PushBulletProperties(false, Collections.emptyList(), "", "");
    }

    @NotNull
    public String getChannel_tag() {
        return channel_tag;
    }

    @NotNull
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "PushBulletProperties{" +
                "channel_tag='" + channel_tag + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", enabled=" + enabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
