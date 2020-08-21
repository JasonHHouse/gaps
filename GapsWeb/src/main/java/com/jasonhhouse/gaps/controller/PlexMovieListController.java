/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.Pair;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/plex")
public class PlexMovieListController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexMovieListController.class);

    private final IoService ioService;
    private final PlexQuery plexQuery;

    @Autowired
    public PlexMovieListController(IoService ioService, PlexQuery plexQuery) {
        this.ioService = ioService;
        this.plexQuery = plexQuery;
    }

    @GetMapping(value = "/movies/{machineIdentifier}/{key}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Movie>> getPlexMovies(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getPlexMovies( {}, {} )", machineIdentifier, key);

        PlexProperties plexProperties = ioService.readProperties();
        Set<Movie> everyMovie = ioService.readMovieIdsFromFile();
        Map<Pair<String, Integer>, Movie> previousMovies = generateOwnedMovieMap(plexProperties, everyMovie);
        String url = generatePlexMovieUrl(plexProperties, machineIdentifier, key);
        List<Movie> ownedMovies = plexQuery.findAllPlexMovies(previousMovies, url);

        //Update Owned Movies
        ioService.writeOwnedMoviesToFile(ownedMovies, machineIdentifier, key);
        return ResponseEntity.ok().body(ownedMovies);
    }

    private Map<Pair<String, Integer>, Movie> generateOwnedMovieMap(PlexProperties plexProperties, Set<Movie> everyMovie) {
        Map<Pair<String, Integer>, Movie> previousMovies = new HashMap<>();

        plexProperties
                .getPlexServers()
                .forEach(plexServer -> plexServer
                        .getPlexLibraries()
                        .stream()
                        .filter(PlexLibrary::getSelected)
                        .forEach(plexLibrary -> everyMovie.forEach(movie -> previousMovies.put(new Pair<>(movie.getName(), movie.getYear()), movie))));

        return previousMovies;
    }

    private String generatePlexMovieUrl(PlexProperties plexProperties, String machineIdentifier, Integer key) {
        LOGGER.info("generatePlexUrl( {}, {} )", machineIdentifier, key);
        return plexProperties
                .getPlexServers()
                .stream()
                .filter(plexServer -> plexServer.getMachineIdentifier().equals(machineIdentifier))
                .map(plexServer -> plexServer
                        .getPlexLibraries()
                        .stream()
                        .filter(plexLibrary -> plexLibrary.getKey().equals(key))
                        .map(plexLibrary -> "http://" + plexServer.getAddress() + ":" + plexServer.getPort() + "/library/sections/" + plexLibrary.getKey() + "/all/?X-Plex-Token=" + plexServer.getPlexToken())
                        .findFirst().orElse(null))
                .findFirst()
                .orElse("");
    }

}
