package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Pair;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.service.MissLabeled;
import com.jasonhhouse.plex.MediaContainer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/missLabeled")
public class MissLabeledController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MissLabeledController.class);

    private final GapsService gapsService;
    private final PlexQuery plexQuery;
    private final MissLabeled missLabeled;

    @Autowired
    public MissLabeledController(GapsService gapsService, PlexQuery plexQuery, MissLabeled missLabeled) {
        this.gapsService = gapsService;
        this.plexQuery = plexQuery;
        this.missLabeled = missLabeled;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/{machineIdentifier}/{key}")
    @ResponseBody
    public ResponseEntity<MediaContainer> getPlexMovies(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getPlexMovies( " + machineIdentifier + ", " + key + " )");

        String url = generatePlexUrl(machineIdentifier, key);
        MediaContainer mediaContainer = plexQuery.findAllPlexVideos(url);
        return ResponseEntity.ok().body(mediaContainer);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/missMatched/{machineIdentifier}/{key}")
    @ResponseBody
    public ResponseEntity<List<Pair<String, Double>>> getMisMatched(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getPlexMovies( " + machineIdentifier + ", " + key + " )");

        String url = generatePlexUrl(machineIdentifier, key);
        MediaContainer mediaContainer = plexQuery.findAllPlexVideos(url);
        List<Pair<String, Double>> pairs = missLabeled.findMatchPercentage(mediaContainer);

        return ResponseEntity.ok().body(pairs);
    }

    private String generatePlexUrl(String machineIdentifier, Integer key) {
        LOGGER.info("generatePlexUrl( " + machineIdentifier + ", " + key + " )");
        return gapsService
                .getPlexSearch()
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
