/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Schedule {

    HOURLY("Hourly", "0 18 * * * *", 0),
    DAILY_4AM("Daily", "0 0 4 * * ?", 1),
    EVERY_MONDAY("Weekly", "0 4 ? * MON *", 2),
    EVERY_TWO_WEEKS("Bi-weekly", "0 4 1,15 * ? *", 3),
    EVERY_MONTH("Monthly", "0 0 4 1 * ?", 4);

    @NotNull
    private final String message;

    @NotNull
    private final String cron;

    @NotNull
    private final Integer id;

    Schedule(@NotNull String message, @NotNull String cron, @NotNull Integer id) {
        this.message = message;
        this.cron = cron;
        this.id = id;
    }

    public static Schedule getSchedule(@NotNull Integer id) {
        if (HOURLY.getId().equals(id)) {
            return HOURLY;
        }else if (DAILY_4AM.getId().equals(id)) {
            return DAILY_4AM;
        } else if (EVERY_MONDAY.getId().equals(id)) {
            return EVERY_MONDAY;
        } else if (EVERY_TWO_WEEKS.getId().equals(id)) {
            return EVERY_TWO_WEEKS;
        } else {
            return EVERY_MONTH;
        }
    }

    public static List<Schedule> getAllSchedules() {
        return new ArrayList<>() {{
            add(HOURLY);
            add(DAILY_4AM);
            add(EVERY_MONDAY);
            add(EVERY_TWO_WEEKS);
            add(EVERY_MONTH);
        }};
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public String getCron() {
        return cron;
    }

    @NotNull
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "message='" + message + '\'' +
                ", cron='" + cron + '\'' +
                ", id=" + id +
                '}';
    }
}
