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

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController(value = "/libraries")
public class LibraryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);

    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public LibraryController(IoService ioService, GapsService gapsService) {
        this.ioService = ioService;
        this.gapsService = gapsService;

        if (CollectionUtils.isEmpty(gapsService.getPlexSearch().getPlexServers())) {
            //Only add if empty, otherwise the server information should be correct
            List<PlexServer> plexServers = ioService.readPlexConfiguration();
            gapsService.getPlexSearch().getPlexServers().addAll(plexServers);
        }
    }

    @GetMapping
    public ModelAndView getLibraries() {
        LOGGER.info("getLibraries()");

        boolean plexServersFound;
        PlexServer plexServer;
        PlexLibrary plexLibrary;
        if (CollectionUtils.isNotEmpty(gapsService.getPlexSearch().getPlexServers())) {
            //Read first plex servers movies
            plexServer = gapsService.getPlexSearch().getPlexServers().stream().findFirst().orElse(new PlexServer());
            plexLibrary = plexServer.getPlexLibraries().stream().findFirst().orElse(new PlexLibrary());
            plexServersFound = true;
        } else {
            plexServer = new PlexServer();
            plexLibrary = new PlexLibrary();
            plexServersFound = false;
        }

        Map<String, PlexServer> plexServerMap = gapsService.getPlexSearch().getPlexServers().stream().collect(Collectors.toMap(PlexServer::getMachineIdentifier, Function.identity()));

        if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
            try {
                PlexSearch plexSearch = ioService.readProperties();
                gapsService.updatePlexSearch(plexSearch);

                if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
                    LOGGER.warn("No owned movies found.");
                    //ToDo
                    //Can't search without key, show error message here
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to read gaps properties.", e);
            }
        }

        ModelAndView modelAndView = new ModelAndView("libraries");
        modelAndView.addObject("plexServers", plexServerMap);
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        modelAndView.addObject("plexServer", plexServer);
        modelAndView.addObject("plexLibrary", plexLibrary);
        modelAndView.addObject("plexServersFound", plexServersFound);
        return modelAndView;
    }

    @GetMapping(path = "{machineIdentifier}/{key}")
    @ResponseBody
    public ResponseEntity<Payload> getLibraries(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getLibraries( " + machineIdentifier + ", " + key + " )");

        List<Movie> movies = ioService.readOwnedMovies(machineIdentifier, key);
        Payload payload;

        if (CollectionUtils.isEmpty(movies)) {
            payload = Payload.PLEX_LIBRARY_MOVIE_NOT_FOUND;
            LOGGER.warn(payload.getReason());
        } else {
            payload = Payload.PLEX_LIBRARY_MOVIE_FOUND;
        }

        payload.setExtras(movies);

        return ResponseEntity.ok().body(payload);
    }

}
