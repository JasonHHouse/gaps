/*
 *
 *  Copyright 2025 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.GapsUrlGenerator;
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
    private final FileIoService fileIoService;
    private final TaskScheduler scheduler;
    private final SearchGapsTask searchGapsTask;
    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    public SchedulerService(FileIoService fileIoService, TmdbService tmdbService, GapsSearch gapsSearch, @Qualifier("Gaps") TaskScheduler scheduler, PlexQuery plexQuery, GapsUrlGenerator gapsUrlGenerator, NotificationService notificationService) {
        this.fileIoService = fileIoService;
        this.scheduler = scheduler;
        this.searchGapsTask = new SearchGapsTask(gapsSearch, tmdbService, fileIoService, plexQuery, gapsUrlGenerator, notificationService);
    }

    public void setSchedule(SchedulePayload schedulePayload) {
        LOGGER.info("setSchedule( {} )", schedulePayload);
        PlexProperties plexProperties = fileIoService.readProperties();
        Schedule schedule = Schedule.getSchedule(schedulePayload.getSchedule());
        schedule.setEnabled(schedulePayload.getEnabled());
        plexProperties.setSchedule(schedule);
        fileIoService.writeProperties(plexProperties);
        setTaskForScheduler(schedule);
    }

    public Schedule getRawSchedule() {
        LOGGER.info("getRawSchedule()");
        return fileIoService.readProperties().getSchedule();
    }

    public List<Schedule> getAllSchedules() {
        LOGGER.info("getAllSchedules()");
        return Schedule.getAllSchedules();
    }

    public String getJsonSchedule() throws IOException {
        LOGGER.info("getJsonSchedule()");
        return objectMapper.writeValueAsString(fileIoService.readProperties().getSchedule());
    }

    public void test() {
        LOGGER.info("test()");
        searchGapsTask.run();
    }

    private void setTaskForScheduler(Schedule schedule) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            LOGGER.info("ScheduledFuture is cancelled {}", scheduledFuture.isCancelled());
        }

        if (!schedule.getEnabled()) {
            LOGGER.warn("Schedule not enabled");
            return;
        }

        LOGGER.info("Setting schedule for {} as cron '{}'", schedule.getMessage(), schedule.getCron());
        LOGGER.info("Cron TimeZone {}", TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        scheduledFuture = scheduler.schedule(searchGapsTask, new CronTrigger(schedule.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));
    }

    // A context refresh event listener
    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent() {
        // Get all tasks from DB and reschedule them in case of context restarted
    }

}
