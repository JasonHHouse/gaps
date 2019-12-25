package com.jasonhhouse.gaps;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface PlexQuery {

    @NotNull List<PlexLibrary> getLibraries(@NotNull PlexSearch plexSearch);

}
