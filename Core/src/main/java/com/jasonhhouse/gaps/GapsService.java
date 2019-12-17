package com.jasonhhouse.gaps;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GapsService {

    PlexSearch getPlexSearch();

    void updateLibrarySelections(@NotNull List<PlexLibrary> plexLibraries);

    void updatePlexSearch(PlexSearch plexSearch);
}
