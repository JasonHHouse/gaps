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
