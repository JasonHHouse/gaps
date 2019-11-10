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

import com.jasonhhouse.gaps.GapsSearch;
import com.jasonhhouse.gaps.PlexLibrary;
import java.util.Set;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Handles REST and WebSocket calls for Gaps.
 */
@Controller
public class PlexController {

    private final Logger logger = LoggerFactory.getLogger(PlexController.class);

    private final GapsSearch gapsSearch;

    @Autowired
    PlexController(GapsSearch gapsSearch) {
        this.gapsSearch = gapsSearch;
    }

    /**
     * Searches Plex for the "Movie" libraries it can find
     *
     * @param address Host name of the machine to connect to Plex on
     * @param port    Port Plex runs on
     * @param token   User specific Plex token
     * @return List of PlexLibraries found
     */
    @RequestMapping(value = "getPlexLibraries", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<PlexLibrary>> getPlexLibraries(@RequestParam("address") String address,
                                                             @RequestParam("port") int port,
                                                             @RequestParam("token") String token) {
        logger.info("getPlexLibraries()");

        if (StringUtils.isEmpty(token)) {
            String reason = "Plex token cannot be empty";
            logger.error(reason);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason);
        }

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(address)
                .port(port)
                .addPathSegment("library")
                .addPathSegment("sections")
                .addQueryParameter("X-Plex-Token", token)
                .build();

        Set<PlexLibrary> plexLibraries = gapsSearch.getPlexLibraries(url);
        return new ResponseEntity<>(plexLibraries, HttpStatus.OK);
    }

}
