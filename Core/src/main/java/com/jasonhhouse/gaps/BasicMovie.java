/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Entity
@JsonDeserialize(builder = BasicMovie.Builder.class)
public final class BasicMovie implements Comparable<BasicMovie> {

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
    private final List<MovieFromCollection> moviesInCollection;
    @NotNull
    private final Integer ratingKey;
    @NotNull
    private final String key;
    @NotNull
    private String imdbId;
    @NotNull
    private String collectionTitle;
    @NotNull
    private Integer collectionId;
    @Id
    @NotNull
    private Integer tmdbId;

    private BasicMovie(@NotNull String name,
                       @NotNull Integer year,
                       @NotNull String posterUrl,
                       @NotNull String collectionTitle,
                       @NotNull Integer collectionId,
                       @NotNull Integer tmdbId,
                       @NotNull String imdbId,
                       @NotNull String language,
                       @NotNull String overview,
                       @NotNull List<MovieFromCollection> moviesInCollection,
                       @NotNull Integer ratingKey,
                       @NotNull String key) {
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
        this.moviesInCollection = moviesInCollection;
        this.ratingKey = ratingKey;
        this.key = key;
    }

    public @NotNull Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NotNull Integer collectionId) {
        this.collectionId = collectionId;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Integer getYear() {
        return year;
    }

    public @NotNull String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(@NotNull String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public @NotNull String getImdbId() {
        return imdbId;
    }

    public void setImdbId(@NotNull String imdbId) {
        this.imdbId = imdbId;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public void setTmdbId(@NotNull Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public @NotNull String getLanguage() {
        return language;
    }

    public @NotNull String getOverview() {
        return overview;
    }

    public @NotNull List<MovieFromCollection> getMoviesInCollection() {
        return moviesInCollection;
    }

    @JsonIgnore
    public @NotNull String getNameWithoutBadCharacters() {
        return nameWithoutBadCharacters;
    }

    public @NotNull Integer getRatingKey() {
        return ratingKey;
    }

    public @NotNull String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicMovie basicMovie = (BasicMovie) o;

        //Compare tmdb id first
        if (tmdbId != -1 && tmdbId.equals(basicMovie.tmdbId)) {
            return true;
        }

        //Compare imdb id next
        if (StringUtils.isNotEmpty(imdbId) && imdbId.equals(basicMovie.imdbId)) {
            return true;
        }

        //Fallback is year and title
        return year.equals(basicMovie.year) && nameWithoutBadCharacters.equals(basicMovie.nameWithoutBadCharacters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameWithoutBadCharacters, year);
    }

    public @NotNull String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", nameWithoutBadCharacters='" + nameWithoutBadCharacters + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", language='" + language + '\'' +
                ", overview='" + overview + '\'' +
                ", moviesInCollection=" + moviesInCollection +
                ", collection='" + collectionTitle + '\'' +
                ", collectionId=" + collectionId +
                ", tmdbId=" + tmdbId +
                ", ratingKey=" + ratingKey +
                ", key='" + key + '\'' +
                '}';
    }

    public int compareTo(BasicMovie o) {
        return getNameWithoutBadCharacters().compareTo(o.getNameWithoutBadCharacters());
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {

        @NotNull
        private final String name;

        @NotNull
        private final Integer year;

        @NotNull
        @JsonProperty
        private String posterUrl;

        @NotNull
        @JsonProperty
        private String collectionTitle;

        @NotNull
        @JsonProperty
        private Integer collectionId;

        @NotNull
        @JsonProperty
        private Integer tmdbId;

        @NotNull
        @JsonProperty
        private String imdbId;

        @NotNull
        @JsonProperty
        private String language;

        @NotNull
        @JsonProperty
        private String overview;

        @NotNull
        @JsonProperty
        private List<MovieFromCollection> moviesInCollection;

        @NotNull
        @JsonProperty
        private Integer ratingKey;

        @NotNull
        @JsonProperty
        private String key;

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
            this.moviesInCollection = new ArrayList<>();
            this.ratingKey = -1;
            this.key = "";
        }

        public @NotNull BasicMovie build() {
            return new BasicMovie(name, year, posterUrl, collectionTitle, collectionId, tmdbId, imdbId, language, overview, moviesInCollection, ratingKey, key);
        }

        public @NotNull Builder setPosterUrl(@NotNull String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public @NotNull Builder setCollectionTitle(@NotNull String collectionTitle) {
            this.collectionTitle = collectionTitle;
            return this;
        }

        public @NotNull Builder setCollectionId(@NotNull Integer collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public @NotNull Builder setTmdbId(@NotNull Integer tmdbId) {
            this.tmdbId = tmdbId;
            return this;
        }

        public @NotNull Builder setImdbId(@NotNull String imdbId) {
            this.imdbId = imdbId;
            return this;
        }

        public @NotNull Builder setLanguage(@NotNull String language) {
            this.language = language;
            return this;
        }

        public @NotNull Builder setOverview(@NotNull String overview) {
            this.overview = overview;
            return this;
        }

        public @NotNull Builder setMoviesInCollection(@NotNull List<MovieFromCollection> moviesInCollection) {
            this.moviesInCollection = moviesInCollection;
            return this;
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
