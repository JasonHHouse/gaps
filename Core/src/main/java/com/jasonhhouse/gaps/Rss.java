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
import java.util.Objects;

public class Rss {
    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("release_date")
    private Integer releaseDate;

    @JsonProperty("tvdb_id")
    private Integer tvdbId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("poster_path")
    private String posterPath;

    public Rss() {
    }

    public Rss(String imdbId, Integer releaseDate, Integer tvdbId, String title, String posterPath) {
        this.imdbId = imdbId;
        this.releaseDate = releaseDate;
        this.tvdbId = tvdbId;
        this.title = title;
        this.posterPath = posterPath;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rss rss = (Rss) o;
        return Objects.equals(imdbId, rss.imdbId) &&
                Objects.equals(releaseDate, rss.releaseDate) &&
                Objects.equals(tvdbId, rss.tvdbId) &&
                Objects.equals(title, rss.title) &&
                Objects.equals(posterPath, rss.posterPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId, releaseDate, tvdbId, title, posterPath);
    }

    @Override
    public String toString() {
        return "Rss{" +
                "imdbId='" + imdbId + '\'' +
                ", releaseDate=" + releaseDate +
                ", tvdbId=" + tvdbId +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
