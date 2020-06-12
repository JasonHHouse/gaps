package com.jasonhhouse.gaps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/collections")
public class CollectionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionsController.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCollections() {
        LOGGER.info("getCollections()");
        return new ModelAndView("collections");
    }

    @RequestMapping(value = "/actors", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getActors() {
        LOGGER.info("getActors()");
        return new ModelAndView("collections-actors");
    }

    @RequestMapping(value = "/directors", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getDirectors() {
        LOGGER.info("getDirectors()");
        return new ModelAndView("collections-directors");
    }
}