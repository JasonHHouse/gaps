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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jasonhhouse.gaps.Movie;
import java.io.IOException;

public class MovieSerializer extends StdSerializer<Movie> {
    public MovieSerializer() {
        this(null);
    }

    protected MovieSerializer(Class<Movie> t) {
        super(t);
    }

    @Override
    public void serialize(Movie movie, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Movie.TVDB_ID, movie.getTvdbId());
        jsonGenerator.writeStringField(Movie.IMDB_ID, movie.getImdbId());
        jsonGenerator.writeStringField(Movie.NAME, movie.getName());
        jsonGenerator.writeNumberField(Movie.YEAR, movie.getYear());
        jsonGenerator.writeNumberField(Movie.COLLECTION_ID, movie.getCollectionId());
        jsonGenerator.writeStringField(Movie.COLLECTION, movie.getCollection());
        jsonGenerator.writeEndObject();
    }
}
