/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexSearchFormatter;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import com.jasonhhouse.gaps.validator.PlexPropertiesValidator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/plexLibraries")
public class PlexLibrariesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexLibrariesController.class);

    private final BindingErrorsService bindingErrorsService;
    private final PlexQuery plexQuery;
    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public PlexLibrariesController(BindingErrorsService bindingErrorsService, PlexQuery plexQuery, IoService ioService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.plexQuery = plexQuery;
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postPlexLibraries(@Valid PlexSearch plexSearch, BindingResult bindingResult) {
        LOGGER.info("postPlexLibraries( " + plexSearch + " )");

        if (bindingErrorsService.hasBindingErrors(bindingResult)) {
            return bindingErrorsService.getErrorPage();
        }

        gapsService.updatePlexSearch(plexSearch);

        try {
            ioService.writeProperties(gapsService.getPlexSearch());
        } catch (IOException e) {
            LOGGER.warn("Could not write gaps properties.", e);
        }

        setPlexSearch();
        //List<PlexLibrary> plexLibraries = plexQuery.getLibraries(plexSearch);
        //gapsService.getPlexSearch().getLibraries().addAll(plexLibraries);

        ModelAndView modelAndView = new ModelAndView("plexLibraries");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPlexLibraries() {
        LOGGER.info("getPlexLibraries()");
        ModelAndView modelAndView = new ModelAndView("plexLibraries");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
        binder.addCustomFormatter(new PlexSearchFormatter(), "plexSearch");
        binder.setValidator(new PlexPropertiesValidator());
    }

    private void setPlexSearch() {
        String json = "[\n" +
                "  {\n" +
                "    \"key\": 23,\n" +
                "    \"title\": \"BM\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 22,\n" +
                "    \"title\": \"Family Videos\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 13,\n" +
                "    \"title\": \"Grandad Movies\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 2,\n" +
                "    \"title\": \"Movies\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 14,\n" +
                "    \"title\": \"Movies - 3D\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 11,\n" +
                "    \"title\": \"Movies - 4K\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 16,\n" +
                "    \"title\": \"Stand Up Comedy\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 6,\n" +
                "    \"title\": \"Workout Videos\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 9,\n" +
                "    \"title\": \"Bassnectar\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 10,\n" +
                "    \"title\": \"Halloween\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 20,\n" +
                "    \"title\": \"Home Theater Demos\",\n" +
                "    \"selected\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": 19,\n" +
                "    \"title\": \"Pre-Rolls\",\n" +
                "    \"selected\": false\n" +
                "  }\n" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<PlexLibrary> plexLibraries;
        try {
            plexLibraries = Arrays.asList(objectMapper.readValue(json, PlexLibrary[].class));
            gapsService.getPlexSearch().getLibraries().addAll(plexLibraries);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
