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
import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.MovieFromCollection;
import java.io.IOException;

public class MovieSerializer extends StdSerializer<BasicMovie> {
    public MovieSerializer() {
        this(null);
    }

    protected MovieSerializer(Class<BasicMovie> t) {
        super(t);
    }

    @Override
    public void serialize(BasicMovie basicMovie, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(BasicMovie.TVDB_ID, basicMovie.getTmdbId());
        jsonGenerator.writeStringField(BasicMovie.IMDB_ID, basicMovie.getImdbId());
        jsonGenerator.writeStringField(BasicMovie.NAME, basicMovie.getTitle());
        jsonGenerator.writeNumberField(BasicMovie.YEAR, basicMovie.getYear());
        jsonGenerator.writeStringField(BasicMovie.POSTER, basicMovie.getPosterUrl());
        jsonGenerator.writeNumberField(BasicMovie.COLLECTION_ID, basicMovie.getCollectionId());
        jsonGenerator.writeStringField(BasicMovie.COLLECTION, basicMovie.getCollectionTitle());
        jsonGenerator.writeStringField(BasicMovie.LANGUAGE, basicMovie.getLanguage());
        jsonGenerator.writeStringField(BasicMovie.OVERVIEW, basicMovie.getOverview());
        jsonGenerator.writeArrayFieldStart(BasicMovie.MOVIES_IN_COLLECTION);
        for (MovieFromCollection movieInCollection : basicMovie.getMoviesInCollection()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("title", movieInCollection.getTitle());
            jsonGenerator.writeStringField("id", movieInCollection.getTmdbId());
            jsonGenerator.writeBooleanField("owned", movieInCollection.getOwned());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
