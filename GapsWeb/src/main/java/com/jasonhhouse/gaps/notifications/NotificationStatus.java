package com.jasonhhouse.gaps.notifications;

public final class NotificationStatus {

    public static final String FAILED_TO_READ_PROPERTIES = "Failed to read %s from Properties file.";

    public static final String AGENT_NOT_ENABLED_FOR_NOTIFICATION_TYPE = "{} not enabled for notification type {}";

    public static final String FAILED_TO_PARSE_JSON = "Failed to turn %s message into JSON";

    public static final String SEND_MESSAGE = "sendMessage( {}, {}, {} )";

    public static final long TIMEOUT = 2500;
}
