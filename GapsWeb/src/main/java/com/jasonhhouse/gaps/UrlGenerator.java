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
    @NotNull HttpUrl generateSearchMovieUrl(String movieDbKey, String query, String year, String language);

    /**
     * Better way to search from plex returning imdb id. Is exact in returning responses but may not be available to
     * all users
     *
     * @param movieDbKey TMDB key
     * @param imdbId     IMDB id
     * @return query
     */
    @NotNull HttpUrl generateFindMovieUrl(String movieDbKey, String imdbId, String language);

    /**
     * Movie searches and finds don't contain collection information and a second detail per movie query must be run
     *
     * @param movieDbKey TMDB key
     * @param movieId    TMDB id
     * @return query
     */
    @NotNull HttpUrl generateMovieDetailUrl(String movieDbKey, String movieId, String language);

    /**
     * With a collection id from a movie, then a user can search for collection details to get the other movies
     *
     * @param movieDbKey   TMDB key
     * @param collectionId collection id
     * @return query
     */
    @NotNull HttpUrl generateCollectionUrl(String movieDbKey, String collectionId, String language);

    /**
     * Using plex token, address, and port, create a plex url to query for plex movie collections to select from
     *
     * @param plexUrl Takes URL and parses it
     * @return query
     */
    @NotNull HttpUrl generatePlexUrl(String plexUrl);
}
