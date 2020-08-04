package com.jasonhhouse.gaps;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class MovieFromCollection {
    @NotNull
    private final String title;
    @NotNull
    private final String tmdbId;
    @NotNull
    private final Boolean owned;

    public MovieFromCollection(@NotNull String title, @NotNull String tmdbId, @NotNull Boolean owned) {
        this.title = title;
        this.tmdbId = tmdbId;
        this.owned = owned;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public String getTmdbId() {
        return tmdbId;
    }

    @NotNull
    public Boolean getOwned() {
        return owned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieFromCollection that = (MovieFromCollection) o;
        return title.equals(that.title) &&
                tmdbId.equals(that.tmdbId) &&
                owned.equals(that.owned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, tmdbId, owned);
    }

    @Override
    public String toString() {
        return "MovieFromCollection{" +
                "title='" + title + '\'' +
                ", id='" + tmdbId + '\'' +
                ", owned=" + owned +
                '}';
    }
}
