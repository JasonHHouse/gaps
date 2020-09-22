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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.jasonhhouse.gaps.MovieFromCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@JsonDeserialize(builder = TmdbMovie.Builder.class)
public final class TmdbMovie extends OutputMovie {

    private TmdbMovie(@NotNull String name,
                      @NotNull Integer year,
                      @NotNull String posterUrl,
                      @NotNull String collectionTitle,
                      @NotNull Integer collectionId,
                      @NotNull Integer tmdbId,
                      @NotNull String imdbId,
                      @NotNull String language,
                      @NotNull String overview,
                      @NotNull List<MovieFromCollection> movieFromCollections) {
        super(name, year, posterUrl, collectionTitle, collectionId, tmdbId, imdbId, language, overview, movieFromCollections);
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder extends GapsMovie.Builder<TmdbMovie> {

        @NotNull
        @JsonProperty
        private List<MovieFromCollection> movieFromCollections;

        public Builder(@NotNull String name, @NotNull Integer year) {
            super(name, year);
            movieFromCollections = Collections.emptyList();
        }

        @Override
        public @NotNull TmdbMovie build() {
            return new TmdbMovie(name, year, posterUrl, collectionTitle, collectionId, tmdbId, imdbId, language, overview, movieFromCollections);
        }

        public @NotNull Builder setMoviesFromCollections(@NotNull List<MovieFromCollection> movieFromCollections) {
            this.movieFromCollections = movieFromCollections;
            return this;
        }
    }
}
