/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.Schedule;
import com.jasonhhouse.gaps.service.SchedulerService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedule")
public class SchedulerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);

    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{schedule}")
    @ResponseBody
    public ResponseEntity<Payload> putSchedule(@PathVariable("schedule") final Integer schedule) {
        LOGGER.info("putSchedule( {} )", schedule);

        try {
            schedulerService.setSchedule(schedule);
        } catch (IOException e) {
            LOGGER.error("Failed to get JSON Schedule", e);
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Payload.SCHEDULE_NOT_UPDATED);
        }

        return ResponseEntity.ok().body(Payload.SCHEDULE_UPDATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSchedule() {
        LOGGER.info("getSchedule()");
        try {
            return ResponseEntity.ok().body(schedulerService.getJsonSchedule());
        } catch (IOException e) {
            LOGGER.error("Failed to read schedule from properties", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read schedule from properties");
        }
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSchedules() {
        LOGGER.info("getAllSchedules()");
        try {
            return ResponseEntity.ok().body(schedulerService.getAllSchedules());
        } catch (IOException e) {
            LOGGER.error("Failed to read schedule from properties", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to parse schedule into JSON");
        }
    }
}
