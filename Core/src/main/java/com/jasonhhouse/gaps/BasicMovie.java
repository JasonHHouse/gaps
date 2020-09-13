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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jasonhhouse.gaps.json.MovieDeserializer;
import com.jasonhhouse.gaps.json.MovieSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerialize(using = MovieSerializer.class)
@JsonDeserialize(using = MovieDeserializer.class)
public final class BasicMovie implements Comparable<BasicMovie>, Video {

    public static final String TVDB_ID = "tvdbId";

    public static final String IMDB_ID = "imdbId";

    public static final String NAME = "name";

    public static final String YEAR = "year";

    public static final String POSTER = "poster_url";

    public static final String COLLECTION_ID = "collectionId";

    public static final String COLLECTION = "collection";

    public static final String LANGUAGE = "language";

    public static final String OVERVIEW = "overview";

    public static final String MOVIES_IN_COLLECTION = "movies_in_collection";

    @NotNull
    private final String name;

    @NotNull
    private final Integer year;

    @NotNull
    private final String nameWithoutBadCharacters;

    @NotNull
    @JsonProperty("poster_url")
    private final String posterUrl;
    @NotNull
    private String imdbId;
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
    private String collection;
    @NotNull
    private Integer collectionId;
    @NotNull
    private Integer tmdbId;

    private BasicMovie(@NotNull String name,
                       @NotNull Integer year,
                       @NotNull String posterUrl,
                       @NotNull String collection,
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
        this.collection = collection;
        this.collectionId = collectionId;
        this.tmdbId = tmdbId;
        this.imdbId = imdbId;
        this.language = language;
        this.overview = overview;
        this.moviesInCollection = moviesInCollection;
        this.ratingKey = ratingKey;
        this.key = key;
    }

    @Override
    public @NotNull Integer getCollectionId() {
        return collectionId;
    }

    @Override
    public void setCollectionId(@NotNull Integer collectionId) {
        this.collectionId = collectionId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public @NotNull String getTitle() {
        return name;
    }

    @NotNull
    public Integer getYear() {
        return year;
    }

    @Override
    public @NotNull String getCollectionTitle() {
        return collection;
    }

    @Override
    public void setCollectionTitle(@NotNull String collectionTitle) {
        this.collection = collectionTitle;
    }

    public void setCollection(@NotNull String collection) {
        this.collection = collection;
    }

    @NotNull
    public String getImdbId() {
        return imdbId;
    }

    @Override
    public void setImdbId(@NotNull String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    @Override
    public void setTmdbId(@NotNull Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public @NotNull String getLanguage() {
        return language;
    }

    public @NotNull String getOverview() {
        return overview;
    }

    @NotNull
    public List<MovieFromCollection> getMoviesInCollection() {
        return moviesInCollection;
    }

    @Override
    public @NotNull String getTitleWithoutBadCharacters() {
        return nameWithoutBadCharacters;
    }

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

        //Compare tvdb id first
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

    @Nullable
    public String getPosterUrl() {
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
                ", collection='" + collection + '\'' +
                ", collectionId=" + collectionId +
                ", tvdbId=" + tmdbId +
                ", ratingKey=" + ratingKey +
                ", key='" + key + '\'' +
                '}';
    }

    public int compareTo(BasicMovie o) {
        return getNameWithoutBadCharacters().compareTo(o.getNameWithoutBadCharacters());
    }

    public static class Builder {

        @NotNull
        private final String name;

        private final int year;

        @NotNull
        private String posterUrl;

        @NotNull
        private String collection;

        private int collectionId;

        private int tvdbId;

        @NotNull
        private String imdbId;

        @NotNull
        private String language;

        @NotNull
        private String overview;

        @NotNull
        private List<MovieFromCollection> moviesInCollection;

        @NotNull
        private Integer ratingKey;

        @NotNull
        private String key;

        public Builder(@NotNull String name, @NotNull Integer year) {
            this.name = name;
            this.year = year;
            this.tvdbId = -1;
            this.imdbId = "";
            this.collection = "";
            this.posterUrl = "";
            this.collectionId = -1;
            this.language = "en";
            this.overview = "";
            this.moviesInCollection = new ArrayList<>();
            this.ratingKey = -1;
            this.key = "";
        }

        public BasicMovie build() {
            return new BasicMovie(name, year, posterUrl, collection, collectionId, tvdbId, imdbId, language, overview, moviesInCollection, ratingKey, key);
        }

        @NotNull
        public Builder setPosterUrl(@NotNull String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        @NotNull
        public Builder setCollection(@NotNull String collection) {
            this.collection = collection;
            return this;
        }

        @NotNull
        public Builder setCollectionId(int collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        @NotNull
        public Builder setTvdbId(int tvdbId) {
            this.tvdbId = tvdbId;
            return this;
        }

        @NotNull
        public Builder setImdbId(@NotNull String imdbId) {
            this.imdbId = imdbId;
            return this;
        }

        @NotNull
        public Builder setLanguage(@NotNull String language) {
            this.language = language;
            return this;
        }

        @NotNull
        public Builder setOverview(@NotNull String overview) {
            this.overview = overview;
            return this;
        }

        @NotNull
        public Builder setMoviesInCollection(@NotNull List<MovieFromCollection> moviesInCollection) {
            this.moviesInCollection = moviesInCollection;
            return this;
        }

        @NotNull
        public Builder setRatingKey(@NotNull Integer ratingKey) {
            this.ratingKey = ratingKey;
            return this;
        }

        @NotNull
        public Builder setKey(@NotNull String key) {
            this.key = key;
            return this;
        }
    }
}
