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
import com.jasonhhouse.gaps.MovieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieStatusDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MovieStatus.class, new MovieStatusDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testDeserializeMovieStatusAll() throws JsonProcessingException {
        String json = "{\"id\":0,\"message\":\"All\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.ALL, status);
        assertEquals(0, status.getId());
        assertEquals("All", status.getMessage());
    }

    @Test
    public void testDeserializeMovieStatusReleased() throws JsonProcessingException {
        String json = "{\"id\":1,\"message\":\"Released\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.RELEASED, status);
        assertEquals(1, status.getId());
        assertEquals("Released", status.getMessage());
    }

    @Test
    public void testDeserializeUnknownIdDefaultsToAll() throws JsonProcessingException {
        String json = "{\"id\":999,\"message\":\"Unknown\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        // Unknown IDs should default to ALL
        assertEquals(MovieStatus.ALL, status);
    }

    @Test
    public void testDeserializeWithMinimalJson() throws JsonProcessingException {
        // Only ID field is required for deserialization
        String json = "{\"id\":1}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.RELEASED, status);
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        ObjectMapper fullMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MovieStatus.class, new MovieStatusSerializer());
        module.addDeserializer(MovieStatus.class, new MovieStatusDeserializer());
        fullMapper.registerModule(module);

        MovieStatus original = MovieStatus.RELEASED;
        String json = fullMapper.writeValueAsString(original);
        MovieStatus deserialized = fullMapper.readValue(json, MovieStatus.class);

        assertEquals(original, deserialized);
    }
}
