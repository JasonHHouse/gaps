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

import com.jasonhhouse.gaps.NotificationType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNotificationProperties implements NotificationProperties {

    @NotNull
    protected final Boolean enabled;

    @NotNull
    protected final List<NotificationType> notificationTypes;

    public AbstractNotificationProperties(@NotNull Boolean enabled, @NotNull List<NotificationType> notificationTypes) {
        this.enabled = enabled;
        this.notificationTypes = notificationTypes;
    }

    @Override
    @NotNull
    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    @NotNull
    public List<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    @Override
    public String toString() {
        return "AbstractNotificationProperties{" +
                "enabled=" + enabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
