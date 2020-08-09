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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final GapsSearch gapsSearch;

    @Autowired
    SearchController(GapsSearch gapsSearch) {
        this.gapsSearch = gapsSearch;
    }

    @MessageMapping("/cancelSearching")
    public void cancelSearching() {
        LOGGER.info("cancelSearching()");
        gapsSearch.cancelSearch();
    }

    /**
     * Main REST call to start Gaps searching for missing movies
     *
     * @deprecated No long used to do search
     */
    @PostMapping(value = "startSearching")
    @ResponseStatus(value = HttpStatus.OK)
    @Deprecated(forRemoval = true)
    public void postStartSearching() {
        LOGGER.info("postStartSearching()");
        LOGGER.warn("Deprecated Method");

        throw new IllegalStateException("Need to pass in machineIdentifier and plex key");
    }

    @GetMapping(value = "/isSearching",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IsSearching> getIsSearching() {
        LOGGER.info("getIsSearching()");

        IsSearching isSearching = new IsSearching(gapsSearch.isSearching());
        return ResponseEntity.ok().body(isSearching);
    }

    public static class IsSearching {
        private final Boolean isSearching;

        public IsSearching(Boolean isSearching) {
            this.isSearching = isSearching;
        }

        public Boolean getIsSearching() {
            return isSearching;
        }
    }
}
