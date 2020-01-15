package com.jasonhhouse.gaps;

import org.jetbrains.annotations.Nullable;

public interface MovieMetadata {

    int getTvdbId();

    void setTvdbId(int tvdbId);

    String getName();

    int getYear();

    @Nullable
    String getImdbId();
}
