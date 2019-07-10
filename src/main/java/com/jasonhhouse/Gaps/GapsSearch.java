package com.jasonhhouse.Gaps;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;

public interface GapsSearch {

    @NotNull CompletableFuture run(@NotNull Gaps gaps);

    @NotNull Integer getTotalMovieCount();

    @NotNull Integer getSearchedMovieCount();

    @NotNull CopyOnWriteArrayList<Movie> getRecommendedMovies();

    @NotNull Set<PlexLibrary> getPlexLibraries(@NotNull String address, @NotNull int port, @NotNull String token);

    void cancelSearch();

    boolean isSearching();
}