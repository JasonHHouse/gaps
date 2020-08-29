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

import com.jasonhhouse.gaps.Mislabeled;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.IoService;
import com.jasonhhouse.gaps.service.MediaContainerService;
import com.jasonhhouse.gaps.service.MislabeledService;
import com.jasonhhouse.plex.video.MediaContainer;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/mislabeled")
public class MislabeledController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MislabeledController.class);

    private final IoService ioService;
    private final PlexQuery plexQuery;
    private final MislabeledService mislabeledService;
    private final MediaContainerService mediaContainerService;

    @Autowired
    public MislabeledController(IoService ioService, PlexQuery plexQuery, MislabeledService mislabeledService, MediaContainerService mediaContainerService) {
        this.ioService = ioService;
        this.plexQuery = plexQuery;
        this.mislabeledService = mislabeledService;
        this.mediaContainerService = mediaContainerService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMislabeled() {
        LOGGER.info("getMislabeled()");

        return new ModelAndView("mislabeled");
    }

    @GetMapping(value = "/{machineIdentifier}/{key}")
    @ResponseBody
    public ResponseEntity<String> getMisMatched(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getPlexMovies( {}, {} )", machineIdentifier, key);

        StopWatch watch = new StopWatch();
        watch.start();

        PlexProperties plexProperties = ioService.readProperties();

        String url = generatePlexUrl(plexProperties, machineIdentifier, key);
        MediaContainer mediaContainer = plexQuery.findAllPlexVideos(url);
        mediaContainerService.deleteAll();
        mediaContainerService.save(mediaContainer);

        watch.stop();
        LOGGER.info("SQLite3 Update {}", watch.getNanoTime());

        return ResponseEntity.ok().body("Success");
    }

    @GetMapping(value = "/{machineIdentifier}/{key}/{percentage}")
    @ResponseBody
    public ResponseEntity<List<Mislabeled>> getMisMatched(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key,
                                                          @PathVariable("percentage") final Double percentage) {
        LOGGER.info("getPlexMovies( {}, {}, {} )", machineIdentifier, key, percentage);

        StopWatch watch = new StopWatch();
        watch.start();

        //ToDo Hardcoded
        MediaContainer mediaContainer = mediaContainerService.list().get(0);
        mediaContainerService.save(mediaContainer);
        List<Mislabeled> mislabeled = mislabeledService.findMatchPercentage(mediaContainer, percentage);

        watch.stop();
        LOGGER.info("SQLite3 Find All {}", watch.getNanoTime());

        return ResponseEntity.ok().body(mislabeled);
    }

    @GetMapping(value = "/plex/{machineIdentifier}/{key}/{percentage}")
    @ResponseBody
    public ResponseEntity<List<Mislabeled>> getPlexMisMatched(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key,
                                                              @PathVariable("percentage") final Double percentage) {
        LOGGER.info("getPlexMovies( {}, {}, {} )", machineIdentifier, key, percentage);

        StopWatch watch = new StopWatch();
        watch.start();

        PlexProperties plexProperties = ioService.readProperties();
        String url = generatePlexUrl(plexProperties, machineIdentifier, key);
        MediaContainer mediaContainer = plexQuery.findAllPlexVideos(url);
        List<Mislabeled> mislabeled = mislabeledService.findMatchPercentage(mediaContainer, percentage);

        watch.stop();
        LOGGER.info("Plex Runtime {}", watch.getNanoTime());

        return ResponseEntity.ok().body(mislabeled);
    }

    private String generatePlexUrl(PlexProperties plexProperties, String machineIdentifier, Integer key) {
        LOGGER.info("generatePlexUrl( {}, {} )", machineIdentifier, key);
        return plexProperties
                .getPlexServers()
                .stream()
                .filter(plexServer -> plexServer.getMachineIdentifier().equals(machineIdentifier))
                .map(plexServer -> plexServer
                        .getPlexLibaries()
                        .stream()
                        .filter(directoryType -> directoryType.getKey().equals(key))
                        .map(plexLibrary -> "http://" + plexServer.getAddress() + ":" + plexServer.getPort() + "/library/sections/" + plexLibrary.getKey() + "/all/?X-Plex-Token=" + plexServer.getPlexToken())
                        .findFirst().orElse(null))
                .findFirst()
                .orElse("");
    }
}