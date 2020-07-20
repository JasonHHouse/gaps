package com.jasonhhouse.gaps;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class MovieFromCollection {
    @NotNull
    private final String title;
    @NotNull
    private final String id;
    @NotNull
    private final Boolean owned;

    public MovieFromCollection(@NotNull String title, @NotNull String id, @NotNull Boolean owned) {
        this.title = title;
        this.id = id;
        this.owned = owned;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public String getId() {
        return id;
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
                id.equals(that.id) &&
                owned.equals(that.owned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, owned);
    }

    @Override
    public String toString() {
        return "MovieFromCollection{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", owned=" + owned +
                '}';
    }
}
