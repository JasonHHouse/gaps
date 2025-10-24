package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieStatusPayloadTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetter() {
        MovieStatusPayload payload = new MovieStatusPayload(1);
        assertEquals(1, payload.getStatus());
    }

    @Test
    public void testConstructorWithNull() {
        MovieStatusPayload payload = new MovieStatusPayload(null);
        assertNull(payload.getStatus());
    }

    @Test
    public void testToString() {
        MovieStatusPayload payload = new MovieStatusPayload(42);
        String result = payload.toString();

        assertNotNull(result);
        assertTrue(result.contains("MovieStatusPayload"));
        assertTrue(result.contains("status=42"));
    }

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = "{\"status\":1}";
        MovieStatusPayload payload = objectMapper.readValue(json, MovieStatusPayload.class);

        assertEquals(1, payload.getStatus());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        MovieStatusPayload payload = new MovieStatusPayload(2);
        String json = objectMapper.writeValueAsString(payload);

        assertTrue(json.contains("\"status\":2"));
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        MovieStatusPayload original = new MovieStatusPayload(5);
        String json = objectMapper.writeValueAsString(original);
        MovieStatusPayload deserialized = objectMapper.readValue(json, MovieStatusPayload.class);

        assertEquals(original.getStatus(), deserialized.getStatus());
    }
}
