package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Payload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TmdbQueryServiceTest {

    @InjectMocks
    TmdbQueryService tmdbQueryService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nullTmdbKey() {
        Payload payload = tmdbQueryService.testTmdbKey(null);
        assertEquals("Null key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_INVALID.getCode());
    }

    @Test
    public void badTmdbKey() {
        Payload payload = tmdbQueryService.testTmdbKey("1234qwer");
        assertEquals("Invalid key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_INVALID.getCode());
    }

    @Test
    public void validTmdbKey() {
        Payload payload = tmdbQueryService.testTmdbKey("723b4c763114904392ca441909aa0375");
        assertEquals("Valid key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_VALID.getCode());
    }
}
