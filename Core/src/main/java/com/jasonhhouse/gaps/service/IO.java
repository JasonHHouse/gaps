/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.movie.BasicMovie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.properties.PlexProperties;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface IO {

    @NotNull List<BasicMovie> readRecommendedMovies(@NotNull String machineIdentifier, @NotNull Integer key);

    @NotNull Boolean doesRssFileExist(@NotNull String machineIdentifier, @NotNull Integer key);

    @NotNull String getRssFile(String machineIdentifier, @NotNull Integer key);

    /**
     * Write the recommended movie list to the RSS file for endpoint to display.
     *
     * @param recommended The recommended movies. (IMDB ID is required.)
     */
    void writeRssFile(@NotNull String machineIdentifier, @NotNull Integer key, @NotNull Set<BasicMovie> recommended);

    /**
     * Prints out all recommended movies to recommendedMovies.json
     */
    void writeRecommendedToFile(@NotNull Set<BasicMovie> recommended, @NotNull String machineIdentifier, @NotNull Integer key);

    /**
     * Prints out all owned movies to ownedMovies.json
     */
    void writeOwnedMoviesToFile(@NotNull List<BasicMovie> ownedBasicMovies, @NotNull String machineIdentifier, @NotNull Integer key);

    @NotNull List<BasicMovie> readOwnedMovies(@NotNull String machineIdentifier, @NotNull Integer key);

    /**
     * Prints out all movies to a text file movieIds.json
     */
    void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie);

    void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie, @NotNull File file);

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    @NotNull Set<BasicMovie> readMovieIdsFromFile();

    void writeProperties(@NotNull PlexProperties plexProperties);

    @NotNull PlexProperties readProperties();

    @NotNull Payload nuke();

}
