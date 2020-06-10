package com.jasonhhouse.gaps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/collections")
public class CollectionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionsController.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAbout() {
        LOGGER.info("getAbout()");
        return new ModelAndView("collections");
    }
}