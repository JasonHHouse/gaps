/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarr_v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Movie movie;

    @BeforeAll
    static void loadJson() {
        try {
            String json = IOUtils.toString(MovieTest.class.getResourceAsStream("radarr_v3_movie.json"), StandardCharsets.UTF_8);
            movie = objectMapper.readValue(json, Movie.class);
        } catch (IOException e) {
            fail("Failed to read radarr v3 single movie JSON file", e);
        }
    }

    @Test
    void MovieTitle() {
        assertEquals("Dragon Ball: Mystical Adventure", movie.getTitle(), "Should find Dragon Ball title");
    }

    @Test
    void AlternateTitles() {
        assertEquals(10, movie.getAlternateTitles().size(), "Should find 10 alternate titles");
    }

    @Test
    void AlternateTitlesSourceType() {
        assertEquals("tmdb", movie.getAlternateTitles().get(0).getSourceType(), "First alternate title source type should be tmdb");
    }

    @Test
    void Images() {
        assertEquals(2, movie.getImages().size(), "Should find 2 images");
    }

    @Test
    void ImageCoverType() {
        assertEquals("poster", movie.getImages().get(0).getCoverType(), "First covertype should be poster");
    }

    @Test
    void Genres() {
        assertEquals(Arrays.asList("Action", "Animation"), movie.getGenres(), "Genres should be Action and Animation");
    }

    @Test
    void Ratings() {
        assertEquals(new Ratings(116, 6.5), movie.getRatings(), "Ratings should be 116 votes and 6.5 value");
    }

    @Test
    void MovieFileQualityQuality() {
        assertEquals(new Quality2(8,"WEBDL-480p","webdl",480,"none"), movie.getMovieFile().getQuality().getQuality(), "Quality2 should be 8, WEBDL-480p, webdl, 480, none");
    }
}
