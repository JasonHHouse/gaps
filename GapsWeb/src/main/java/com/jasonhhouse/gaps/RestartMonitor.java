/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.service.SchedulerService;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RestartMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartMonitor.class);

    private final SchedulerService schedulerService;

    public RestartMonitor(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("init()");
        final Schedule schedule = schedulerService.getRawSchedule();
        if (schedule != null) {
            LOGGER.info("Setting saved schedule to cron job");
            final SchedulePayload schedulePayload = new SchedulePayload(schedule.getId(), schedule.getEnabled());
            schedulerService.setSchedule(schedulePayload);
        }
    }

}