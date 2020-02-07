package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    TMDB_KEY_VALID(20, "TMDB Key valid."),
    TMDB_KEY_INVALID(21, "TMDB Key invalid"),
    TMDB_CONNECTION_ERROR(22, "Error connecting to TMDB with url"),
    TMDB_KEY_SAVE_SUCCESSFUL(23, "TMDB key saved."),
    TMDB_KEY_SAVE_UNSUCCESSFUL(24, "TMDB key not saved.");

    final private int code;
    final private String reason;
    private String extras;

    Payload(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public String getExtras() {
        return extras;
    }

    public Payload setExtras(String extras) {
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
