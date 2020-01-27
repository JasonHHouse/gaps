package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GapsServiceTest implements GapsService {

    private PlexSearch plexSearch;

    public GapsServiceTest() {
        plexSearch = new PlexSearch();
        plexSearch.setPlexToken("token");
        plexSearch.setPort(1);
        plexSearch.setAddress("abc");
        plexSearch.setMovieDbApiKey("key");
        plexSearch.setPlexServer(new PlexServer("Joker", "asdf1q1w34asldka"));

        PlexLibrary plexLibrary = new PlexLibrary(1, "Movies", true);
        PlexLibrary plexLibrary1 = new PlexLibrary(2, "Movies HD", false);
        List<PlexLibrary> plexLibraries = new ArrayList<>();
        plexLibraries.add(plexLibrary);
        plexLibraries.add(plexLibrary1);
        plexSearch.getPlexServer().getPlexLibraries().addAll(plexLibraries);
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
}
