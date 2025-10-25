/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.notifications;

import org.jetbrains.annotations.NotNull;

public final class NotificationStatus {

    @NotNull
    public static final String FAILED_TO_READ_PROPERTIES = "Failed to read %s from Properties file.";

    @NotNull
    public static final String AGENT_NOT_ENABLED_FOR_NOTIFICATION_TYPE = "{} not enabled for notification type {}";

    @NotNull
    public static final String FAILED_TO_PARSE_JSON = "Failed to turn %s message into JSON";

    @NotNull
    public static final String SEND_MESSAGE = "sendMessage( {}, {}, {} )";

    @NotNull
    public static final Long TIMEOUT = 2500L;

    private NotificationStatus() {
        //No init
    }
}
