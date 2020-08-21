/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.GapsSearch;
import com.jasonhhouse.gaps.GapsUrlGenerator;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.Schedule;
import com.jasonhhouse.gaps.SchedulePayload;
import com.jasonhhouse.gaps.SearchGapsTask;
import com.jasonhhouse.gaps.properties.PlexProperties;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final IoService ioService;
    private final TaskScheduler scheduler;
    private final SearchGapsTask searchGapsTask;
    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    public SchedulerService(IoService ioService, TmdbService tmdbService, GapsSearch gapsSearch, @Qualifier("Gaps") TaskScheduler scheduler, PlexQuery plexQuery, GapsUrlGenerator gapsUrlGenerator, NotificationService notificationService) {
        this.ioService = ioService;
        this.scheduler = scheduler;
        this.searchGapsTask = new SearchGapsTask(gapsSearch, tmdbService, ioService, plexQuery, gapsUrlGenerator, notificationService);
    }

    public void setSchedule(SchedulePayload schedulePayload) throws IOException {
        LOGGER.info("setSchedule( {} )", schedulePayload);
        PlexProperties plexProperties = ioService.readProperties();
        Schedule schedule = Schedule.getSchedule(schedulePayload.getSchedule());
        schedule.setEnabled(schedulePayload.getEnabled());
        plexProperties.setSchedule(schedule);
        ioService.writeProperties(plexProperties);
        setTaskForScheduler(schedule);
    }

    public Schedule getRawSchedule() throws IOException {
        LOGGER.info("getRawSchedule()");
        return ioService.readProperties().getSchedule();
    }

    public List<Schedule> getAllSchedules() {
        LOGGER.info("getAllSchedules()");
        return Schedule.getAllSchedules();
    }

    public String getJsonSchedule() throws IOException {
        LOGGER.info("getJsonSchedule()");
        return objectMapper.writeValueAsString(ioService.readProperties().getSchedule());
    }

    private void setTaskForScheduler(Schedule schedule) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }

        if (!schedule.getEnabled()) {
            LOGGER.info("Schedule not enabled");
            return;
        }

        scheduledFuture = scheduler.schedule(searchGapsTask, new CronTrigger(schedule.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));
    }

    // A context refresh event listener
    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent() {
        // Get all tasks from DB and reschedule them in case of context restarted
    }

}
