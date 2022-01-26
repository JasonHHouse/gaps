package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class MovieStatusPayload {
    private final Integer status;

    @JsonCreator
    public MovieStatusPayload(@JsonProperty("status") Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "MovieStatusPayload{" +
                "status=" + status +
                '}';
    }
}
