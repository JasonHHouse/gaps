package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jasonhhouse.gaps.Movie;
import java.io.IOException;

public class MovieSerializer extends StdSerializer<Movie> {

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
