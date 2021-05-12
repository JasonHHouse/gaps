package com.jasonhhouse.gaps;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.jetbrains.annotations.NotNull;

@Entity
public class MonitoredMovie {
    @Id
    @NotNull
    private Integer tmdbId;

    @NotNull
    private Boolean monitored;

    public MonitoredMovie() {
    }

    public MonitoredMovie(@NotNull Integer tmdbId, @NotNull Boolean monitored) {
        this.tmdbId = tmdbId;
        this.monitored = monitored;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(@NotNull Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public @NotNull Boolean getMonitored() {
        return monitored;
    }

    public void setMonitored(@NotNull Boolean monitored) {
        this.monitored = monitored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoredMovie monitoredMovie1 = (MonitoredMovie) o;
        return tmdbId.equals(monitoredMovie1.tmdbId) && monitored.equals(monitoredMovie1.monitored);
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
