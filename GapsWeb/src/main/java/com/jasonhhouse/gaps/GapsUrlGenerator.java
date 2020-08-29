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

import com.jasonhhouse.plex.libs.PlexLibrary;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service(value = "real")
public class GapsUrlGenerator implements UrlGenerator {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String TMDB_URL = "api.themoviedb.org";
    private static final String TMDB_VERSION = "3";
    private static final String SEARCH = "search";
    private static final String MOVIE = "movie";
    private static final String API_KEY = "api_key";
    private static final String LANGUAGE = "language";
    private static final String PAGE = "page";
    private static final String INCLUDE_ADULT = "include_adult";
    private static final String QUERY = "query";
    private static final String YEAR = "year";
    private static final String FIND = "find";
    private static final String EXTERNAL_SOURCE = "external_source";
    private static final String COLLECTION = "collection";
    private static final String LIBRARY = "library";
    private static final String SECTIONS = "sections";
    private static final String METADATA = "metadata";
    private static final String ALL = "all";
    private static final String PLEX_TOKEN = "X-Plex-Token";

    @Override
    public @NotNull HttpUrl generateSearchMovieUrl(@NotNull String movieDbKey, @NotNull String query, @NotNull String year, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(TMDB_URL)
                .addPathSegment(TMDB_VERSION)
                .addPathSegment(SEARCH)
                .addPathSegment(MOVIE)
                .addQueryParameter(API_KEY, movieDbKey)
                .addQueryParameter(LANGUAGE, language)
                .addQueryParameter(PAGE, "1")
                .addQueryParameter(INCLUDE_ADULT, "false")
                .addQueryParameter(QUERY, query)
                .addQueryParameter(YEAR, year)
                .build();
    }

    @Override
    public @NotNull HttpUrl generateFindMovieUrl(@NotNull String movieDbKey, @NotNull String imdbId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(TMDB_URL)
                .addPathSegment(TMDB_VERSION)
                .addPathSegment(FIND)
                .addPathSegment(imdbId)
                .addQueryParameter(API_KEY, movieDbKey)
                .addQueryParameter(LANGUAGE, language)
                .addQueryParameter(EXTERNAL_SOURCE, "imdb_id")
                .build();
    }

    @Override
    public @NotNull HttpUrl generateMovieDetailUrl(@NotNull String movieDbKey, @NotNull String movieId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(TMDB_URL)
                .addPathSegment(TMDB_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(movieId)
                .addQueryParameter(API_KEY, movieDbKey)
                .addQueryParameter(LANGUAGE, language)
                .build();
    }

    @Override
    public @NotNull HttpUrl generateCollectionUrl(@NotNull String movieDbKey, @NotNull String collectionId, @NotNull String language) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(TMDB_URL)
                .addPathSegment(TMDB_VERSION)
                .addPathSegment(COLLECTION)
                .addPathSegment(collectionId)
                .addQueryParameter(API_KEY, movieDbKey)
                .addQueryParameter(LANGUAGE, language)
                .build();
    }

    @Override
    public @Nullable HttpUrl generatePlexUrl(@NotNull String plexUrl) {
        return HttpUrl.parse(plexUrl);
    }

    @Override
    public @Nullable HttpUrl generatePlexLibraryUrl(@NotNull PlexServer plexServer,@NotNull  PlexLibrary plexLibrary) {
        return new HttpUrl.Builder()
                .scheme(HTTP)
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addPathSegment(LIBRARY)
                .addPathSegment(SECTIONS)
                .addPathSegment(plexLibrary.getKey().toString())
                .addPathSegment(ALL)
                .addQueryParameter(PLEX_TOKEN, plexServer.getPlexToken())
                .build();
    }

    @Override
    public @NotNull HttpUrl generatePlexMetadataUrl(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary, @NotNull Integer ratingKey) {
        return new HttpUrl.Builder()
                .scheme(HTTP)
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addPathSegment(LIBRARY)
                .addPathSegment(METADATA)
                .addPathSegment(ratingKey.toString())
                .addQueryParameter(PLEX_TOKEN, plexServer.getPlexToken())
                .build();
    }
}
