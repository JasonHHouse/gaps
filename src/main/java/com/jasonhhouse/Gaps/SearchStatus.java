package com.jasonhhouse.Gaps;

import java.util.Objects;

public final class SearchStatus {
    private final int searchedMovieCount;
    private final int totalMovieCount;

    public SearchStatus(int searchedMovieCount, int totalMovieCount) {
        this.searchedMovieCount = searchedMovieCount;
        this.totalMovieCount = totalMovieCount;
    }

    public int getSearchedMovieCount() {
        return searchedMovieCount;
    }

    public int getTotalMovieCount() {
        return totalMovieCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchStatus that = (SearchStatus) o;
        return searchedMovieCount == that.searchedMovieCount &&
                totalMovieCount == that.totalMovieCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchedMovieCount, totalMovieCount);
    }

    @Override
    public String toString() {
        return "SearchStatus{" +
                "searchedMovieCount=" + searchedMovieCount +
                ", totalMovieCount=" + totalMovieCount +
                '}';
    }
}
