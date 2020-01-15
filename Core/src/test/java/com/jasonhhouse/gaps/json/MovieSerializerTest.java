package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jasonhhouse.gaps.Movie;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieSerializerTest {

    @Test
    void serializeAndDeserialize() throws Exception {
        MovieSerializer movieSerializer = new MovieSerializer();
        MovieDeserializer movieDeserializer = new MovieDeserializer();

        Movie movie = new Movie.Builder("Alien", 1979)
                .setTvdbId(1345)
                .setCollection("Aliens Collection")
                .setCollectionId(5423)
                .setImdbId("IMDB ID")
                .setPosterUrl("POSTER URL")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        Writer jsonWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        movieSerializer.serialize(movie, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = factory.createParser(jsonWriter.toString());
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        jsonParser.setCodec(objectMapper);

        Movie movie1 = movieDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(movie1, movie, "Failed to serialize then deserialize a movie");
    }

    @Test
    void serializeAndDeserialize_NullPoster() throws Exception {
        MovieSerializer movieSerializer = new MovieSerializer();
        MovieDeserializer movieDeserializer = new MovieDeserializer();

        Movie movie = new Movie.Builder("Alien", 1979)
                .setTvdbId(1345)
                .setCollection("Aliens Collection")
                .setCollectionId(5423)
                .setImdbId("IMDB ID")
                .setPosterUrl(null)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        Writer jsonWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        movieSerializer.serialize(movie, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = factory.createParser(jsonWriter.toString());
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        jsonParser.setCodec(objectMapper);

        Movie movie1 = movieDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(movie1, movie, "Failed to serialize then deserialize a movie with null poster url");
    }
}
