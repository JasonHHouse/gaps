package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/")
public class GapsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    private final GapsService gapsService;

    @Autowired
    public GapsController(GapsService gapsService) {
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        LOGGER.info("getIndex()");
        ModelAndView modelAndView = new ModelAndView("index");

        //ToDo
        //Remove for testing
        gapsService.getPlexSearch().setMovieDbApiKey("723b4c763114904392ca441909aa0375");

        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
    }
}
