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
@RequestMapping(value = "/plexMovieList")
public class PlexMovieListController {
    private final Logger logger = LoggerFactory.getLogger(PlexMovieListController.class);

    private final BindingErrorsService bindingErrorsService;
    private final PlexService plexService;
    private final GapsService gapsService;

    @Autowired
    public PlexMovieListController(BindingErrorsService bindingErrorsService, PlexService plexService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.plexService = plexService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexMovieList(PlexSearch plexSearch, BindingResult bindingResult) {
        logger.info("postPlexLibraries( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        Set<PlexLibrary> plexLibraries = plexService.getPlexLibraries(plexSearch);
        gapsService.copyInLibraries(plexLibraries);

        //ToDo
        //Make the search for plex libs and copy that in here

        ModelAndView modelAndView = new ModelAndView("plexMovieList");
        modelAndView.addObject("plexSearch", plexSearch);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexMovieList() {
        logger.info("getPlexLibraries()");
        return new ModelAndView("plexLibraries");
    }
}
