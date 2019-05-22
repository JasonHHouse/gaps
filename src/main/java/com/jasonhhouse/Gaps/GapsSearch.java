package com.jasonhhouse.Gaps;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface GapsSearch {

    CompletableFuture<ResponseEntity<Set<Movie>>> run(Properties properties);

    Integer getTotalMovieCount();

    Integer getSearchedMovieCount();

}
