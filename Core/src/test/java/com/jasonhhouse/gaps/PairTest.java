/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PairTest {

    @Test
    void pairTest() {
        Pair<Integer, Float> pair1 = new Pair<>(1, 1.0F);
        Pair<Integer, Float> pair2 = new Pair<>(1, 1.0F);
        Pair<Integer, Float> pair3 = new Pair<>(1, 2.0F);

        assertEquals(pair1.getLeft(), pair2.getLeft(), "Pair1 and Pair2 left should match");
        assertEquals(pair1.getRight(), pair2.getRight(), "Pair1 and Pair2 right should match");

        assertEquals(pair1, pair2, "Pair equals method broken");
        assertNotEquals(pair1, pair3, "Pair equals method broken");
    }
}
