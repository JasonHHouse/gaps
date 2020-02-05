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

import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the process of searching, movies, counts, and canceling
 */
public interface GapsSearch {

    /**
     * Kicks of searching for all missing movies
     */
    @Deprecated
    void run();

    /**
     * Kicks of searching for all missing movies
     */
    void run(String machineIdentifier, Integer key);

    /**
     * @return The total count of movies to be searched
     */
    @NotNull Integer getTotalMovieCount();

    /**
     * @return The current count of movies searched
     */
    @NotNull Integer getSearchedMovieCount();

    /**
     * @return The movies that are missing from collections
     */
    @NotNull CopyOnWriteArrayList<Movie> getRecommendedMovies();

    /**
     * Cancel the current search
     */
    void cancelSearch();

    /**
     * @return Returns true if currently searching
     */
    boolean isSearching();
}