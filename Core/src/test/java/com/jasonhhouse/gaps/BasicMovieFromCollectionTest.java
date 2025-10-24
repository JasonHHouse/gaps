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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BasicMovieFromCollectionTest {

    @Test
    void MovieFromCollectionTest_Invalid_Owned (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", 1234, false, 5);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", 1234, true, 5);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of owned field");
    }

    @Test
    void MovieFromCollectionTest_Invalid_Id (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", 1234, false, 5);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", 9874, false, 5);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of ID field");
    }

    @Test
    void MovieFromCollectionTest_Invalid_Name (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", 1234, false, 5);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("qwe", 1234, false, 5);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of name field");
    }

    @Test
    void MovieFromCollectionTest_Invalid_Year (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", 1234, false, 5);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("qwe", 1234, false, 4);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of year field");
    }

    @Test
    void MovieFromCollectionTest_Valid (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", 1234, false, 5);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", 1234, false, 5);

        assertEquals(movieFromCollection1, movieFromCollection2, "Movies should be equal");
    }
}
