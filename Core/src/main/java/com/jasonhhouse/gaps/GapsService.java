package com.jasonhhouse.gaps;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GapsService {

    PlexSearch getPlexSearch();

    void updateLibrarySelections(@NotNull List<PlexLibrary> plexLibraries);

    void updatePlexSearch(PlexSearch plexSearch);
}
