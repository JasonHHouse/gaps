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

import com.jasonhhouse.gaps.movie.GapsMovie;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SearchResults {
    @NotNull
    private final Integer searchedMovieCount;
    @NotNull
    private final Integer totalMovieCount;
    @Nullable
    private final GapsMovie nextGapsMovie;

    public SearchResults(@NotNull Integer searchedMovieCount, @NotNull Integer totalMovieCount, @Nullable GapsMovie nextGapsMovie) {
        this.searchedMovieCount = searchedMovieCount;
        this.totalMovieCount = totalMovieCount;
        this.nextGapsMovie = nextGapsMovie;
    }

    public @NotNull Integer getSearchedMovieCount() {
        return searchedMovieCount;
    }

    public @NotNull Integer getTotalMovieCount() {
        return totalMovieCount;
    }

    public @Nullable GapsMovie getNextGapsMovie() {
        return nextGapsMovie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResults that = (SearchResults) o;
        return searchedMovieCount.equals(that.searchedMovieCount) &&
                totalMovieCount.equals(that.totalMovieCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchedMovieCount, totalMovieCount, nextGapsMovie);
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "searchedMovieCount=" + searchedMovieCount +
                ", totalMovieCount=" + totalMovieCount +
                ", nextGapsMovie=" + nextGapsMovie +
                '}';
    }
}
