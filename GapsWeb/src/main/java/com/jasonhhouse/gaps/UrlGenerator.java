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
    @NotNull HttpUrl generateSearchMovieUrl(String movieDbKey, String query, String year);

    /**
     * Better way to search from plex returning imdb id. Is exact in returning responses but may not be available to
     * all users
     *
     * @param movieDbKey TMDB key
     * @param imdbId     IMDB id
     * @return query
     */
    @NotNull HttpUrl generateFindMovieUrl(String movieDbKey, String imdbId);

    /**
     * Movie searches and finds don't contain collection information and a second detail per movie query must be run
     *
     * @param movieDbKey TMDB key
     * @param movieId    TMDB id
     * @return query
     */
    @NotNull HttpUrl generateMovieDetailUrl(String movieDbKey, String movieId);

    /**
     * With a collection id from a movie, then a user can search for collection details to get the other movies
     *
     * @param movieDbKey   TMDB key
     * @param collectionId collection id
     * @return query
     */
    @NotNull HttpUrl generateCollectionUrl(String movieDbKey, String collectionId);

    /**
     * Using plex token, address, and port, create a plex url to query for plex movie collections to select from
     *
     * @param plexUrl Takes URL and parses it
     * @return query
     */
    @NotNull HttpUrl generatePlexUrl(String plexUrl);
}
