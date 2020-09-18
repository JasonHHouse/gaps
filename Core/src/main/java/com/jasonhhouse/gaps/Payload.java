package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Payload {
    UNKNOWN_ERROR(-1, "Unknown error."),
    SEARCH_SUCCESSFUL(0, "Search successful."),
    SEARCH_CANCELLED(1, "Search cancelled."),
    SEARCH_FAILED(2, "Search failed. Check docker Gaps log file."),
    OWNED_MOVIES_CANNOT_BE_EMPTY(3, "Owned movies list cannot be empty. You must search first."),
    PLEX_CONNECTION_SUCCEEDED(10, "Connection to Plex succeeded."),
    PLEX_CONNECTION_FAILED(11, "Connection to Plex failed."),
    PARSING_PLEX_FAILED(12, "Failed to parse Plex XML."),
    PLEX_URL_ERROR(13, "Error with plex Url"),
    PLEX_LIBRARIES_FOUND(13, "Plex Libraries found."),
    DUPLICATE_PLEX_LIBRARY(14, "Attempted to add duplicate Plex library"),
    TMDB_KEY_VALID(20, "TMDB Key valid."),
    TMDB_KEY_INVALID(21, "TMDB Key invalid"),
    TMDB_CONNECTION_ERROR(22, "Error connecting to TMDB with url"),
    TMDB_KEY_SAVE_SUCCESSFUL(23, "TMDB key saved."),
    TMDB_KEY_SAVE_UNSUCCESSFUL(24, "TMDB key not saved."),
    NUKE_SUCCESSFUL(30, "Nuke successful. All files deleted."),
    NUKE_UNSUCCESSFUL(31, "Nuke unsuccessful. Manually delete files."),
    PLEX_LIBRARY_MOVIE_FOUND(40, "Plex's library movies found."),
    PLEX_LIBRARY_MOVIE_NOT_FOUND(41, "Plex's library movies not found."),
    RECOMMENDED_MOVIES_FOUND(50, "Recommended movies found."),
    RECOMMENDED_MOVIES_NOT_FOUND(51, "Recommended movies not found."),
    SCHEDULE_FOUND(60, "Search schedule found."),
    SCHEDULE_NOT_FOUND(61, "Search schedule not found."),
    SCHEDULE_UPDATED(62, "Search schedule updated successfully."),
    SCHEDULE_NOT_UPDATED(63, "Search schedule update failed."),
    NOTIFICATION_TEST_SUCCEEDED(70, "Notification test succeeded."),
    NOTIFICATION_TEST_FAILED(71, "Notification test failed."),
    TELEGRAM_NOTIFICATION_UPDATE_SUCCEEDED(80, "Telegram Notification Update Succeeded."),
    TELEGRAM_NOTIFICATION_UPDATE_FAILED(81, "Telegram Notification Update Failed."),
    TELEGRAM_NOTIFICATION_FOUND(82, "Telegram Notification Found."),
    TELEGRAM_NOTIFICATION_NOT_FOUND(83, "Telegram Notification Not Found."),
    SLACK_NOTIFICATION_UPDATE_SUCCEEDED(90, "Slack Notification Update Succeeded."),
    SLACK_NOTIFICATION_UPDATE_FAILED(91, "Slack Notification Update Failed."),
    SLACK_NOTIFICATION_FOUND(92, "Slack Notification Found."),
    SLACK_NOTIFICATION_NOT_FOUND(93, "Slack Notification Not Found."),
    PUSH_BULLET_NOTIFICATION_UPDATE_SUCCEEDED(100, "PushBullet Notification Update Succeeded."),
    PUSH_BULLET_NOTIFICATION_UPDATE_FAILED(101, "PushBullet Notification Update Failed."),
    PUSH_BULLET_NOTIFICATION_FOUND(102, "PushBullet Notification Found."),
    PUSH_BULLET_NOTIFICATION_NOT_FOUND(103, "PushBullet Notification Not Found."),
    GOTIFY_NOTIFICATION_UPDATE_SUCCEEDED(110, "Gotify Notification Update Succeeded."),
    GOTIFY_NOTIFICATION_UPDATE_FAILED(111, "Gotify Notification Update Failed."),
    GOTIFY_NOTIFICATION_FOUND(112, "Gotify Notification Found."),
    GOTIFY_NOTIFICATION_NOT_FOUND(113, "Gotify Notification Not Found."),
    EMAIL_NOTIFICATION_UPDATE_SUCCEEDED(120, "Email Notification Update Succeeded."),
    EMAIL_NOTIFICATION_UPDATE_FAILED(121, "Email Notification Update Failed."),
    EMAIL_NOTIFICATION_FOUND(122, "Email Notification Found."),
    EMAIL_NOTIFICATION_NOT_FOUND(123, "Email Notification Not Found."),
    PUSH_OVER_NOTIFICATION_UPDATE_SUCCEEDED(130, "PushOver Notification Update Succeeded."),
    PUSH_OVER_NOTIFICATION_UPDATE_FAILED(131, "PushOver Notification Update Failed."),
    PUSH_OVER_NOTIFICATION_FOUND(132, "PushOver Notification Found."),
    PUSH_OVER_NOTIFICATION_NOT_FOUND(133, "PushOver Notification Not Found."),
    DISCORD_NOTIFICATION_UPDATE_SUCCEEDED(140, "Discord Notification Update Succeeded."),
    DISCORD_NOTIFICATION_UPDATE_FAILED(141, "Discord Notification Update Failed."),
    DISCORD_NOTIFICATION_FOUND(142, "Discord Notification Found."),
    DISCORD_NOTIFICATION_NOT_FOUND(143, "Discord Notification Not Found.");

    private final int code;
    private final String reason;
    private Object extras;

    Payload(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public Object getExtras() {
        return extras;
    }

    @SuppressFBWarnings(
            value = "ME_ENUM_FIELD_SETTER",
            justification = "I know what I'm doing")
    public Payload setExtras(Object extras) {
        this.extras = extras;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
