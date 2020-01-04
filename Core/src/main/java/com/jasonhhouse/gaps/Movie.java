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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jasonhhouse.gaps.json.MovieDeserializer;
import com.jasonhhouse.gaps.json.MovieSerializer;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

@JsonSerialize(using = MovieSerializer.class)
@JsonDeserialize(using = MovieDeserializer.class)
public final class Movie implements Comparable<Movie> {

    public static final String TVDB_ID = "tvdbId";

    public static final String IMDB_ID = "imdbId";

    public static final String NAME = "name";

    public static final String YEAR = "year";

    public static final String POSTER = "poster_url";

    public static final String COLLECTION_ID = "collectionId";

    public static final String COLLECTION = "collection";

    private final String name;

    private final int year;

    @Nullable
    private String posterUrl;

    @Nullable
    private String collection;

    private int collectionId;

    private int tvdbId;

    @Nullable
    private String imdbId;

    private Movie(String name, int year, @Nullable String posterUrl, @Nullable String collection, int collectionId, int tvdbId, @Nullable String imdbId) {
        this.name = name;
        this.year = year;
        this.posterUrl = posterUrl;
        this.collection = collection;
        this.collectionId = collectionId;
        this.tvdbId = tvdbId;
        this.imdbId = imdbId;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(int tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    @Nullable
    public String getCollection() {
        return collection;
    }

    @Nullable
    public String getImdbId() {
        return imdbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year &&
                Objects.equals(name, movie.name);
    }

    @Nullable
    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
    }

    @Override
    public String toString() {
        return name + " (" + year + ")" + (StringUtils.isNotEmpty(collection) ? "  ---  collection='" + collection + '\'' : "");
    }

    public int compareTo(Movie o) {
        return getName().compareTo(o.getName());
    }

    public static class Builder {

        private final String name;

        private final int year;

        private String posterUrl;

        private String collection;

        private int collectionId;

        private int tvdbId;

        private String imdbId;

        public Builder(String name, int year) {
            this.name = name;
            this.year = year;
            this.tvdbId = -1;
            this.imdbId = "";
            this.collection = "";
            this.posterUrl = "";
            this.collectionId = -1;
        }

        public Movie build() {
            return new Movie(name, year, posterUrl, collection, collectionId, tvdbId, imdbId);
        }

        public Builder setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public Builder setCollection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder setCollectionId(int collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public Builder setTvdbId(int tvdbId) {
            this.tvdbId = tvdbId;
            return this;
        }

        public Builder setImdbId(String imdbId) {
            this.imdbId = imdbId;
            return this;
        }
    }
}
