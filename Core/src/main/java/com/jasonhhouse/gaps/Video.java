/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface Video {

    /*
     * @return The title of the movie
     */
    @NotNull String getTitle();

    /**
     * @return The year the movie was released
     */
    @NotNull Integer getYear();

    /**
     * @return The name of the collection the movie belongs too, empty otherwise
     */
    @NotNull String getCollectionTitle();

    /**
     * Set the name of the collection the movie belongs too
     *
     * @param collectionTitle the title
     */
    void setCollectionTitle(@NotNull String collectionTitle);

    /**
     * @return The id of the collection the movie belogns too, -1 otherwise
     */
    @NotNull Integer getCollectionId();

    /**
     * Set the id of the collection the movie belongs too
     *
     * @param collectionId the ID
     */
    void setCollectionId(@NotNull Integer collectionId);

    /**
     * @return The IMDB ID of the movie, empty otherwise
     */
    @NotNull String getImdbId();

    /**
     * Set the IMDB ID of the movie
     *
     * @param imdbId IMDB ID
     */
    void setImdbId(@NotNull Integer imdbId);

    /**
     * @return The TMDB ID of the movie, empty otherwise
     */
    @NotNull Integer getTmdbId();

    /**
     * Set the TMDB ID of the movie
     *
     * @param tmdbId TMDB ID
     */
    void setTmdbId(@NotNull String tmdbId);

    /**
     * @return List of movies in the collection, empty otherwise
     */
    @NotNull List<MovieFromCollection> getMoviesInCollection();

    /**
     * @return Removes some optional characters for easier movie title comparison
     */
    @NotNull String getTitleWithoutBadCharacters();
}
