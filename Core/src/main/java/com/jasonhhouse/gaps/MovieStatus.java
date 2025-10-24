/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
