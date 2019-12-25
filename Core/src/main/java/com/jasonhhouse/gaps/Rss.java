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

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getImdbId() {
        return imdbId;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
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
