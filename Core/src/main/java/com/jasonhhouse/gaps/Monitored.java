package com.jasonhhouse.gaps;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.jetbrains.annotations.NotNull;

@Entity
public class Monitored {
    @Id
    @NotNull
    private final Integer tmdbId;

    @NotNull
    private final Boolean monitored;

    public Monitored(@NotNull Integer tmdbId, @NotNull Boolean monitored) {
        this.tmdbId = tmdbId;
        this.monitored = monitored;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public @NotNull Boolean getMonitored() {
        return monitored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Monitored monitored1 = (Monitored) o;
        return tmdbId.equals(monitored1.tmdbId) && monitored.equals(monitored1.monitored);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbId, monitored);
    }

    @Override
    public String toString() {
        return "Monitored{" +
                "tmdbId=" + tmdbId +
                ", monitored=" + monitored +
                '}';
    }
}
