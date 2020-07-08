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
import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Schedule;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public SchedulerService(IoService ioService, GapsService gapsService) {
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    public void setSchedule(int intSchedule) {
        Schedule schedule = Schedule.getSchedule(intSchedule);
        gapsService.getPlexSearch().setSchedule(schedule);
    }

    public Schedule getRawSchedule() throws IOException {
        return ioService.readProperties().getSchedule();
    }

    public String getJsonSchedule() throws IOException {
        JsonSchedule jsonSchedule = new JsonSchedule(ioService.readProperties().getSchedule().ordinal());
        return objectMapper.writeValueAsString(jsonSchedule);
    }

    private final class JsonSchedule {
        private int scheduleOrdinal;

        public JsonSchedule(int scheduleOrdinal) {
            this.scheduleOrdinal = scheduleOrdinal;
        }

        public int getScheduleOrdinal() {
            return scheduleOrdinal;
        }

        public void setScheduleOrdinal(int scheduleOrdinal) {
            this.scheduleOrdinal = scheduleOrdinal;
        }
    }
}
