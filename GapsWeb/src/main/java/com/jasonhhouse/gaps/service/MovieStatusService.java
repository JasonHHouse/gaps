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
