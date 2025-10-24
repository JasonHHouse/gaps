package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jasonhhouse.gaps.MovieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieStatusDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MovieStatus.class, new MovieStatusDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testDeserializeMovieStatusAll() throws JsonProcessingException {
        String json = "{\"id\":0,\"message\":\"All\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.ALL, status);
        assertEquals(0, status.getId());
        assertEquals("All", status.getMessage());
    }

    @Test
    public void testDeserializeMovieStatusReleased() throws JsonProcessingException {
        String json = "{\"id\":1,\"message\":\"Released\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.RELEASED, status);
        assertEquals(1, status.getId());
        assertEquals("Released", status.getMessage());
    }

    @Test
    public void testDeserializeUnknownIdDefaultsToAll() throws JsonProcessingException {
        String json = "{\"id\":999,\"message\":\"Unknown\"}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        // Unknown IDs should default to ALL
        assertEquals(MovieStatus.ALL, status);
    }

    @Test
    public void testDeserializeWithMinimalJson() throws JsonProcessingException {
        // Only ID field is required for deserialization
        String json = "{\"id\":1}";
        MovieStatus status = objectMapper.readValue(json, MovieStatus.class);

        assertEquals(MovieStatus.RELEASED, status);
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        ObjectMapper fullMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MovieStatus.class, new MovieStatusSerializer());
        module.addDeserializer(MovieStatus.class, new MovieStatusDeserializer());
        fullMapper.registerModule(module);

        MovieStatus original = MovieStatus.RELEASED;
        String json = fullMapper.writeValueAsString(original);
        MovieStatus deserialized = fullMapper.readValue(json, MovieStatus.class);

        assertEquals(original, deserialized);
    }
}
