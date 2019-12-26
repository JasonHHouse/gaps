package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexSearchFormatter;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import com.jasonhhouse.gaps.validator.TmdbKeyValidator;
import java.io.IOException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/plexConfiguration")
public class PlexConfigurationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexConfigurationController.class);

    private final BindingErrorsService bindingErrorsService;
    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public PlexConfigurationController(BindingErrorsService bindingErrorsService, IoService ioService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexConfiguration(@Valid PlexSearch plexSearch, BindingResult bindingResult) {
        LOGGER.info("postPlexConfiguration( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        gapsService.updatePlexSearch(plexSearch);

        try {
            ioService.writeProperties(gapsService.getPlexSearch());
        } catch (IOException e) {
            LOGGER.warn("Could not write gaps properties.", e);
        }

        ModelAndView modelAndView = new ModelAndView("plexConfiguration");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPlexConfiguration() {
        LOGGER.info("getPlexConfiguration()");
        ModelAndView modelAndView = new ModelAndView("plexConfiguration");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @InitBinder("plexSearch")
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
        binder.addCustomFormatter(new PlexSearchFormatter(), "plexSearch");
        binder.setValidator(new TmdbKeyValidator());
    }
}
