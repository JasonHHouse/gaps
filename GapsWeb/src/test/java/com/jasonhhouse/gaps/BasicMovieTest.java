package com.jasonhhouse.gaps;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BasicMovieTest {

    @Test
    public void testBasicMovieBuilder() {
        BasicMovie movie = new BasicMovie.Builder("The Matrix", 1999)
                .setTmdbId(603)
                .setImdbId("tt0133093")
                .setCollectionId(2344)
                .setCollectionTitle("The Matrix Collection")
                .setPosterUrl("https://image.tmdb.org/t/p/w185/poster.jpg")
                .setBackdropPathUrl("https://image.tmdb.org/t/p/original/backdrop.jpg")
                .setOverview("A computer hacker learns from mysterious rebels about the true nature of his reality.")
                .build();

        assertTrue("Movie name should be 'The Matrix'", "The Matrix".equals(movie.getName()));
        assertTrue("Movie year should be 1999", movie.getYear() == 1999);
        assertTrue("TMDB ID should be 603", movie.getTmdbId() == 603);
        assertEquals("IMDB ID should match", "tt0133093", movie.getImdbId());
        assertTrue("Collection ID should be 2344", movie.getCollectionId() == 2344);
        assertEquals("Collection title should match", "The Matrix Collection", movie.getCollectionTitle());
    }

    @Test
    public void testBasicMovieEquality_SameMovie() {
        BasicMovie movie1 = new BasicMovie.Builder("Inception", 2010).build();
        BasicMovie movie2 = new BasicMovie.Builder("Inception", 2010).build();

        assertEquals("Movies with same name and year should be equal", movie1, movie2);
    }

    @Test
    public void testBasicMovieEquality_DifferentName() {
        BasicMovie movie1 = new BasicMovie.Builder("Inception", 2010).build();
        BasicMovie movie2 = new BasicMovie.Builder("Interstellar", 2010).build();

        assertNotEquals("Movies with different names should not be equal", movie1, movie2);
    }

    @Test
    public void testBasicMovieEquality_DifferentYear() {
        BasicMovie movie1 = new BasicMovie.Builder("Inception", 2010).build();
        BasicMovie movie2 = new BasicMovie.Builder("Inception", 2014).build();

        assertNotEquals("Movies with different years should not be equal", movie1, movie2);
    }

    @Test
    public void testBasicMovieWithGenres() {
        List<String> genres = Arrays.asList("Action", "Sci-Fi", "Thriller");
        BasicMovie movie = new BasicMovie.Builder("Blade Runner", 1982)
                .setGenres(genres)
                .build();

        assertNotNull("Genres should not be null", movie.getGenres());
        assertTrue("Should have 3 genres", movie.getGenres().size() == 3);
        assertTrue("Should contain Action genre", movie.getGenres().contains("Action"));
    }

    @Test
    public void testBasicMovieHashCode() {
        BasicMovie movie1 = new BasicMovie.Builder("Avatar", 2009).setTmdbId(19995).build();
        BasicMovie movie2 = new BasicMovie.Builder("Avatar", 2009).setTmdbId(19995).build();

        assertTrue("HashCode should be equal for equal movies", movie1.hashCode() == movie2.hashCode());
    }

    @Test
    public void testBasicMovieToString() {
        BasicMovie movie = new BasicMovie.Builder("The Godfather", 1972)
                .setTmdbId(238)
                .build();

        String result = movie.toString();
        assertNotNull("ToString should not be null", result);
        assertTrue("ToString should contain movie name", result.contains("The Godfather"));
        assertTrue("ToString should contain year", result.contains("1972"));
    }

    @Test
    public void testMovieFromCollection() {
        MovieFromCollection movieFromCollection = new MovieFromCollection(
                "Die Hard", 562, true, 1988);

        assertEquals("Title should match", "Die Hard", movieFromCollection.getTitle());
        assertTrue("TMDB ID should be 562", movieFromCollection.getTmdbId() == 562);
        assertTrue("Should be owned", movieFromCollection.getOwned());
        assertTrue("Year should be 1988", movieFromCollection.getYear() == 1988);
    }

    @Test
    public void testMovieFromCollectionEquality() {
        MovieFromCollection movie1 = new MovieFromCollection("Terminator", 218, false, 1984);
        MovieFromCollection movie2 = new MovieFromCollection("Terminator", 218, false, 1984);

        assertEquals("Identical MovieFromCollection objects should be equal", movie1, movie2);
    }

    @Test
    public void testMovieFromCollectionNotEqual_DifferentOwnership() {
        MovieFromCollection movie1 = new MovieFromCollection("RoboCop", 5548, true, 1987);
        MovieFromCollection movie2 = new MovieFromCollection("RoboCop", 5548, false, 1987);

        assertNotEquals("MovieFromCollection with different ownership should not be equal", movie1, movie2);
    }

    @Test
    public void testBasicMovieCollectionOperations() {
        BasicMovie movie = new BasicMovie.Builder("Star Wars", 1977)
                .setCollectionId(10)
                .setCollectionTitle("Star Wars Collection")
                .build();

        assertTrue("Collection ID should be set", movie.getCollectionId() == 10);
        assertEquals("Collection title should be set", "Star Wars Collection", movie.getCollectionTitle());
    }

    @Test
    public void testBasicMovieNullSafety() {
        BasicMovie movie = new BasicMovie.Builder("Test Movie", 2000).build();

        // Test that getters don't throw NPE for unset optional fields
        assertNotNull("Movie should not be null", movie);
        // TMDB ID and Collection ID may not default to 0, just verify they don't throw NPE
        movie.getTmdbId();
        movie.getCollectionId();
    }
}
