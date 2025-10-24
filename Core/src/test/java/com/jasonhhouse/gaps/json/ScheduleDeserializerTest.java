/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jasonhhouse.gaps.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Schedule.class, new ScheduleDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testDeserializeScheduleHourly() throws JsonProcessingException {
        String json = "{\"id\":0,\"message\":\"Hourly\",\"enabled\":true}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.HOURLY, schedule);
        assertEquals(0, schedule.getId());
        assertEquals("Hourly", schedule.getMessage());
        assertTrue(schedule.getEnabled());
    }

    @Test
    public void testDeserializeScheduleDaily() throws JsonProcessingException {
        String json = "{\"id\":1,\"message\":\"Daily\",\"enabled\":false}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.DAILY_4AM, schedule);
        assertEquals(1, schedule.getId());
        assertEquals("Daily", schedule.getMessage());
        assertFalse(schedule.getEnabled());

        // Clean up
        schedule.setEnabled(true);
    }

    @Test
    public void testDeserializeScheduleWeekly() throws JsonProcessingException {
        String json = "{\"id\":2,\"message\":\"Weekly\",\"enabled\":true}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.EVERY_MONDAY, schedule);
        assertEquals(2, schedule.getId());
    }

    @Test
    public void testDeserializeScheduleBiWeekly() throws JsonProcessingException {
        String json = "{\"id\":3,\"message\":\"Bi-weekly\",\"enabled\":true}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.EVERY_TWO_WEEKS, schedule);
        assertEquals(3, schedule.getId());
    }

    @Test
    public void testDeserializeScheduleMonthly() throws JsonProcessingException {
        String json = "{\"id\":4,\"message\":\"Monthly\",\"enabled\":true}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.EVERY_MONTH, schedule);
        assertEquals(4, schedule.getId());
    }

    @Test
    public void testDeserializeUnknownIdDefaultsToMonthly() throws JsonProcessingException {
        String json = "{\"id\":999,\"message\":\"Unknown\",\"enabled\":true}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        // Unknown IDs should default to EVERY_MONTH
        assertEquals(Schedule.EVERY_MONTH, schedule);
    }

    @Test
    public void testDeserializeWithEnabledFalse() throws JsonProcessingException {
        String json = "{\"id\":0,\"enabled\":false}";
        Schedule schedule = objectMapper.readValue(json, Schedule.class);

        assertEquals(Schedule.HOURLY, schedule);
        assertFalse(schedule.getEnabled());

        // Clean up
        schedule.setEnabled(true);
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        ObjectMapper fullMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Schedule.class, new ScheduleSerializer());
        module.addDeserializer(Schedule.class, new ScheduleDeserializer());
        fullMapper.registerModule(module);

        Schedule original = Schedule.DAILY_4AM.setEnabled(false);
        String json = fullMapper.writeValueAsString(original);
        Schedule deserialized = fullMapper.readValue(json, Schedule.class);

        assertEquals(original, deserialized);
        assertEquals(original.getEnabled(), deserialized.getEnabled());

        // Clean up
        original.setEnabled(true);
    }

    @Test
    public void testRoundTripAllSchedules() throws JsonProcessingException {
        ObjectMapper fullMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Schedule.class, new ScheduleSerializer());
        module.addDeserializer(Schedule.class, new ScheduleDeserializer());
        fullMapper.registerModule(module);

        for (Schedule schedule : Schedule.getAllSchedules()) {
            String json = fullMapper.writeValueAsString(schedule);
            Schedule deserialized = fullMapper.readValue(json, Schedule.class);

            assertEquals(schedule, deserialized);
            assertEquals(schedule.getId(), deserialized.getId());
            assertEquals(schedule.getMessage(), deserialized.getMessage());
            assertEquals(schedule.getEnabled(), deserialized.getEnabled());
        }
    }
}
