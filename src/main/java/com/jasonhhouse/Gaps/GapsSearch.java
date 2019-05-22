package com.jasonhhouse.Gaps;

import java.util.Set;

public interface GapsSearch {

    Set<Movie> run(Properties properties);

    Integer getTotalMovieCount();

    Integer getSearchedMovieCount();

}
