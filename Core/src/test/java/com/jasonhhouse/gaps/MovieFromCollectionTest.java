/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieFromCollectionTest {

    @NotNull
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testReadingFromJson() throws JsonProcessingException {
        MovieFromCollection movieFromCollection = objectMapper.readValue("{\"title\":\"TITLE\",\"tmdbId\":123,\"owned\":true,\"year\":5}}", MovieFromCollection.class);
        assertEquals("TITLE", movieFromCollection.getTitle(), "Title should be 'TITLE'");
        assertEquals(123, movieFromCollection.getTmdbId(), "tmdbId should be '123'");
        assertTrue(movieFromCollection.getOwned(), "Title should be 'TITLE'");
        assertEquals(5, movieFromCollection.getYear(), "year should be '5'");
    }

    @Test
    void testWritingToJson() throws JsonProcessingException {
        MovieFromCollection movieFromCollection = new MovieFromCollection("TITLE",123,true, 5);
        String json = objectMapper.writeValueAsString(movieFromCollection);
        assertEquals("{\"title\":\"TITLE\",\"tmdbId\":123,\"owned\":true,\"year\":5}", json, "JSON output should be equal");
    }
}