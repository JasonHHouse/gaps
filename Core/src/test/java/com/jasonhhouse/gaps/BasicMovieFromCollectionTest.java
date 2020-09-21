package com.jasonhhouse.gaps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BasicMovieFromCollectionTest {

    @Test
    void MovieFromCollectionTest_Invalid_Owned (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", "1234", false);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", "1234", true);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of owned field");
    }

    @Test
    void MovieFromCollectionTest_Invalid_Id (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", "1234", false);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", "9874", false);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of ID field");
    }

    @Test
    void MovieFromCollectionTest_Invalid_Name (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", "1234", false);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("qwe", "1234", false);

        assertNotEquals(movieFromCollection1, movieFromCollection2, "Movies should not be equal because of name field");
    }

    @Test
    void MovieFromCollectionTest_Valid (){
        MovieFromCollection movieFromCollection1 = new MovieFromCollection("ABC", "1234", false);
        MovieFromCollection movieFromCollection2 = new MovieFromCollection("ABC", "1234", false);

        assertEquals(movieFromCollection1, movieFromCollection2, "Movies should be equal");
    }
}
