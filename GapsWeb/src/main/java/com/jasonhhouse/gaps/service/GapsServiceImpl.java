package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GapsServiceImpl implements GapsService {

    @NotNull
    private final PlexSearch plexSearch;

    public GapsServiceImpl() {
        this.plexSearch = new PlexSearch();
    }

    @Override
    public @NotNull PlexSearch getPlexSearch() {
        return plexSearch;
    }

    @Override
    public void updateLibrarySelections(@NotNull List<PlexLibrary> plexLibraries) {
        getPlexSearch().getLibraries().clear();
        getPlexSearch().getLibraries().addAll(plexLibraries);
    }

    @Override
    public String toString() {
        return "GapsServiceImpl{" +
                "plexSearch=" + plexSearch +
                '}';
    }

    @Override
    public void updatePlexSearch(PlexSearch plexSearch) {
        if (StringUtils.isNotEmpty(plexSearch.getAddress())) {
            this.plexSearch.setAddress(plexSearch.getAddress());
        }

        if (plexSearch.getPort() != null) {
            this.plexSearch.setPort(plexSearch.getPort());
        }

        if (StringUtils.isNotEmpty(plexSearch.getPlexToken())) {
            this.plexSearch.setPlexToken(plexSearch.getPlexToken());
        }

        if (StringUtils.isNotEmpty(plexSearch.getMovieDbApiKey())) {
            this.plexSearch.setMovieDbApiKey(plexSearch.getMovieDbApiKey());
        }
    }
}
