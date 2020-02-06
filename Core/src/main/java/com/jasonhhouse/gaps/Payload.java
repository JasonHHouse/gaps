package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Payload {
    OWNED_MOVIES_CANNOT_BE_EMPTY(0, "Owned movies list cannot be empty. You must search first."),
    SEARCH_CANCELLED(1, "Search cancelled."),
    SEARCH_FAILED(2, "Search failed. Check docker Gaps log file."),
    SEARCH_SUCCESSFUL(3, "Search successful.");

    final private int code;
    final private String reason;

    Payload(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
