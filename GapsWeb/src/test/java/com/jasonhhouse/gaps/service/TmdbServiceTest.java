/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Payload;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TmdbServiceTest {

    private final TmdbService tmdbService = new TmdbService();

    @Test
    public void nullTmdbKey() {
        Payload payload = tmdbService.testTmdbKey(null);
        assertEquals("Null key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_INVALID.getCode());
    }

    @Test
    public void badTmdbKey() {
        Payload payload = tmdbService.testTmdbKey("1234qwer");
        assertEquals("Invalid key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_INVALID.getCode());
    }

    @Test
    public void validTmdbKey() {
        Payload payload = tmdbService.testTmdbKey("723b4c763114904392ca441909aa0375");
        assertEquals("Valid key for TMDB failed", payload.getCode(), Payload.TMDB_KEY_VALID.getCode());
    }
}
