/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.BasicMovie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicMovieSerializerTest {

    @Test
    void serializeAndDeserialize() throws Exception {
        BasicMovie basicMovie = new BasicMovie.Builder("Alien", 1979)
                .setTmdbId(1345)
                .setCollectionTitle("Aliens Collection")
                .setCollectionId(5423)
                .setImdbId("IMDB ID")
                .setPosterUrl("POSTER URL")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(basicMovie);
        BasicMovie remadeBasicMovie = objectMapper.readValue(value, BasicMovie.class);

        assertEquals(remadeBasicMovie, basicMovie, "Failed to serialize then deserialize a movie");
    }

    @Test
    void serializeAndDeserialize_NullPoster() throws Exception {
        BasicMovie basicMovie = new BasicMovie.Builder("Alien", 1979)
                .setTmdbId(1345)
                .setCollectionTitle("Aliens Collection")
                .setCollectionId(5423)
                .setImdbId("IMDB ID")
                .setPosterUrl("")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(basicMovie);
        BasicMovie remadeBasicMovie = objectMapper.readValue(value, BasicMovie.class);

        assertEquals(remadeBasicMovie, basicMovie, "Failed to serialize then deserialize a movie");
    }
}
