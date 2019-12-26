package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
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

    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public GapsController(IoService ioService, GapsService gapsService) {
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        LOGGER.info("getIndex()");
        ModelAndView modelAndView = new ModelAndView("index");

        try {
            PlexSearch plexSearch = ioService.readProperties();
            gapsService.updatePlexSearch(plexSearch);
        } catch (IOException e) {
            LOGGER.warn("Failed to read gaps properties.", e);
        }

        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
    }
}
