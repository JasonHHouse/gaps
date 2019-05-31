package com.jasonhhouse.Gaps;

import java.util.concurrent.CompletableFuture;

public interface GapsSearch {

    CompletableFuture run(Properties properties);

    Integer getTotalMovieCount();

    Integer getSearchedMovieCount();

    void cancelSearch();
}
