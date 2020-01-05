/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GapsServiceImpl implements GapsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsServiceImpl.class);

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
        LOGGER.info("updateLibrarySelections( " + plexLibraries + " )");
        LOGGER.info("BEFORE:" + getPlexSearch().getLibraries().toString());
        for (PlexLibrary plexLibrary : plexLibraries) {
            int index = getPlexSearch().getLibraries().indexOf(plexLibrary);
            LOGGER.info("Index of plexLibrary: " + index + " - " + plexLibrary);
            if (index == -1) {
                getPlexSearch().getLibraries().add(plexLibrary);
            } else {
                getPlexSearch().getLibraries().get(index).setSelected(plexLibrary.getSelected());
            }

        }
        LOGGER.info("AFTER:" + getPlexSearch().getLibraries().toString());
    }

    @Override
    public String toString() {
        return "GapsServiceImpl{" +
                "plexSearch=" + plexSearch +
                '}';
    }

    @Override
    public void updatePlexSearch(PlexSearch plexSearch) {
        LOGGER.info("updatePlexSearch( " + plexSearch + " )");
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
