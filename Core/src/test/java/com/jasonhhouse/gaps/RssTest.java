/*
 * Copyright 2020 Jason H House
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

public class RssTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetters() {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test Movie", "/poster.jpg");

        assertEquals("tt1234567", rss.getImdbId());
        assertEquals(2020, rss.getReleaseDate());
        assertEquals(12345, rss.getTmdbId());
        assertEquals("Test Movie", rss.getTitle());
        assertEquals("/poster.jpg", rss.getPosterPath());
    }

    @Test
    public void testConstructorWithNullValues() {
        Rss rss = new Rss(null, null, null, null, null);

        assertNull(rss.getImdbId());
        assertNull(rss.getReleaseDate());
        assertNull(rss.getTmdbId());
        assertNull(rss.getTitle());
        assertNull(rss.getPosterPath());
    }

    @Test
    public void testEquals_SameObject() {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        assertEquals(rss, rss);
    }

    @Test
    public void testEquals_EqualObjects() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");

        assertEquals(rss1, rss2);
    }

    @Test
    public void testEquals_DifferentImdbId() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt7654321", 2020, 12345, "Test", "/poster.jpg");

        assertNotEquals(rss1, rss2);
    }

    @Test
    public void testEquals_DifferentReleaseDate() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2021, 12345, "Test", "/poster.jpg");

        assertNotEquals(rss1, rss2);
    }

    @Test
    public void testEquals_DifferentTmdbId() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2020, 54321, "Test", "/poster.jpg");

        assertNotEquals(rss1, rss2);
    }

    @Test
    public void testEquals_DifferentTitle() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2020, 12345, "Different", "/poster.jpg");

        assertNotEquals(rss1, rss2);
    }

    @Test
    public void testEquals_DifferentPosterPath() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2020, 12345, "Test", "/different.jpg");

        assertNotEquals(rss1, rss2);
    }

    @Test
    public void testEquals_Null() {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        assertNotEquals(rss, null);
    }

    @Test
    public void testEquals_DifferentClass() {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        assertNotEquals(rss, "Not an Rss");
    }

    @Test
    public void testHashCode_SameValues() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");

        assertEquals(rss1.hashCode(), rss2.hashCode());
    }

    @Test
    public void testHashCode_DifferentValues() {
        Rss rss1 = new Rss("tt1234567", 2020, 12345, "Test", "/poster.jpg");
        Rss rss2 = new Rss("tt7654321", 2020, 12345, "Test", "/poster.jpg");

        assertNotEquals(rss1.hashCode(), rss2.hashCode());
    }

    @Test
    public void testToString() {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test Movie", "/poster.jpg");
        String result = rss.toString();

        assertNotNull(result);
        assertTrue(result.contains("Rss"));
        assertTrue(result.contains("imdbId='tt1234567'"));
        assertTrue(result.contains("releaseDate=2020"));
        assertTrue(result.contains("tmdbId=12345"));
        assertTrue(result.contains("title='Test Movie'"));
        assertTrue(result.contains("posterPath='/poster.jpg'"));
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        Rss rss = new Rss("tt1234567", 2020, 12345, "Test Movie", "/poster.jpg");
        String json = objectMapper.writeValueAsString(rss);

        assertTrue(json.contains("\"imdb_id\":\"tt1234567\""));
        assertTrue(json.contains("\"release_date\":2020"));
        assertTrue(json.contains("\"tmdb_id\":12345"));
        assertTrue(json.contains("\"title\":\"Test Movie\""));
        assertTrue(json.contains("\"poster_path\":\"/poster.jpg\""));
    }
}
