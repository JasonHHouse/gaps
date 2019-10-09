package com.jasonhhouse.Gaps;

import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service(value="real")
public class GapsUrlGenerator implements UrlGenerator {

    @Override
    public @NotNull HttpUrl generateSearchMovieUrl(String movieDbKey, String query, String year) {
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
    public @NotNull HttpUrl generateMovieDetailUrl(String movieDbKey, String movieId) {
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
    public @NotNull HttpUrl generateCollectionUrl(String movieDbKey, String collectionId) {
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
}
