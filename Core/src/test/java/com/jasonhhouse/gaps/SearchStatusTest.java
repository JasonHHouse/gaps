package com.jasonhhouse.gaps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchStatusTest {

    @Test
    public void testConstructorAndGetIsSearching() {
        SearchStatus status = new SearchStatus(true);
        assertTrue(status.getIsSearching());
    }

    @Test
    public void testConstructorWithFalse() {
        SearchStatus status = new SearchStatus(false);
        assertFalse(status.getIsSearching());
    }

    @Test
    public void testConstructorWithNull() {
        SearchStatus status = new SearchStatus(null);
        assertNull(status.getIsSearching());
    }
}
