package com.jasonhhouse.Gaps;

import java.util.Objects;

public class Movie implements Comparable<Movie> {

    private String name;
    private int year;
    private String collection;

    public Movie() {
    }

    Movie(String name, int year, String collection) {
        this.name = name;
        this.year = year;
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
        return "name='" + name + '\'' +
                ", year=" + year +
                ", collection='" + collection + '\'';
    }

    public int compareTo(Movie o) {
        return o.getName().compareTo(getName());
    }
}
