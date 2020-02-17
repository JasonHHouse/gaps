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

        Movie movie = new Movie("Alien", 1979);
        movie.setTvdbId(1345);
        movie.setCollection("Aliens Collection");
        movie.setCollectionId(5423);
        movie.setImdbId("IMDB ID");
        movie.setPosterUrl("POSTER URL");

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

        Movie movie = new Movie("Alien", 1979);
        movie.setTvdbId(1345);
        movie.setCollection("Aliens Collection");
        movie.setCollectionId(5423);
        movie.setImdbId("IMDB ID");
        movie.setPosterUrl("");

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
