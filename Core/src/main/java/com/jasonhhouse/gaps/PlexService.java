package com.jasonhhouse.gaps;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface PlexService {

    @NotNull Set<PlexLibrary> getPlexLibraries(@NotNull PlexSearch plexSearch);

}
