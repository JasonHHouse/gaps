package com.jasonhhouse.gaps;

import java.util.Objects;

public class MoviePair {

    private final String title;
    private final int year;

    public MoviePair(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviePair moviePair = (MoviePair) o;
        return year == moviePair.year &&
                Objects.equals(title, moviePair.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public String toString() {
        return "MoviePair{" +
                "title='" + title + '\'' +
                ", year=" + year +
                '}';
    }
}
