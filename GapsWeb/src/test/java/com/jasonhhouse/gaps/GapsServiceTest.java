package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GapsServiceTest implements GapsService {

    private PlexSearch plexSearch;

    public GapsServiceTest() {
        plexSearch = new PlexSearch();
        plexSearch.setMovieDbApiKey("key");
        plexSearch.addPlexServer(new PlexServer("Joker", "asdf1q1w34asldka", "token", "address", 1));

        PlexLibrary plexLibrary = new PlexLibrary(1, "Movies", "asdf1q1w34asldka", true);
        PlexLibrary plexLibrary1 = new PlexLibrary(2, "Movies HD", "asdf1q1w34asldka", false);
        List<PlexLibrary> plexLibraries = new ArrayList<>();
        plexLibraries.add(plexLibrary);
        plexLibraries.add(plexLibrary1);
        plexSearch.getPlexServers().stream().findFirst().orElse(new PlexServer()).getPlexLibraries().addAll(plexLibraries);
    }

    @Override
    public PlexSearch getPlexSearch() {
        return plexSearch;
    }

    @Override
    public void updateLibrarySelections(@NotNull List<String> plexLibraries) {

    }

    @Override
    public void updatePlexSearch(PlexSearch plexSearch) {

    }

    @Override
    public void nukePlexSearch() {

    }
}
