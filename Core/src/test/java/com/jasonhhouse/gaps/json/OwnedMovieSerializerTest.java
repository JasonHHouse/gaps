package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jasonhhouse.gaps.OwnedMovie;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OwnedMovieSerializerTest {

    @Test
    void serializeAndDeserialize() throws Exception {
        OwnedMovieSerializer movieSerializer = new OwnedMovieSerializer();
        OwnedMovieDeserializer movieDeserializer = new OwnedMovieDeserializer();

        OwnedMovie ownedMovie = new OwnedMovie("Alien", 1979, "THUMBNAIL", 1235, "TVDB_ID", "ENGLISH");

        ObjectMapper objectMapper = new ObjectMapper();
        Writer jsonWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        movieSerializer.serialize(ownedMovie, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = factory.createParser(jsonWriter.toString());
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        jsonParser.setCodec(objectMapper);

        OwnedMovie ownedMovie1 = movieDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(ownedMovie1, ownedMovie, "Failed to serialize then deserialize a movie");
    }

    @Test
    void serializeAndDeserialize_NullThumbnail() throws Exception {
        OwnedMovieSerializer movieSerializer = new OwnedMovieSerializer();
        OwnedMovieDeserializer movieDeserializer = new OwnedMovieDeserializer();

        OwnedMovie ownedMovie = new OwnedMovie("Alien", 1979, null, 1235, "TVDB_ID", "ENGLISH");

        ObjectMapper objectMapper = new ObjectMapper();
        Writer jsonWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        movieSerializer.serialize(ownedMovie, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = factory.createParser(jsonWriter.toString());
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        jsonParser.setCodec(objectMapper);

        OwnedMovie ownedMovie1 = movieDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(ownedMovie1, ownedMovie, "Failed to serialize then deserialize a movie with null thumbnail");
    }

    @Test
    void serializeAndDeserialize_NullLanguage() throws Exception {
        OwnedMovieSerializer movieSerializer = new OwnedMovieSerializer();
        OwnedMovieDeserializer movieDeserializer = new OwnedMovieDeserializer();

        OwnedMovie ownedMovie = new OwnedMovie("Alien", 1979, "THUMBNAIL", 1235, "TVDB_ID", null);

        ObjectMapper objectMapper = new ObjectMapper();
        Writer jsonWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        movieSerializer.serialize(ownedMovie, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = factory.createParser(jsonWriter.toString());
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        jsonParser.setCodec(objectMapper);

        OwnedMovie ownedMovie1 = movieDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(ownedMovie1, ownedMovie, "Failed to serialize then deserialize a movie with null language");
    }
}
