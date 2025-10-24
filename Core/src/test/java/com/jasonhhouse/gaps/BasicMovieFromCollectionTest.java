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
