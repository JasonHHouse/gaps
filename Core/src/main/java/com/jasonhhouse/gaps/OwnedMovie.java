package com.jasonhhouse.gaps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jasonhhouse.gaps.json.OwnedMovieDeserializer;
import com.jasonhhouse.gaps.json.OwnedMovieSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerialize(using = OwnedMovieSerializer.class)
@JsonDeserialize(using = OwnedMovieDeserializer.class)
public class OwnedMovie implements Comparable<OwnedMovie>, MovieMetadata {

    public static final String TVDB_ID = "tvdbId";

    public static final String IMDB_ID = "imdbId";

    public static final String NAME = "name";

    public static final String YEAR = "year";

    public static final String THUMBNAIL = "thumbnail";

    private final String name;

    private final int year;

    private String thumbnail;

    private int tvdbId;

    @Nullable
    private String imdbId;

    public OwnedMovie(String name, int year, String thumbnail, int tvdbId, @Nullable String imdbId) {
        this.name = name;
        this.year = year;
        this.thumbnail = thumbnail;
        this.tvdbId = tvdbId;
        this.imdbId = imdbId;
    }

    @Override
    public int getTvdbId() {
        return tvdbId;
    }

    @Override
    public void setTvdbId(int tvdbId) {
        this.tvdbId = tvdbId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public @Nullable String getImdbId() {
        return imdbId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int compareTo(@NotNull OwnedMovie o) {
        return getName().compareTo(o.getName());
    }
}
