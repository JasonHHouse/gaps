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

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
            value = "/home",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndexOnClick() {
        LOGGER.info("getIndexOnClick()");

        PlexSearch plexSearch = null;
        try {
            plexSearch = ioService.readProperties();
            plexSearch.getPlexServers().addAll(ioService.readPlexConfiguration());
            gapsService.updatePlexSearch(plexSearch);
        } catch (IOException e) {
            LOGGER.warn("Failed to read gaps properties.", e);
        }

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        LOGGER.info("getIndex()");

        PlexSearch plexSearch = null;
        try {
            plexSearch = ioService.readProperties();
            plexSearch.getPlexServers().addAll(ioService.readPlexConfiguration());
            gapsService.updatePlexSearch(plexSearch);
        } catch (IOException e) {
            LOGGER.warn("Failed to read gaps properties.", e);
        }

        //If configuration is filled in, jump to libraries page
        if(plexSearch != null && StringUtils.isNotEmpty(plexSearch.getMovieDbApiKey()) && CollectionUtils.isNotEmpty(plexSearch.getPlexServers())) {
            return new ModelAndView("redirect:/libraries");
        }



        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.PUT,
            value = "/nuke/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> putNuke(@PathVariable("username") final String username) {
        LOGGER.info("putNuke( " + username + " )");
        LOGGER.info("Deleting all local files");
        Payload payload = ioService.nuke();
        if (payload.getCode() == Payload.NUKE_SUCCESSFUL.getCode()) {
            gapsService.nukePlexSearch();
        }
        return ResponseEntity.ok().body(payload);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/about",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAbout() {
        LOGGER.info("getAbout()");

        return new ModelAndView("about");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
    }
}
