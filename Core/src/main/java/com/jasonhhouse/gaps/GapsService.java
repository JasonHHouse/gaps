package com.jasonhhouse.gaps;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface GapsService {

    PlexSearch getPlexSearch();

    Set<PlexLibrary> getPlexLibraries();

    void copyInLibraries(@NotNull Set<PlexLibrary> plexLibraries);
}
