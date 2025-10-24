/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
