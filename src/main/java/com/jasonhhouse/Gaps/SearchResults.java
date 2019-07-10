package com.jasonhhouse.Gaps;

import java.util.List;
import java.util.Objects;

public final class SearchResults {
    private final int searchedMovieCount;
    private final int totalMovieCount;
    private final List moviesFound;

    public SearchResults(int searchedMovieCount, int totalMovieCount, List moviesFound) {
        this.searchedMovieCount = searchedMovieCount;
        this.totalMovieCount = totalMovieCount;
        this.moviesFound = moviesFound;
    }

    public int getSearchedMovieCount() {
        return searchedMovieCount;
    }

    public int getTotalMovieCount() {
        return totalMovieCount;
    }

    public List getMoviesFound() {
        return moviesFound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResults that = (SearchResults) o;
        return searchedMovieCount == that.searchedMovieCount &&
                totalMovieCount == that.totalMovieCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchedMovieCount, totalMovieCount);
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "searchedMovieCount=" + searchedMovieCount +
                ", totalMovieCount=" + totalMovieCount +
                ", moviesFound=" + moviesFound +
                '}';
    }
}
