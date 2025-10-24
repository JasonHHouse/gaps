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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class SortedList<T> extends ArrayList<T> {

    private static final long serialVersionUID = -441423857950125427L;

    private final Comparator<T> comparator;

    public SortedList(Class<T> clazz) {
        super();
        if (!Comparable.class.isAssignableFrom(clazz)) {
            throw new ClassCastException(clazz + " does not implement comparable");
        }
        // Create comparator using the comparable interface.
        this.comparator = new Comparator<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(T o1, T o2) {
                return ((Comparable<? super T>) o1).compareTo(o2);
            }
        };
    }

    public SortedList(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    /**
     * Add the element to this sorted list in a sorted order.
     *
     * @param e The element
     * @return Always true
     */
    @Override
    public boolean add(T e) {
        int insertPoint = Collections.binarySearch(this, e, comparator);
        if (insertPoint < 0) {
            insertPoint = (insertPoint * -1) - 1;
        }
        super.add(insertPoint, e);
        return true;

    }

    /**
     * @deprecated Function ignores the index and insert the element in the correct order
     */
    @Deprecated
    public void add(int index, T element) {
        add(element);
    }

    /**
     * Add all elements from the collection to this sorted list in a sorted order.
     *
     * @param c The collection holding all elements to add to this sorted list. The elements in the collection needn't to be in sorted order
     * @return Always true
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T s : c) {
            add(s);
        }
        return true;
    }

    /**
     * @deprecated Function ignores the index and insert the elements in the correct order
     */
    @Deprecated
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(c);
    }
}