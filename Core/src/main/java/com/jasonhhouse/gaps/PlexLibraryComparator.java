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

import com.jasonhhouse.plex.libs.PlexLibrary;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class PlexLibraryComparator implements Comparator<PlexLibrary> {
    @Override
    public int compare(PlexLibrary o1, PlexLibrary o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }

    @Override
    public Comparator<PlexLibrary> reversed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<PlexLibrary> thenComparing(Comparator<? super PlexLibrary> other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> Comparator<PlexLibrary> thenComparing(Function<? super PlexLibrary, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<PlexLibrary> thenComparing(Function<? super PlexLibrary, ? extends U> keyExtractor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<PlexLibrary> thenComparingInt(ToIntFunction<? super PlexLibrary> keyExtractor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<PlexLibrary> thenComparingLong(ToLongFunction<? super PlexLibrary> keyExtractor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<PlexLibrary> thenComparingDouble(ToDoubleFunction<? super PlexLibrary> keyExtractor) {
        throw new UnsupportedOperationException();
    }
}
