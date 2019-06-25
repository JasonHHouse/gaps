package com.jasonhhouse.Gaps;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class PlexLibrary implements Comparable<PlexLibrary> {
    private Integer key;
    private String title;

    public PlexLibrary() {
    }

    public PlexLibrary(Integer key, String title) {
        this.key = key;
        this.title = title;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexLibrary that = (PlexLibrary) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "key=" + key +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NotNull PlexLibrary o) {
        return this.title.compareTo(o.title);
    }
}
