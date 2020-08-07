/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.notifications;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.properties.NotificationProperties;
import org.jetbrains.annotations.Nullable;

public interface NotificationAgent<T extends NotificationProperties> {

    String FAILED_TO_READ_PROPERTIES = "Failed to read %s from Properties file.";

    String AGENT_NOT_ENABLED_FOR_NOTIFICATION_TYPE = "{} not enabled for notification type {}";

    String FAILED_TO_PARSE_JSON = "Failed to turn %s message into JSON";

    String SEND_MESSAGE = "sendMessage( {}, {}, {} )";

    long TIMEOUT = 2500;

    int getId();

    String getName();

    boolean isEnabled();

    boolean sendMessage(NotificationType notificationType, String level, String title, String message);

    @Nullable
    T getNotificationProperties();
}
