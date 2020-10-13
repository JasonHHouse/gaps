/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.plex.PlexServer;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import com.jasonhhouse.gaps.service.RssService;
import com.jasonhhouse.gaps.plex.PlexLibrary;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RSSController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSSController.class);

    private final FileIoService fileIoService;
    private final RssService rssService;

    @Autowired
    public RSSController(FileIoService fileIoService, RssService rssService) {
        this.fileIoService = fileIoService;
        this.rssService = rssService;
    }

    @GetMapping(path = "/rss/{machineIdentifier}/{libraryKey}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRss(@PathVariable("machineIdentifier") String machineIdentifier, @PathVariable("libraryKey") Integer libraryKey) {
        LOGGER.info("getRss( {}, {} )", machineIdentifier, libraryKey);

        String rss = null;
        if (fileIoService.doesRssFileExist(machineIdentifier, libraryKey)) {
            rss = fileIoService.getRssFile(machineIdentifier, libraryKey);
        }

        LOGGER.info("rss:{}", rss);

        if (StringUtils.isEmpty(rss)) {
            //Show empty page
            LOGGER.warn("No RSS Found, didn't call from redirect");
            return "No RSS feed found.";
        } else {
            return rss;
        }
    }

    @GetMapping(path = "/rssCheck")
    public ModelAndView getRssCheck() {
        LOGGER.info("getRssCheck()");

        PlexProperties plexProperties = fileIoService.readProperties();
        ModelAndView modelAndView = new ModelAndView("rssCheck");
        Map<PlexLibrary, PlexServer> map = rssService.foundAnyRssFeeds();
        modelAndView.addObject("plexServers", plexProperties.getPlexServers());
        modelAndView.addObject("plexServerMap", map);
        modelAndView.addObject("foundPlexLibraries", MapUtils.isNotEmpty(map));
        return modelAndView;
    }
}