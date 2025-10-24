package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jasonhhouse.gaps.MovieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieStatusSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MovieStatus.class, new MovieStatusSerializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerializeMovieStatusAll() throws JsonProcessingException {
        MovieStatus status = MovieStatus.ALL;
        String json = objectMapper.writeValueAsString(status);

        assertTrue(json.contains("\"id\":0"));
        assertTrue(json.contains("\"message\":\"All\""));
    }

    @Test
    public void testSerializeMovieStatusReleased() throws JsonProcessingException {
        MovieStatus status = MovieStatus.RELEASED;
        String json = objectMapper.writeValueAsString(status);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"message\":\"Released\""));
    }

    @Test
    public void testSerializedJsonStructure() throws JsonProcessingException {
        MovieStatus status = MovieStatus.ALL;
        String json = objectMapper.writeValueAsString(status);

        // Verify it's a proper JSON object with both fields
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
        assertTrue(json.contains("id"));
        assertTrue(json.contains("message"));
    }

    @Test
    public void testSerializeAllMovieStatuses() throws JsonProcessingException {
        for (MovieStatus status : MovieStatus.getAllMovieStatuses()) {
            String json = objectMapper.writeValueAsString(status);

            assertNotNull(json);
            assertTrue(json.contains("\"id\":" + status.getId()));
            assertTrue(json.contains("\"message\":\"" + status.getMessage() + "\""));
        }
    }
}
