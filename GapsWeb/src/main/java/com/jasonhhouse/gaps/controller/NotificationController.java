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
import com.jasonhhouse.gaps.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/notifications")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/test/all")
    @ResponseBody
    public ResponseEntity<Payload> putTestAll() {
        LOGGER.info("putTestAll()");

        try {
            notificationService.test();
            LOGGER.info("Notification Test All Succeeded");
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error("Notification Test All Failed", e);
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/test/{id}")
    @ResponseBody
    public ResponseEntity<Payload> putTest(@PathVariable("id") final Integer id) {
        LOGGER.info("putTest( {} )", id);

        try {
            notificationService.test(id);
            LOGGER.info("Notification Test with ID {} Succeeded", id);
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(String.format("Notification Test Failed with ID %s", id), e);
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED.setExtras(e.getMessage()));
        }
    }
}
