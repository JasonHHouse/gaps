/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.notifications;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.properties.NotificationProperties;
import com.jasonhhouse.gaps.service.IoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jasonhhouse.gaps.notifications.NotificationStatus.AGENT_NOT_ENABLED_FOR_NOTIFICATION_TYPE;

public abstract class AbstractNotificationAgent<T extends NotificationProperties> implements NotificationAgent<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotificationAgent.class);

    protected final IoService ioService;
    protected T t;

    protected AbstractNotificationAgent(IoService ioService) {
        this.ioService = ioService;
    }

    @Override
    public boolean isEnabled() {
        NotificationProperties notificationProperties = getNotificationProperties();

        if (notificationProperties == null) {
            return false;
        } else {
            return notificationProperties.getEnabled();
        }
    }

    protected boolean sendPrepMessage(NotificationType notificationType) {
        LOGGER.info("sendPrepMessage()");

        t = getNotificationProperties();

        if (t == null) {
            return false;
        }

        if (!t.getNotificationTypes().contains(notificationType)) {
            LOGGER.info(AGENT_NOT_ENABLED_FOR_NOTIFICATION_TYPE, getName(), notificationType);
            return false;
        }

        return true;
    }
}