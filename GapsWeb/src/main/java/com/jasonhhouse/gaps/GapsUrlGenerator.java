/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service(value = "real")
public class GapsUrlGenerator implements UrlGenerator {

    @Override
    public @NotNull HttpUrl generateSearchMovieUrl(@NotNull String movieDbKey, @NotNull String query, @NotNull String year, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("search")
                .addPathSegment("movie")
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", language)
                .addQueryParameter("page", "1")
                .addQueryParameter("include_adult", "false")
                .addQueryParameter("query", query)
                .addQueryParameter("year", year)
                .build();
    }

    @Override
    public @NotNull HttpUrl generateFindMovieUrl(@NotNull String movieDbKey, @NotNull String imdbId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("find")
                .addPathSegment(imdbId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", language)
                .addQueryParameter("external_source", "imdb_id")
                .build();
    }

    @Override
    public @NotNull HttpUrl generateMovieDetailUrl(@NotNull String movieDbKey, @NotNull String movieId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("movie")
                .addPathSegment(movieId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", language)
                .build();
    }

    @Override
    public @NotNull HttpUrl generateCollectionUrl(@NotNull String movieDbKey, @NotNull String collectionId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("collection")
                .addPathSegment(collectionId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", language)
                .build();
    }

    @Override
    public @Nullable HttpUrl generatePlexUrl(@NotNull String plexUrl) {
        return HttpUrl.parse(plexUrl);
    }
}
