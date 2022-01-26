package com.jasonhhouse.gaps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jasonhhouse.gaps.json.MovieStatusDeserializer;
import com.jasonhhouse.gaps.json.MovieStatusSerializer;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonSerialize(using = MovieStatusSerializer.class)
@JsonDeserialize(using = MovieStatusDeserializer.class)
public enum MovieStatus {
    ALL("All", 0),
    RELEASED("Released", 1);

    public static final String ID_LABEL = "id";
    public static final String MESSAGE_LABEL = "message";

    @NotNull
    private final String message;

    @NotNull
    private final Integer id;

    MovieStatus(@NotNull String message, @NotNull Integer id) {
        this.message = message;
        this.id = id;
    }

    public static MovieStatus getMovieStatus(@NotNull Integer id) {
        if (RELEASED.getId().equals(id)) {
            return RELEASED;
        } else {
            return ALL;
        }
    }

    public static @NotNull List<MovieStatus> getAllMovieStatuses() {
        return Arrays.asList(ALL, RELEASED);
    }

    public @NotNull String getMessage() {
        return message;
    }

    public @NotNull Integer getId() {
        return id;
    }

    @Override
    public @NotNull String toString() {
        return "MovieStatus{" +
                "message='" + message + '\'' +
                ", id=" + id +
                '}';
    }
}
