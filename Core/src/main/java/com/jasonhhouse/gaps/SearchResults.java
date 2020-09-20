/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.movie.BasicMovie;
import java.util.Objects;

public final class SearchResults {
    private final int searchedMovieCount;
    private final int totalMovieCount;
    private final BasicMovie nextBasicMovie;

    public SearchResults(int searchedMovieCount, int totalMovieCount, BasicMovie nextBasicMovie) {
        this.searchedMovieCount = searchedMovieCount;
        this.totalMovieCount = totalMovieCount;
        this.nextBasicMovie = nextBasicMovie;
    }

    public int getSearchedMovieCount() {
        return searchedMovieCount;
    }

    public int getTotalMovieCount() {
        return totalMovieCount;
    }

    public BasicMovie getNextMovie() {
        return nextBasicMovie;
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
                ", nextMovie=" + nextBasicMovie +
                '}';
    }
}
