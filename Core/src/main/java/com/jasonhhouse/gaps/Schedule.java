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

import org.jetbrains.annotations.NotNull;

public enum Schedule {

    DAILY_4AM("Daily", "0 0 4 * * ?"),
    EVERY_MONDAY("Weekly", "0 0 4 ? * MON *"),
    EVERY_TWO_WEEKS("Bi-weekly", "0 0 4 1,15 * ? *"),
    EVERY_MONTH("Monthly", "0 0 4 1 * ?");

    @NotNull
    private final String message;

    @NotNull
    private final String cron;

    Schedule(@NotNull String message, @NotNull String cron) {
        this.message = message;
        this.cron = cron;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public String getCron() {
        return cron;
    }

    public static Schedule getSchedule(int ordinal) {
        if (ordinal == DAILY_4AM.ordinal()) {
            return DAILY_4AM;
        } else if (ordinal == EVERY_MONDAY.ordinal()) {
            return EVERY_MONDAY;
        } else if (ordinal == EVERY_TWO_WEEKS.ordinal()) {
            return EVERY_TWO_WEEKS;
        } else {
            return EVERY_MONTH;
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "message='" + message + '\'' +
                ", cron='" + cron + '\'' +
                '}';
    }
}
