/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonhhouse.gaps.NotificationType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class PushOverProperties extends AbstractNotificationProperties {

    @NotNull
    private final String token;
    @NotNull
    private final String user;
    @NotNull
    private final Integer priority;
    @NotNull
    private final String sound;
    @NotNull
    private final Integer retry;
    @NotNull
    private final Integer expire;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PushOverProperties(@JsonProperty(value = "enabled", required = true) @NotNull Boolean enabled,
                              @JsonProperty(value = "notificationTypes", required = true) @NotNull List<NotificationType> notificationTypes,
                              @JsonProperty(value = "token", required = true) @NotNull String token,
                              @JsonProperty(value = "user", required = true) @NotNull String user,
                              @JsonProperty(value = "priority", required = true) @NotNull Integer priority,
                              @JsonProperty(value = "sound", required = true) @NotNull String sound,
                              @JsonProperty(value = "retry", required = true) @NotNull Integer retry,
                              @JsonProperty(value = "expire", required = true) @NotNull Integer expire) {
        super(enabled, notificationTypes);
        this.token = token;
        this.user = user;
        this.priority = priority;
        this.sound = sound;
        this.retry = retry;
        this.expire = expire;
    }

    public @NotNull String getToken() {
        return token;
    }

    public @NotNull String getUser() {
        return user;
    }

    public @NotNull Integer getPriority() {
        return priority;
    }

    public @NotNull String getSound() {
        return sound;
    }

    @NotNull
    public Integer getRetry() {
        return retry;
    }

    @NotNull
    public Integer getExpire() {
        return expire;
    }

    @Override
    public String toString() {
        return "PushOverProperties{" +
                "token='" + token + '\'' +
                ", user='" + user + '\'' +
                ", priority=" + priority +
                ", sound='" + sound + '\'' +
                ", retry=" + retry +
                ", expire=" + expire +
                ", enabled=" + enabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
