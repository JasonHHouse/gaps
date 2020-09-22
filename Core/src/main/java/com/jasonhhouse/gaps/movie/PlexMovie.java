/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@JsonDeserialize(builder = PlexMovie.Builder.class)
public final class PlexMovie extends InputMovie {

    @NotNull
    private final Integer ratingKey;
    @NotNull
    private final String key;

    private PlexMovie(@NotNull String name,
                      @NotNull Integer year,
                      @NotNull String posterUrl,
                      @NotNull String collectionTitle,
                      @NotNull Integer collectionId,
                      @NotNull Integer tmdbId,
                      @NotNull String imdbId,
                      @NotNull String language,
                      @NotNull String overview,
                      @NotNull Integer ratingKey,
                      @NotNull String key) {
        super(name, year, posterUrl, collectionTitle, collectionId, tmdbId, imdbId, language, overview);
        this.ratingKey = ratingKey;
        this.key = key;
    }

    public @NotNull Integer getRatingKey() {
        return ratingKey;
    }

    public @NotNull String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "PlexMovie{" +
                "ratingKey=" + ratingKey +
                ", key='" + key + '\'' +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder extends InputMovie.Builder<PlexMovie> {

        @NotNull
        @JsonProperty
        private Integer ratingKey;

        @NotNull
        @JsonProperty
        private String key;

        @JsonCreator
        public Builder(@JsonProperty(value = "name") @NotNull String name,
                       @JsonProperty(value = "year") @NotNull Integer year) {
            super(name, year);
            this.ratingKey = -1;
            this.key = "";
        }

        public @NotNull PlexMovie build() {
            return new PlexMovie(name, year, posterUrl, collectionTitle, collectionId, tmdbId, imdbId, language, overview, ratingKey, key);
        }

        public @NotNull Builder setRatingKey(@NotNull Integer ratingKey) {
            this.ratingKey = ratingKey;
            return this;
        }

        public @NotNull Builder setKey(@NotNull String key) {
            this.key = key;
            return this;
        }
    }
}
