/*
 * Copyright 2025 Jason H House
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

public interface UrlGenerator {

    /**
     * Fallback search operation. Ideally use the find command to look up by imdb id but when that isn't available
     * search with this method using title and year
     *
     * @param movieDbKey TMDB key
     * @param query      search query which is the movie title
     * @param year       movie release year
     * @return query
     */
    @NotNull HttpUrl generateSearchMovieUrl(@NotNull String movieDbKey, @NotNull String query,@NotNull  String year, @NotNull String language);

    /**
     * Better way to search from plex returning imdb id. Is exact in returning responses but may not be available to
     * all users
     *
     * @param movieDbKey TMDB key
     * @param imdbId     IMDB id
     * @return query
     */
    @NotNull HttpUrl generateFindMovieUrl(@NotNull String movieDbKey,@NotNull  String imdbId,@NotNull  String language);

    /**
     * Movie searches and finds don't contain collection information and a second detail per movie query must be run
     *
     * @param movieDbKey TMDB key
     * @param movieId    TMDB id
     * @return query
     */
    @NotNull HttpUrl generateMovieDetailUrl(@NotNull String movieDbKey,@NotNull  String movieId, @NotNull String language);

    /**
     * With a collection id from a movie, then a user can search for collection details to get the other movies
     *
     * @param movieDbKey   TMDB key
     * @param collectionId collection id
     * @return query
     */
    @NotNull HttpUrl generateCollectionUrl(@NotNull String movieDbKey, @NotNull String collectionId,@NotNull  String language);

    /**
     * Using plex token, address, and port, create a plex url to query for plex movie collections to select from
     *
     * @param plexUrl Takes URL and parses it
     * @return query
     */
    @Nullable HttpUrl generatePlexUrl(@NotNull String plexUrl);

    /**
     * Using plex token, address, and port, create a plex library url to query for all plex movies
     *
     * @param plexServer    The PlexServer to query
     * @param plexLibrary The PlexLibrary to query
     * @return query
     */
    @Nullable HttpUrl generatePlexLibraryUrl(@NotNull PlexServer plexServer,@NotNull  PlexLibrary plexLibrary);

    @NotNull HttpUrl generatePlexMetadataUrl(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary,@NotNull  Integer ratingKey);

}
