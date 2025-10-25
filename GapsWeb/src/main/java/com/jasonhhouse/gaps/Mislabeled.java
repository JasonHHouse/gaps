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

import java.util.Objects;

public class Mislabeled {
    private final String filename;
    private final String plexTitle;
    private final Double percentageMatch;

    public Mislabeled(String filename, String plexTitle, Double percentageMatch) {
        this.filename = filename;
        this.plexTitle = plexTitle;
        this.percentageMatch = percentageMatch;
    }

    public String getFilename() {
        return filename;
    }

    public String getPlexTitle() {
        return plexTitle;
    }

    public Double getPercentageMatch() {
        return percentageMatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mislabeled that = (Mislabeled) o;
        return Objects.equals(filename, that.filename) &&
                Objects.equals(plexTitle, that.plexTitle) &&
                Objects.equals(percentageMatch, that.percentageMatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, plexTitle, percentageMatch);
    }

    @Override
    public String toString() {
        return "MissLabeled{" +
                "filename='" + filename + '\'' +
                ", plexTitle='" + plexTitle + '\'' +
                ", percentageMatch=" + percentageMatch +
                '}';
    }
}
