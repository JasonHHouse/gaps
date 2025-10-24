/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulePayloadTest {

    @Test
    public void testConstructorAndGetters() {
        SchedulePayload payload = new SchedulePayload(1, true);

        assertEquals(1, payload.getSchedule());
        assertTrue(payload.getEnabled());
    }

    @Test
    public void testConstructorWithFalseEnabled() {
        SchedulePayload payload = new SchedulePayload(2, false);

        assertEquals(2, payload.getSchedule());
        assertFalse(payload.getEnabled());
    }

    @Test
    public void testConstructorWithNullValues() {
        SchedulePayload payload = new SchedulePayload(null, null);

        assertNull(payload.getSchedule());
        assertNull(payload.getEnabled());
    }

    @Test
    public void testToString() {
        SchedulePayload payload = new SchedulePayload(3, true);
        String result = payload.toString();

        assertNotNull(result);
        assertTrue(result.contains("SchedulePayload"));
        assertTrue(result.contains("schedule=3"));
        assertTrue(result.contains("enabled=true"));
    }

    @Test
    public void testToStringWithFalse() {
        SchedulePayload payload = new SchedulePayload(4, false);
        String result = payload.toString();

        assertTrue(result.contains("schedule=4"));
        assertTrue(result.contains("enabled=false"));
    }
}
