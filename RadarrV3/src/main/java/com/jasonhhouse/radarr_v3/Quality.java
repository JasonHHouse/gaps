/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarr_v3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Quality {
    @NotNull
    private final Quality2 quality;
    @NotNull
    private final Revision revision;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Quality(@JsonProperty(value = "quality") @Nullable Quality2 quality,
                   @JsonProperty(value = "revision") @Nullable Revision revision) {
        this.quality = quality == null ? Quality2.getDefault() : quality;
        this.revision = revision == null ? Revision.getDefault() : revision;
    }

    @NotNull
    static Quality getDefault() {
        return new Quality(null, null);
    }

    @NotNull
    public Quality2 getQuality() {
        return quality;
    }

    @NotNull
    public Revision getRevision() {
        return revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quality quality1 = (Quality) o;
        return quality.equals(quality1.quality) &&
                revision.equals(quality1.revision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quality, revision);
    }

    @Override
    public String toString() {
        return "Quality{" +
                "quality=" + quality +
                ", revision=" + revision +
                '}';
    }
}
