package com.jasonhhouse.gaps;/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public final class Movie implements Comparable<Movie>, Jsonify<Movie> {

    public static final String TVDB_ID = "tvdbId";

    public static final String IMDB_ID = "imdbId";

    public static final String NAME = "name";

    public static final String YEAR = "year";

    public static final String POSTER = "poster_url";

    private final String name;

    private final int year;

    @Nullable
    private String posterUrl;

    @Nullable
    private String collection;

    private int tvdbId;

    @Nullable
    private String imdbId;

    public Movie(int tvdbId, @Nullable String imdbId, String name, int year, @Nullable String collection, @Nullable String posterUrl) {
        this.tvdbId = tvdbId;
        this.imdbId = imdbId;
        this.name = name;
        this.year = year;
        this.collection = collection;
        this.posterUrl = posterUrl;
    }

    public Movie(int tvdbId, @Nullable String imdbId, String name, int year) {
        this(tvdbId, imdbId, name, year, null, null);
    }

    public Movie(String imdbId, String name, int year, String collection) {
        this(-1, imdbId, name, year, collection, null);
    }

    public Movie(int tvdbId, String name, int year, String collection) {
        this(tvdbId, null, name, year, collection, null);
    }

    public Movie(int tvdbId, String name, int year, String collection, String posterUrl) {
        this(tvdbId, null, name, year, collection, null);
    }

    public Movie(String name, int year, String collection) {
        this(-1, name, year, collection);
    }

    public Movie(String name, int year) {
        this(-1, name, year, null);
    }

    public int getTvdbId() {
        return tvdbId;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

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

    public void merge(Movie movie) {
        tvdbId = tvdbId == -1 ? movie.getTvdbId() : -1;
        imdbId = imdbId == null ? movie.getImdbId() : null;
        collection = collection == null ? movie.getCollection() : null;
        posterUrl = posterUrl == null ? movie.getPosterUrl() : null;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TVDB_ID, tvdbId);
        jsonObject.put(IMDB_ID, imdbId);
        jsonObject.put(NAME, name);
        jsonObject.put(YEAR, year);
        jsonObject.put(POSTER, posterUrl);
        return jsonObject;
    }

}
