package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexService;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/plexLibraries")
public class PlexLibrariesController {
    private final Logger logger = LoggerFactory.getLogger(PlexLibrariesController.class);

    private final BindingErrorsService bindingErrorsService;
    private final PlexService plexService;
    private final GapsService gapsService;

    @Autowired
    public PlexLibrariesController(BindingErrorsService bindingErrorsService, PlexService plexService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.plexService = plexService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexLibraries(PlexSearch plexSearch, BindingResult bindingResult) {
        logger.info("postPlexLibraries( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        gapsService.updatePlexSearch(plexSearch);

        Set<PlexLibrary> plexLibraries = plexService.queryPlexLibraries(plexSearch);
        gapsService.copyInLibraries(plexLibraries);

        logger.info(gapsService.getPlexSearch().toString());

        ModelAndView modelAndView = new ModelAndView("plexLibraries");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPlexLibraries() {
        logger.info("getPlexLibraries()");
        ModelAndView modelAndView = new ModelAndView("plexLibraries");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }
}
