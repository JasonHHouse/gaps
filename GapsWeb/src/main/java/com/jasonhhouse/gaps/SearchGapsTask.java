package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.service.IoService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import okhttp3.HttpUrl;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchGapsTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchGapsTask.class);

    private final GapsService gapsService;
    private final GapsSearch gapsSearch;
    private final IoService ioService;
    private final PlexQuery plexQuery;
    private final GapsUrlGenerator gapsUrlGenerator;

    public SearchGapsTask(GapsService gapsService, GapsSearch gapsSearch, IoService ioService, PlexQuery plexQuery, GapsUrlGenerator gapsUrlGenerator) {
        this.gapsService = gapsService;
        this.gapsSearch = gapsSearch;
        this.ioService = ioService;
        this.plexQuery = plexQuery;
        this.gapsUrlGenerator = gapsUrlGenerator;
    }


    @Override
    public void run() {
        LOGGER.info("run()");

        if (CollectionUtils.isEmpty(gapsService.getPlexSearch().getPlexServers())) {
            LOGGER.warn("No Plex Servers Found. Canceling automatic search.");
            return;
        }

        updatePlexLibraries();

        updateLibraryMovies();

        findRecommendedMovies();
    }

    private void updateLibraryMovies() {
        LOGGER.info("updateLibraryMovies");
        for (PlexServer plexServer : gapsService.getPlexSearch().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer.getPlexLibraries()) {
                HttpUrl url = gapsUrlGenerator.generatePlexLibraryUrl(plexServer, plexLibrary);
                List<Movie> ownedMovies =plexQuery.findAllPlexMovies(generateOwnedMovieMap(), url);
                ioService.writeOwnedMoviesToFile(ownedMovies, plexLibrary.getMachineIdentifier(), plexLibrary.getKey());
            }
        }
    }

    private void updatePlexLibraries() {
        LOGGER.info("updatePlexLibraries");
        //Update each Plex Library from each Plex Server
        for (PlexServer plexServer : gapsService.getPlexSearch().getPlexServers()) {
            Payload getLibrariesResults = plexQuery.getLibraries(plexServer);
            if (Payload.PLEX_LIBRARIES_FOUND == getLibrariesResults) {
                LOGGER.info("Plex libraries found for Plex Server {}", plexServer.getFriendlyName());
            } else {
                LOGGER.warn("Plex libraries not found for Plex Server {}", plexServer.getFriendlyName());
            }
        }
    }

    private void findRecommendedMovies() {
        LOGGER.info("findRecommendedMovies");
        for (PlexServer plexServer : gapsService.getPlexSearch().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer.getPlexLibraries()) {
                gapsSearch.run(plexLibrary.getMachineIdentifier(), plexLibrary.getKey());
            }
        }
    }

    private Map<Pair<String, Integer>, Movie> generateOwnedMovieMap() {
        Set<Movie> everyMovie = ioService.readMovieIdsFromFile();
        Map<Pair<String, Integer>, Movie> previousMovies = new HashMap<>();

        gapsService
                .getPlexSearch()
                .getPlexServers()
                .forEach(plexServer -> plexServer
                        .getPlexLibraries()
                        .stream()
                        .filter(PlexLibrary::getSelected)
                        .forEach(plexLibrary -> everyMovie.forEach(movie -> previousMovies.put(new Pair<>(movie.getName(), movie.getYear()), movie))));

        return previousMovies;
    }
}