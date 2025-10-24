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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchResultsTest {

    @Test
    public void testConstructorAndGetters() {
        BasicMovie movie = new BasicMovie.Builder("Test Movie", 2020).build();
        SearchResults results = new SearchResults(5, 10, movie);

        assertEquals(5, results.getSearchedMovieCount());
        assertEquals(10, results.getTotalMovieCount());
        assertEquals(movie, results.getNextMovie());
    }

    @Test
    public void testConstructorWithNullMovie() {
        SearchResults results = new SearchResults(3, 7, null);

        assertEquals(3, results.getSearchedMovieCount());
        assertEquals(7, results.getTotalMovieCount());
        assertNull(results.getNextMovie());
    }

    @Test
    public void testEquals_SameObject() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results = new SearchResults(5, 10, movie);

        assertEquals(results, results);
    }

    @Test
    public void testEquals_EqualObjects() {
        BasicMovie movie1 = new BasicMovie.Builder("Test1", 2020).build();
        BasicMovie movie2 = new BasicMovie.Builder("Test2", 2021).build();

        SearchResults results1 = new SearchResults(5, 10, movie1);
        SearchResults results2 = new SearchResults(5, 10, movie2);

        assertEquals(results1, results2);
    }

    @Test
    public void testEquals_DifferentSearchedCount() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results1 = new SearchResults(5, 10, movie);
        SearchResults results2 = new SearchResults(6, 10, movie);

        assertNotEquals(results1, results2);
    }

    @Test
    public void testEquals_DifferentTotalCount() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results1 = new SearchResults(5, 10, movie);
        SearchResults results2 = new SearchResults(5, 11, movie);

        assertNotEquals(results1, results2);
    }

    @Test
    public void testEquals_Null() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results = new SearchResults(5, 10, movie);

        assertNotEquals(results, null);
    }

    @Test
    public void testEquals_DifferentClass() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results = new SearchResults(5, 10, movie);

        assertNotEquals(results, "Not a SearchResults");
    }

    @Test
    public void testHashCode_SameValues() {
        BasicMovie movie1 = new BasicMovie.Builder("Test1", 2020).build();
        BasicMovie movie2 = new BasicMovie.Builder("Test2", 2021).build();

        SearchResults results1 = new SearchResults(5, 10, movie1);
        SearchResults results2 = new SearchResults(5, 10, movie2);

        assertEquals(results1.hashCode(), results2.hashCode());
    }

    @Test
    public void testHashCode_DifferentValues() {
        BasicMovie movie = new BasicMovie.Builder("Test", 2020).build();
        SearchResults results1 = new SearchResults(5, 10, movie);
        SearchResults results2 = new SearchResults(6, 10, movie);

        assertNotEquals(results1.hashCode(), results2.hashCode());
    }

    @Test
    public void testToString() {
        BasicMovie movie = new BasicMovie.Builder("Test Movie", 2020).build();
        SearchResults results = new SearchResults(5, 10, movie);
        String result = results.toString();

        assertNotNull(result);
        assertTrue(result.contains("SearchResults"));
        assertTrue(result.contains("searchedMovieCount=5"));
        assertTrue(result.contains("totalMovieCount=10"));
        assertTrue(result.contains("nextMovie="));
    }
}
