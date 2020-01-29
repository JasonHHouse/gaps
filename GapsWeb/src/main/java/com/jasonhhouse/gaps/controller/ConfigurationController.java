package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import com.jasonhhouse.gaps.service.PlexQueryImpl;
import com.jasonhhouse.gaps.service.TmdbService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/configuration")
public class ConfigurationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

    private final BindingErrorsService bindingErrorsService;
    private final TmdbService tmdbService;
    private final SimpMessagingTemplate template;
    private final PlexQueryImpl plexQuery;
    private final GapsService gapsService;
    private final IoService ioService;

    public ConfigurationController(BindingErrorsService bindingErrorsService, TmdbService tmdbService, SimpMessagingTemplate template, PlexQueryImpl plexQuery, GapsService gapsService, IoService ioService) {
        this.bindingErrorsService = bindingErrorsService;
        this.tmdbService = tmdbService;
        this.template = template;
        this.plexQuery = plexQuery;
        this.gapsService = gapsService;
        this.ioService = ioService;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getConfiguration() {
        LOGGER.info("getConfiguration()");

        try {
            PlexSearch plexSearch = ioService.readProperties();
            gapsService.updatePlexSearch(plexSearch);

            List<PlexServer> plexServers = ioService.readPlexConfiguration();
            plexSearch.getPlexServers().addAll(plexServers);
        } catch (IOException e) {
            LOGGER.warn("Failed to read gaps properties.", e);
        }

        ModelAndView modelAndView = new ModelAndView("configuration");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(value = "/plex",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void postPlexServer(@Valid final PlexServer plexServer, BindingResult bindingResult) {
        LOGGER.info("postPlexLibraries( " + plexServer + " )");

/*
        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            LOGGER.error("Error binding PlexServer object: " + plexServer);
        }
*/
        plexQuery.queryPlexServer(plexServer);
        plexQuery.getLibraries(plexServer);
        gapsService.getPlexSearch().addPlexServer(plexServer);
        ioService.writePlexConfiguration(gapsService.getPlexSearch().getPlexServers());

        template.convertAndSend("/configuration/plex/complete", plexServer);
    }

    @RequestMapping(value = "/testTmdbKey/{tmdbKey}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postTestTmdbKey(@PathVariable("tmdbKey") final String tmdbKey) {
        LOGGER.info("postTestTmdbKey( " + tmdbKey + " )");

        String tmdbResponse = tmdbService.testTmdbKey(tmdbKey);
        return ResponseEntity.ok().body(tmdbResponse);
    }
}
