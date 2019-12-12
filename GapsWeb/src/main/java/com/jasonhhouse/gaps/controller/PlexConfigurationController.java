package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/plexConfiguration")
public class PlexConfigurationController {

    private final Logger logger = LoggerFactory.getLogger(PlexConfigurationController.class);

    private final BindingErrorsService bindingErrorsService;

    public PlexConfigurationController(BindingErrorsService bindingErrorsService) {
        this.bindingErrorsService = bindingErrorsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexConfiguration(PlexSearch plexSearch,  BindingResult bindingResult) {
        logger.info("postPlexConfiguration( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        ModelAndView modelAndView = new ModelAndView("plexConfiguration");
        modelAndView.addObject("plexSearch", plexSearch);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPlexConfiguration() {
        logger.info("getPlexConfiguration()");
        return new ModelAndView("plexConfiguration");
    }
}
