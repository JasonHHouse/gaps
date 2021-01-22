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
        return null;
    }

    @Override
    public Comparator<PlexLibrary> thenComparing(Comparator<? super PlexLibrary> other) {
        return null;
    }

    @Override
    public <U> Comparator<PlexLibrary> thenComparing(Function<? super PlexLibrary, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return null;
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<PlexLibrary> thenComparing(Function<? super PlexLibrary, ? extends U> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<PlexLibrary> thenComparingInt(ToIntFunction<? super PlexLibrary> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<PlexLibrary> thenComparingLong(ToLongFunction<? super PlexLibrary> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<PlexLibrary> thenComparingDouble(ToDoubleFunction<? super PlexLibrary> keyExtractor) {
        return null;
    }
}
