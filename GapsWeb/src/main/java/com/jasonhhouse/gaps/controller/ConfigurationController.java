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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import com.jasonhhouse.gaps.service.MovieStatusService;
import com.jasonhhouse.gaps.service.PlexQueryImpl;
import com.jasonhhouse.gaps.service.SchedulerService;
import com.jasonhhouse.gaps.service.TmdbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/configuration")
public class ConfigurationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TMDB_KEY = "tmdbKey:";
    private static final String SUCCESS = "success";
    private static final String CONFIGURATION_PLEX = "/configuration/plex";
    private static final String CONFIGURATION_PLEX_COMPLETE = CONFIGURATION_PLEX + "/complete";

    @NotNull
    private final TmdbService tmdbService;
    @NotNull
    private final SimpMessagingTemplate template;
    @NotNull
    private final PlexQueryImpl plexQuery;
    @NotNull
    private final FileIoService fileIoService;
    @NotNull
    private final SchedulerService schedulerService;
    @NotNull
    private final MovieStatusService movieStatusService;

    public ConfigurationController(@NotNull TmdbService tmdbService,
                                   @NotNull SimpMessagingTemplate template,
                                   @NotNull PlexQueryImpl plexQuery,
                                   @NotNull FileIoService fileIoService,
                                   @NotNull SchedulerService schedulerService,
                                   @NotNull MovieStatusService movieStatusService) {
        this.tmdbService = tmdbService;
        this.template = template;
        this.plexQuery = plexQuery;
        this.fileIoService = fileIoService;
        this.schedulerService = schedulerService;
        this.movieStatusService = movieStatusService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getConfiguration() {
        LOGGER.info("getConfiguration()");

        PlexProperties plexProperties = fileIoService.readProperties();
        ModelAndView modelAndView = new ModelAndView("configuration");
        modelAndView.addObject("plexProperties", plexProperties);
        modelAndView.addObject("schedules", schedulerService.getAllSchedules());
        modelAndView.addObject("movieStatuses", movieStatusService.getAllMovieStatuses());
        modelAndView.addObject("configurationPage", true);
        return modelAndView;
    }

    @Operation(summary = "Adds new Plex Server")
    @PostMapping(value = "/add/plex",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void postAddPlexServer(@Parameter(description = "The details of the Plex Server to add.") @Valid final PlexServer plexServer) {
        LOGGER.info("postAddPlexServer( {} )", plexServer);

        PlexProperties plexProperties = fileIoService.readProperties();

        try {
            plexQuery.queryPlexServer(plexServer);
            Payload payload = plexQuery.getLibraries(plexServer);

            if (payload.getCode() == Payload.PLEX_LIBRARIES_FOUND.getCode()) {
                if (plexProperties.getPlexServers().contains(plexServer)) {
                    template.convertAndSend(CONFIGURATION_PLEX + "/duplicate", Payload.DUPLICATE_PLEX_LIBRARY);
                } else {
                    plexProperties.addPlexServer(plexServer);
                    fileIoService.writeProperties(plexProperties);
                    template.convertAndSend(CONFIGURATION_PLEX_COMPLETE, payload.setExtras(plexServer));
                }
            } else {
                template.convertAndSend(CONFIGURATION_PLEX_COMPLETE, payload);
            }
        } catch (Exception e) {
            LOGGER.error("Could not add plex server", e);
            template.convertAndSend(CONFIGURATION_PLEX_COMPLETE, Payload.UNKNOWN_ERROR);
        }
    }

    @Operation(summary = "Tests connection to a single Plex Server")
    @PutMapping(value = "/test/plex",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> putTestPlexServer(@Valid final PlexServer plexServer, BindingResult bindingResult) {
        LOGGER.info("putTestPlexServer( {} )", plexServer);

        Payload payload;
        try {
            payload = plexQuery.queryPlexServer(plexServer);
        } catch (Exception e) {
            payload = Payload.UNKNOWN_ERROR;
        }

        return ResponseEntity.ok().body(payload);
    }

    @Operation(summary = "Tests connection to a single Plex Server")
    @PutMapping(value = "/test/plex/{machineIdentifier}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> putTestPlexServerByMachineId(@PathVariable("machineIdentifier") final String machineIdentifier) {
        LOGGER.info("putTestPlexServerByMachineId( {} )", machineIdentifier);

        PlexProperties plexProperties = fileIoService.readProperties();

        ObjectNode objectNode = objectMapper.createObjectNode();
        PlexServer returnedPlexServer = plexProperties.getPlexServers().stream().filter(plexServer -> plexServer.getMachineIdentifier().equals(machineIdentifier)).findFirst().orElse(new PlexServer());

        if (StringUtils.isEmpty(returnedPlexServer.getMachineIdentifier())) {
            //Failed to find and delete
            objectNode.put(SUCCESS, false);
        } else {
            plexQuery.queryPlexServer(returnedPlexServer);
            objectNode.put(SUCCESS, true);
        }

        return ResponseEntity.ok().body(objectNode.toString());
    }

    @DeleteMapping(value = "/delete/plex/{machineIdentifier}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deletePlexServer(@PathVariable("machineIdentifier") final String machineIdentifier) {
        LOGGER.info("deletePlexServer( {} )", machineIdentifier);

        PlexProperties plexProperties = fileIoService.readProperties();

        ObjectNode objectNode = objectMapper.createObjectNode();
        PlexServer returnedPlexServer = plexProperties.getPlexServers().stream().filter(plexServer -> plexServer.getMachineIdentifier().equals(machineIdentifier)).findFirst().orElse(new PlexServer());
        if (StringUtils.isEmpty(returnedPlexServer.getMachineIdentifier())) {
            //Failed to find and delete
            objectNode.put(SUCCESS, false);
        } else {
            plexProperties.getPlexServers().remove(returnedPlexServer);
            fileIoService.writeProperties(plexProperties);
            objectNode.put(SUCCESS, true);
        }

        return ResponseEntity.ok().body(objectNode.toString());
    }

    @PutMapping(value = "/test/tmdbKey/{tmdbKey}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> postTestTmdbKey(@PathVariable("tmdbKey") final String tmdbKey) {
        LOGGER.info("postTestTmdbKey( {} )", tmdbKey);

        Payload payload = tmdbService.testTmdbKey(tmdbKey).setExtras(TMDB_KEY + tmdbKey);
        return ResponseEntity.ok().body(payload);
    }

    @PostMapping(value = "/save/tmdbKey/{tmdbKey}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> postSaveTmdbKey(@PathVariable("tmdbKey") final String tmdbKey) {
        LOGGER.info("postSaveTmdbKey( {} )", tmdbKey);

        PlexProperties plexProperties = fileIoService.readProperties();
        plexProperties.setMovieDbApiKey(tmdbKey);
        fileIoService.writeProperties(plexProperties);
        Payload payload = Payload.TMDB_KEY_SAVE_SUCCESSFUL.setExtras(TMDB_KEY + tmdbKey);
        return ResponseEntity.ok().body(payload);
    }

}
