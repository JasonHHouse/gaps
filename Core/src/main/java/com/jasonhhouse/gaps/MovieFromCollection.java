package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MovieFromCollection {
    @NotNull
    private final String title;
    @NotNull
    private final Integer tmdbId;
    @NotNull
    private final Boolean owned;
    @NotNull
    private final Integer year;

    @JsonCreator
    public MovieFromCollection(@JsonProperty(value = "title") @Nullable String title,
                               @JsonProperty(value = "tmdbId") @Nullable Integer tmdbId,
                               @JsonProperty(value = "owned") @Nullable Boolean owned,
                               @JsonProperty(value = "year") @Nullable Integer year) {
        this.title = StringUtils.isEmpty(title) ? "" : title;
        this.tmdbId = tmdbId == null ? -1 : tmdbId;
        this.owned = owned != null && owned;
        this.year = year == null ? -1 : year;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public Integer getTmdbId() {
        return tmdbId;
    }

    @NotNull
    public Boolean getOwned() {
        return owned;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "MovieFromCollection{" +
                "title='" + title + '\'' +
                ", tmdbId=" + tmdbId +
                ", owned=" + owned +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieFromCollection that = (MovieFromCollection) o;
        return title.equals(that.title) && tmdbId.equals(that.tmdbId) && owned.equals(that.owned) && year.equals(that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, tmdbId, owned, year);
    }
}
