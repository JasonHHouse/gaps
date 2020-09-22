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
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class GapsMovie {

    @NotNull
    private final String name;
    @NotNull
    private final Integer year;
    @NotNull
    private final String nameWithoutBadCharacters;
    @NotNull
    private final String posterUrl;
    @NotNull
    private final String language;
    @NotNull
    private final String overview;
    @NotNull
    private String imdbId;
    @NotNull
    private String collectionTitle;
    @NotNull
    private Integer collectionId;
    @NotNull
    private Integer tmdbId;

    protected GapsMovie(@NotNull String name,
                         @NotNull Integer year,
                         @NotNull String posterUrl,
                         @NotNull String collectionTitle,
                         @NotNull Integer collectionId,
                         @NotNull Integer tmdbId,
                         @NotNull String imdbId,
                         @NotNull String language,
                         @NotNull String overview) {
        this.name = name;
        this.nameWithoutBadCharacters = name.replaceAll("[<>`~\\[\\]()*&^%$#@!|{}.,?\\-_=+:;]", "");
        this.year = year;
        this.posterUrl = posterUrl;
        this.collectionTitle = collectionTitle;
        this.collectionId = collectionId;
        this.tmdbId = tmdbId;
        this.imdbId = imdbId;
        this.language = language;
        this.overview = overview;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Integer getYear() {
        return year;
    }

    public @NotNull String getNameWithoutBadCharacters() {
        return nameWithoutBadCharacters;
    }

    public @NotNull String getPosterUrl() {
        return posterUrl;
    }

    public @NotNull String getImdbId() {
        return imdbId;
    }

    public void setImdbId(@NotNull String imdbId) {
        this.imdbId = imdbId;
    }

    public @NotNull String getLanguage() {
        return language;
    }

    public @NotNull String getOverview() {
        return overview;
    }

    public @NotNull String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(@NotNull String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public @NotNull Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NotNull Integer collectionId) {
        this.collectionId = collectionId;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(@NotNull Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public String toString() {
        return "InputMovie{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", nameWithoutBadCharacters='" + nameWithoutBadCharacters + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", language='" + language + '\'' +
                ", overview='" + overview + '\'' +
                ", collectionTitle='" + collectionTitle + '\'' +
                ", collectionId=" + collectionId +
                ", tmdbId=" + tmdbId +
                '}';
    }

    public int compareTo(PlexMovie o) {
        return getNameWithoutBadCharacters().compareTo(o.getNameWithoutBadCharacters());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GapsMovie gapsMovie = (GapsMovie) o;

        //Compare tvdb id first
        if (getTmdbId() != -1 && getTmdbId().equals(gapsMovie.getTmdbId())) {
            return true;
        }

        //Compare imdb id next
        if (StringUtils.isNotEmpty(getImdbId()) && getImdbId().equals(gapsMovie.getImdbId())) {
            return true;
        }

        //Fallback is year and title
        return getYear().equals(gapsMovie.getYear()) && getNameWithoutBadCharacters().equals(gapsMovie.getNameWithoutBadCharacters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNameWithoutBadCharacters(), getYear());
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static abstract class Builder<T extends GapsMovie> {

        @NotNull
        protected String name;

        @NotNull
        protected Integer year;

        @NotNull
        @JsonProperty
        protected String posterUrl;

        @NotNull
        @JsonProperty
        protected String collectionTitle;

        @NotNull
        @JsonProperty
        protected Integer collectionId;

        @NotNull
        @JsonProperty
        protected Integer tmdbId;

        @NotNull
        @JsonProperty
        protected String imdbId;

        @NotNull
        @JsonProperty
        protected String language;

        @NotNull
        @JsonProperty
        protected String overview;

        @JsonCreator
        public Builder(@JsonProperty(value = "name") @NotNull String name,
                       @JsonProperty(value = "year") @NotNull Integer year) {
            this.name = name;
            this.year = year;
            this.tmdbId = -1;
            this.imdbId = "";
            this.collectionTitle = "";
            this.posterUrl = "";
            this.collectionId = -1;
            this.language = "en";
            this.overview = "";
        }

        public abstract @NotNull T build();

        public @NotNull Builder<T> setPosterUrl(@NotNull String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public @NotNull Builder<T> setCollectionTitle(@NotNull String collectionTitle) {
            this.collectionTitle = collectionTitle;
            return this;
        }

        public @NotNull Builder<T> setCollectionId(@NotNull Integer collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public @NotNull Builder<T> setTmdbId(@NotNull Integer tmdbId) {
            this.tmdbId = tmdbId;
            return this;
        }

        public @NotNull Builder<T> setImdbId(@NotNull String imdbId) {
            this.imdbId = imdbId;
            return this;
        }

        public @NotNull Builder<T> setLanguage(@NotNull String language) {
            this.language = language;
            return this;
        }

        public @NotNull Builder<T> setOverview(@NotNull String overview) {
            this.overview = overview;
            return this;
        }
    }

}
