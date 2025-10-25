/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.service.GapsSearch;
import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import com.jasonhhouse.plex.libs.PlexLibrary;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/recommended")
public class RecommendedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendedController.class);

    private final FileIoService fileIoService;
    private final GapsSearch gapsSearch;

    @Autowired
    public RecommendedController(FileIoService fileIoService, GapsSearch gapsSearch) {
        this.fileIoService = fileIoService;
        this.gapsSearch = gapsSearch;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecommended() {
        LOGGER.info("getRecommended()");

        PlexProperties plexProperties = fileIoService.readProperties();
        PlexServer plexServer;
        PlexLibrary plexLibrary;

        if (CollectionUtils.isNotEmpty(plexProperties.getPlexServers())) {
            //Read first plex servers movies
            plexServer = plexProperties.getPlexServers().stream().findFirst().orElse(new PlexServer());
            plexLibrary = plexServer.getPlexLibraries().stream().findFirst().orElse(new PlexLibrary());
        } else {
            plexServer = new PlexServer();
            plexLibrary = new PlexLibrary();
        }

        Map<String, PlexServer> plexServerMap = plexProperties.getPlexServers().stream().collect(Collectors.toMap(PlexServer::getMachineIdentifier, Function.identity()));

        ModelAndView modelAndView = new ModelAndView("recommended");
        modelAndView.addObject("plexServers", plexServerMap);
        modelAndView.addObject("plexProperties", plexProperties);
        modelAndView.addObject("plexServer", plexServer);
        modelAndView.addObject("plexLibrary", plexLibrary);
        modelAndView.addObject("recommendedPage", true);
        return modelAndView;
    }

    @GetMapping(path = "{machineIdentifier}/{key}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> getRecommended(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getRecommended( {}, {} )", machineIdentifier, key);

        final List<BasicMovie> ownedBasicMovies = fileIoService.readOwnedMovies(machineIdentifier, key);
        Payload payload;

        if (CollectionUtils.isEmpty(ownedBasicMovies)) {
            payload = Payload.PLEX_LIBRARY_MOVIE_NOT_FOUND;
            LOGGER.warn(payload.getReason());
        } else {
            List<BasicMovie> basicMovies = fileIoService.readRecommendedMovies(machineIdentifier, key);
            if (CollectionUtils.isEmpty(basicMovies)) {
                payload = Payload.RECOMMENDED_MOVIES_NOT_FOUND;
                LOGGER.warn(payload.getReason());
            } else {
                payload = Payload.RECOMMENDED_MOVIES_FOUND;
            }
            payload.setExtras(basicMovies);
        }

        return ResponseEntity.ok().body(payload);
    }

    /**
     * Start Gaps searching for missing movies
     *
     * @param machineIdentifier plex server id
     * @param key               plex library key
     */
    @PutMapping(value = "/find/{machineIdentifier}/{key}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void putFindRecommencedMovies(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("putFindRecommencedMovies( {}, {} )", machineIdentifier, key);

        gapsSearch.run(machineIdentifier, key);
    }

    /**
     * Cancel Gaps searching for missing movies
     *
     * @param machineIdentifier plex server id
     * @param key               plex library key
     */
    @MessageMapping("/cancel/{machineIdentifier}/{key}")
    public void cancelSearching(@DestinationVariable final String machineIdentifier, @DestinationVariable final Integer key) {
        LOGGER.info("cancelSearching( {}, {} )", machineIdentifier, key);
        gapsSearch.cancelSearch();
    }


}
