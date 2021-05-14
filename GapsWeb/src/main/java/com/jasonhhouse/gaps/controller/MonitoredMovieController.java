package com.jasonhhouse.gaps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.MonitoredMovie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.service.MonitoredMovieService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/monitoredMovie")
public class MonitoredMovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoredMovieController.class);

    private final MonitoredMovieService monitoredMovieService;

    @Autowired
    public MonitoredMovieController(MonitoredMovieService monitoredMovieService) {
        this.monitoredMovieService = monitoredMovieService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMonitoredMovieMap() {
        LOGGER.info("getMonitoredMovieMap()");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(monitoredMovieService.map());
        } catch (IOException e) {
            LOGGER.error("Failed to read monitored movies", e);
        }
        return "{}";
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payload> setMonitoredMovie(@RequestBody MonitoredMovie monitoredMovie) {
        LOGGER.info("setMonitoredMovie( {} )", monitoredMovie);
        Payload payload;

        try {
            monitoredMovieService.save(monitoredMovie);
            payload = Payload.MONITORED_MOVIE_UPDATE_SUCCEEDED;
        } catch (Exception e) {
            payload = Payload.MONITORED_MOVIE_UPDATE_FAILED;
        }

        return ResponseEntity.ok().body(payload);
    }
}
