/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jasonhhouse.gaps.MovieFromCollection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

interface GapsMovie {

    @NotNull Integer getCollectionId();

    void setCollectionId(@NotNull Integer collectionId);

    @NotNull String getName();

    @NotNull Integer getYear();

    @NotNull String getCollectionTitle();

    void setCollectionTitle(@NotNull String collectionTitle);

    @NotNull String getImdbId();

    void setImdbId(@NotNull String imdbId);

    @NotNull Integer getTmdbId();

    void setTmdbId(@NotNull Integer tmdbId);

    @NotNull String getLanguage();

    @NotNull String getOverview();

    @NotNull List<MovieFromCollection> getMoviesInCollection();

    @JsonIgnore
    @NotNull String getNameWithoutBadCharacters();

    @NotNull Integer getRatingKey();

    @NotNull String getKey();

}
