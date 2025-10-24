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

public class MovieStatusSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MovieStatus.class, new MovieStatusSerializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerializeMovieStatusAll() throws JsonProcessingException {
        MovieStatus status = MovieStatus.ALL;
        String json = objectMapper.writeValueAsString(status);

        assertTrue(json.contains("\"id\":0"));
        assertTrue(json.contains("\"message\":\"All\""));
    }

    @Test
    public void testSerializeMovieStatusReleased() throws JsonProcessingException {
        MovieStatus status = MovieStatus.RELEASED;
        String json = objectMapper.writeValueAsString(status);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"message\":\"Released\""));
    }

    @Test
    public void testSerializedJsonStructure() throws JsonProcessingException {
        MovieStatus status = MovieStatus.ALL;
        String json = objectMapper.writeValueAsString(status);

        // Verify it's a proper JSON object with both fields
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
        assertTrue(json.contains("id"));
        assertTrue(json.contains("message"));
    }

    @Test
    public void testSerializeAllMovieStatuses() throws JsonProcessingException {
        for (MovieStatus status : MovieStatus.getAllMovieStatuses()) {
            String json = objectMapper.writeValueAsString(status);

            assertNotNull(json);
            assertTrue(json.contains("\"id\":" + status.getId()));
            assertTrue(json.contains("\"message\":\"" + status.getMessage() + "\""));
        }
    }
}
