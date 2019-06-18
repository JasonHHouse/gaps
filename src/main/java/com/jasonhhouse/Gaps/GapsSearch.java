package com.jasonhhouse.Gaps;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface GapsSearch {

    @NotNull CompletableFuture run(@NotNull Gaps gaps);

    @NotNull Integer getTotalMovieCount();

    @NotNull Integer getSearchedMovieCount();

    void cancelSearch();

    boolean isSearching();
}
