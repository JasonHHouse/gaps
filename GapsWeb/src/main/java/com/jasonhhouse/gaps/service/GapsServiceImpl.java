package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GapsServiceImpl implements GapsService {

    @NotNull
    private final PlexSearch plexSearch;

    @NotNull
    private final Set<PlexLibrary> plexLibraries;

    public GapsServiceImpl() {
        this.plexSearch = new PlexSearch();
        this.plexLibraries = new HashSet<>();
    }

    @Override
    public @NotNull PlexSearch getPlexSearch() {
        return plexSearch;
    }

    @Override
    public @NotNull Set<PlexLibrary> getPlexLibraries() {
        return plexLibraries;
    }

    @Override
    public String toString() {
        return "GapsServiceImpl{" +
                "plexSearch=" + plexSearch +
                ", plexLibraries=" + plexLibraries +
                '}';
    }

    @Override
    public void copyInLibraries(@NotNull Set<PlexLibrary> plexLibraries) {
        plexLibraries.forEach(plexLibrary -> plexSearch.setLibrary(plexLibrary.getTitle(), false));
        this.plexLibraries.addAll(plexLibraries);
    }

    @Override
    public void updatePlexSearch(PlexSearch plexSearch) {
        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            this.plexSearch.setAddress(plexSearch.getAddress());
        }

        if (plexSearch.getPort() != null) {
            this.plexSearch.setPort(plexSearch.getPort());
        }

        if (StringUtils.isEmpty(plexSearch.getPlexToken())) {
            this.plexSearch.setPlexToken(plexSearch.getPlexToken());
        }

        if (StringUtils.isEmpty(plexSearch.getMovieDbApiKey())) {
            this.plexSearch.setMovieDbApiKey(plexSearch.getMovieDbApiKey());
        }
    }
}
