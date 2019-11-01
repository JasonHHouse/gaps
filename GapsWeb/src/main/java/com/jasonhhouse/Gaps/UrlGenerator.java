package com.jasonhhouse.Gaps;

import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

public interface UrlGenerator {
    @NotNull HttpUrl generateSearchMovieUrl(String movieDbKey, String query, String year);

    @NotNull HttpUrl generateMovieDetailUrl(String movieDbKey, String movieId);

    @NotNull HttpUrl generateCollectionUrl(String movieDbKey, String collectionId);

    @NotNull HttpUrl generatePlexUrl(String plexUrl);
}
