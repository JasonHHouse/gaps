/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.MovieStatus;
import com.jasonhhouse.gaps.MovieStatusPayload;
import com.jasonhhouse.gaps.properties.PlexProperties;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MovieStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieStatus.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FileIoService fileIoService;

    public MovieStatusService(FileIoService fileIoService) {
        this.fileIoService = fileIoService;
    }

    public void setMovieStatus(MovieStatusPayload movieStatusPayload) {
        LOGGER.info("setMovieStatus( {} )", movieStatusPayload);
        PlexProperties plexProperties = fileIoService.readProperties();
        MovieStatus movieStatus = MovieStatus.getMovieStatus(movieStatusPayload.getStatus());
        plexProperties.setMovieStatus(movieStatus);
        fileIoService.writeProperties(plexProperties);
    }

    public List<MovieStatus> getAllMovieStatuses() {
        LOGGER.info("getAllMovieStatus()");
        return MovieStatus.getAllMovieStatuses();
    }

    public MovieStatus getRawMovieStatus() {
        LOGGER.info("getRawMovieStatus()");
        return fileIoService.readProperties().getMovieStatus();
    }

    public String getJsonMovieStatus() throws IOException {
        LOGGER.info("getJsonMovieStatus()");
        return objectMapper.writeValueAsString(fileIoService.readProperties().getMovieStatus());
    }
}
