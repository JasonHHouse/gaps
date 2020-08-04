/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.controller.GapsController;
import com.jasonhhouse.gaps.service.SchedulerService;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> searchAgain;

    private SchedulerService schedulerService;

    public SchedulerConfig(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();

        Schedule schedule;
        try {
            schedule = schedulerService.getRawSchedule();
        } catch (IOException e) {
            LOGGER.error("Error reading schedule. Defaulting to daily.", e);
            schedule = Schedule.DAILY_4AM;
        }

        setSearchSchedule(threadPoolTaskScheduler, schedule.getCron());
        this.taskScheduler = threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    private void setSearchSchedule(TaskScheduler scheduler, String cronExp) {
        searchAgain = scheduler.schedule(() -> {
            LOGGER.info(Thread.currentThread().getName() + " The scheduled search executed at " + new Date());
            //ToDo start gaps search

        }, triggerContext -> new CronTrigger(cronExp).nextExecutionTime(triggerContext));
    }

    public void refreshCronSchedule(String cronExp) {
        if (searchAgain != null) {
            searchAgain.cancel(true);
            setSearchSchedule(taskScheduler, cronExp);
        }
    }
}
