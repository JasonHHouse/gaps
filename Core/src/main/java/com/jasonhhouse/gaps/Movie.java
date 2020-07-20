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
public final class Movie implements Comparable<Movie> {

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

    private final String name;

    private final Integer year;

    @Nullable
    @JsonProperty("poster_url")
    private final String posterUrl;
    @Nullable
    private final String imdbId;
    @Nullable
    private final String language;
    @Nullable
    private final String overview;
    @Nullable
    private String collection;
    @NotNull
    private Integer collectionId;
    @NotNull
    private Integer tvdbId;
    @NotNull
    private final List<MovieFromCollection> moviesInCollection;

    private Movie(String name, Integer year, @Nullable String posterUrl, @Nullable String collection, @NotNull Integer collectionId, @NotNull Integer tvdbId,
                  @Nullable String imdbId, @Nullable String language, @Nullable String overview, @NotNull List<MovieFromCollection> moviesInCollection) {
        this.name = name;
        this.year = year;
        this.posterUrl = posterUrl;
        this.collection = collection;
        this.collectionId = collectionId;
        this.tvdbId = tvdbId;
        this.imdbId = imdbId;
        this.language = language;
        this.overview = overview;
        this.moviesInCollection = moviesInCollection;
    }

    public @NotNull Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public @NotNull Integer getTvdbId() {
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

    public void setCollection(@Nullable String collection) {
        this.collection = collection;
    }

    @Nullable
    public String getImdbId() {
        return imdbId;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    @NotNull
    public List<MovieFromCollection> getMoviesInCollection() {
        return moviesInCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;

        //Compare tvdb id first
        if (tvdbId != -1 && tvdbId.equals(movie.tvdbId)) {
            //LOGGER.info("Movie - tvdbId equals() true: " + tvdbId);
            return true;
        }

        //Compare imdb id next
        if (StringUtils.isNotEmpty(imdbId) && imdbId.equals(movie.imdbId)) {
            //LOGGER.info("Movie - imdbId equals() true: " + imdbId);
            return true;
        }

        //Fallback is year and title
        return year.equals(movie.year) && name.equals(movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
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
                ", posterUrl='" + posterUrl + '\'' +
                ", collection='" + collection + '\'' +
                ", collectionId=" + collectionId +
                ", tvdbId=" + tvdbId +
                ", imdbId='" + imdbId + '\'' +
                ", language='" + language + '\'' +
                ", overview='" + overview + '\'' +
                '}';
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

        private String language;

        private String overview;

        private List<MovieFromCollection> moviesInCollection;

        public Builder(String name, int year) {
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
        }

        public Movie build() {
            return new Movie(name, year, posterUrl, collection, collectionId, tvdbId, imdbId, language, overview, moviesInCollection);
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

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder setMoviesInCollection(List<MovieFromCollection> moviesInCollection) {
            this.moviesInCollection = moviesInCollection;
            return this;
        }
    }
}
