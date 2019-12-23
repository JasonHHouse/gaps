package com.jasonhhouse.gaps;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlexQuery {

    @NotNull List<PlexLibrary> getLibraries(@NotNull PlexSearch plexSearch);

}
