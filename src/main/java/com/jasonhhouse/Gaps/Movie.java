package com.jasonhhouse.Gaps;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

final class Movie implements Comparable<Movie> {

    private final String name;
    private final int year;
    private final String collection;

    Movie(String name, int year, String collection) {
        this.name = name;
        this.year = year;
        this.collection = collection;
    }

    String getName() {
        return name;
    }

    int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year &&
                Objects.equals(name, movie.name);
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
}
