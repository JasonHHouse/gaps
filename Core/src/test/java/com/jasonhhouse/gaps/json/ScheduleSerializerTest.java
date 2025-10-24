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

public class ScheduleSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Schedule.class, new ScheduleSerializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerializeScheduleHourly() throws JsonProcessingException {
        Schedule schedule = Schedule.HOURLY;
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"id\":0"));
        assertTrue(json.contains("\"message\":\"Hourly\""));
        assertTrue(json.contains("\"enabled\":true"));
    }

    @Test
    public void testSerializeScheduleDaily() throws JsonProcessingException {
        Schedule schedule = Schedule.DAILY_4AM;
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"message\":\"Daily\""));
        assertTrue(json.contains("\"enabled\":true"));
    }

    @Test
    public void testSerializeScheduleWeekly() throws JsonProcessingException {
        Schedule schedule = Schedule.EVERY_MONDAY;
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"id\":2"));
        assertTrue(json.contains("\"message\":\"Weekly\""));
    }

    @Test
    public void testSerializeScheduleBiWeekly() throws JsonProcessingException {
        Schedule schedule = Schedule.EVERY_TWO_WEEKS;
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"id\":3"));
        assertTrue(json.contains("\"message\":\"Bi-weekly\""));
    }

    @Test
    public void testSerializeScheduleMonthly() throws JsonProcessingException {
        Schedule schedule = Schedule.EVERY_MONTH;
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"id\":4"));
        assertTrue(json.contains("\"message\":\"Monthly\""));
    }

    @Test
    public void testSerializeScheduleWithEnabledFalse() throws JsonProcessingException {
        Schedule schedule = Schedule.HOURLY.setEnabled(false);
        String json = objectMapper.writeValueAsString(schedule);

        assertTrue(json.contains("\"enabled\":false"));

        // Clean up - reset to default
        schedule.setEnabled(true);
    }

    @Test
    public void testSerializedJsonStructure() throws JsonProcessingException {
        Schedule schedule = Schedule.DAILY_4AM;
        String json = objectMapper.writeValueAsString(schedule);

        // Verify it's a proper JSON object with all three fields
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
        assertTrue(json.contains("id"));
        assertTrue(json.contains("message"));
        assertTrue(json.contains("enabled"));
    }

    @Test
    public void testSerializeAllSchedules() throws JsonProcessingException {
        for (Schedule schedule : Schedule.getAllSchedules()) {
            String json = objectMapper.writeValueAsString(schedule);

            assertNotNull(json);
            assertTrue(json.contains("\"id\":" + schedule.getId()));
            assertTrue(json.contains("\"message\":\"" + schedule.getMessage() + "\""));
            assertTrue(json.contains("\"enabled\":" + schedule.getEnabled()));
        }
    }
}
