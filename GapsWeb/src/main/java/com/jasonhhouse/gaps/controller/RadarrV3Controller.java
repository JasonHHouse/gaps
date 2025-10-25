/*
 *
 *  Copyright 2025 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.service.RadarrV3Service;
import com.jasonhhouse.radarr_v3.Movie;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/radarr/v3")
public class RadarrV3Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadarrV3Controller.class);

    private final RadarrV3Service radarrV3Service;

    public RadarrV3Controller(RadarrV3Service radarrV3Service) {
        this.radarrV3Service = radarrV3Service;
    }

    @GetMapping(value = "/movies/{address}/{port}/{apiKey}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Movie>> getPlexMovies(@PathVariable("address") final String address, @PathVariable("port") final Integer port, @PathVariable("apiKey") final String apiKey) {
        LOGGER.info("getPlexMovies( {}, {}, {} )", address, port, apiKey);

        List<Movie> movies = radarrV3Service.getMovies(address, port, apiKey);
        return ResponseEntity.ok().body(movies);
    }
}
