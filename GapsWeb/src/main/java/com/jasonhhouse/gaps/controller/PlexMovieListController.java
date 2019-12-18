package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexSearchFormatter;
import com.jasonhhouse.gaps.PlexService;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/plexMovieList")
public class PlexMovieListController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexMovieListController.class);

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
    public ModelAndView postPlexMovieList(@Valid @ModelAttribute("plexSearch") PlexSearch plexSearch, BindingResult bindingResult) {
        LOGGER.info("postPlexMovieList( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        List<PlexLibrary> plexLibraries = plexService.queryPlexLibraries(gapsService.getPlexSearch());
        gapsService.updateLibrarySelections(plexLibraries);
        gapsService.updatePlexSearch(plexSearch);

        ModelAndView modelAndView = new ModelAndView("plexMovieList");
        LOGGER.info(gapsService.getPlexSearch().toString());
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPlexMovieList() {
        LOGGER.info("getPlexMovieList()");
        return new ModelAndView("plexMovieList");
    }

}
