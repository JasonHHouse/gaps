package com.jasonhhouse.gaps;

import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service(value = "real")
public class GapsUrlGenerator implements UrlGenerator {

    @Override
    public @NotNull HttpUrl generateSearchMovieUrl(@NotNull String movieDbKey, @NotNull String query, @NotNull String year) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("search")
                .addPathSegment("movie")
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", "en-US")
                .addQueryParameter("page", "1")
                .addQueryParameter("include_adult", "false")
                .addQueryParameter("query", query)
                .addQueryParameter("year", year)
                .build();
    }

    @Override
    public @NotNull HttpUrl generateFindMovieUrl(@NotNull String movieDbKey, @NotNull String imdbId) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("find")
                .addPathSegment(imdbId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", "en-US")
                .addQueryParameter("external_source", "imdb_id")
                .build();
    }

    @Override
    public @NotNull HttpUrl generateMovieDetailUrl(@NotNull String movieDbKey, @NotNull String movieId) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("movie")
                .addPathSegment(movieId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", "en-US")
                .build();
    }

    @Override
    public @NotNull HttpUrl generateCollectionUrl(@NotNull String movieDbKey, @NotNull String collectionId) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("collection")
                .addPathSegment(collectionId)
                .addQueryParameter("api_key", movieDbKey)
                .addQueryParameter("language", "en-US")
                .build();
    }

    @Override
    public @NotNull HttpUrl generatePlexUrl(@NotNull String plexUrl) {
        return HttpUrl.parse(plexUrl);
    }
}
