package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.PlexSearch;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/")
public class GapsController {

    private final Logger logger = LoggerFactory.getLogger(GapsController.class);

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() throws IOException {
        logger.info("getIndex()");
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("plexSearch", new PlexSearch());
        return modelAndView;
    }

}
