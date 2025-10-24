/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieStatusPayloadTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetter() {
        MovieStatusPayload payload = new MovieStatusPayload(1);
        assertEquals(1, payload.getStatus());
    }

    @Test
    public void testConstructorWithNull() {
        MovieStatusPayload payload = new MovieStatusPayload(null);
        assertNull(payload.getStatus());
    }

    @Test
    public void testToString() {
        MovieStatusPayload payload = new MovieStatusPayload(42);
        String result = payload.toString();

        assertNotNull(result);
        assertTrue(result.contains("MovieStatusPayload"));
        assertTrue(result.contains("status=42"));
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"status\":1}";
        MovieStatusPayload payload = objectMapper.readValue(json, MovieStatusPayload.class);

        assertEquals(1, payload.getStatus());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        MovieStatusPayload payload = new MovieStatusPayload(2);
        String json = objectMapper.writeValueAsString(payload);

        assertTrue(json.contains("\"status\":2"));
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        MovieStatusPayload original = new MovieStatusPayload(5);
        String json = objectMapper.writeValueAsString(original);
        MovieStatusPayload deserialized = objectMapper.readValue(json, MovieStatusPayload.class);

        assertEquals(original.getStatus(), deserialized.getStatus());
    }
}
