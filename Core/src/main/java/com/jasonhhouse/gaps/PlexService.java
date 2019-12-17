package com.jasonhhouse.gaps;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface PlexService {

    @NotNull List<PlexLibrary> queryPlexLibraries(@NotNull PlexSearch plexSearch);

}
