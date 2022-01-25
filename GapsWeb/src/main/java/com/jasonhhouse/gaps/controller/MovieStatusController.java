package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.MovieStatusPayload;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.service.MovieStatusService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/movieStatus")
public class MovieStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieStatusController.class);

    private final MovieStatusService movieStatusService;

    @Autowired
    public MovieStatusController(MovieStatusService movieStatusService) {
        this.movieStatusService = movieStatusService;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payload> putMovieStatus(@RequestBody final MovieStatusPayload movieStatusPayload) {
        LOGGER.info("putMovieStatus( {} )", movieStatusPayload);

        try {
            movieStatusService.setMovieStatus(movieStatusPayload);
        } catch (Exception e) {
            LOGGER.error("Failed to update Movie Status", e);
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Payload.MOVIE_STATUS_NOT_UPDATED);
        }

        return ResponseEntity.ok().body(Payload.MOVIE_STATUS_UPDATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSchedule() {
        LOGGER.info("getSchedule()");
        try {
            return ResponseEntity.ok().body(movieStatusService.getJsonMovieStatus());
        } catch (IOException e) {
            LOGGER.error("Failed to parse schedule into JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to parse schedule into JSON");
        }
    }
}
