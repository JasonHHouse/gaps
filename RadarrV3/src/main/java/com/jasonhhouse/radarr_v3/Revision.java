/*
 *
 *  Copyright 2025 Jason H House
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
public final class Revision {
    @NotNull
    private final Integer version;
    @NotNull
    private final Integer real;
    @NotNull
    private final Boolean isRepack;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Revision(@JsonProperty(value = "version") @Nullable Integer version,
                    @JsonProperty(value = "real") @Nullable Integer real,
                    @JsonProperty(value = "isRepack") @Nullable Boolean isRepack) {
        this.version = version == null ? -1 : version;
        this.real = real == null ? -1 : real;
        this.isRepack = isRepack != null && isRepack;
    }

    @NotNull
    static Revision getDefault() {
        return new Revision(null, null, null);
    }

    @NotNull
    public Integer getVersion() {
        return version;
    }

    @NotNull
    public Integer getReal() {
        return real;
    }

    @NotNull
    public Boolean getRepack() {
        return isRepack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revision revision = (Revision) o;
        return version.equals(revision.version) &&
                real.equals(revision.real) &&
                isRepack.equals(revision.isRepack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, real, isRepack);
    }

    @Override
    public String toString() {
        return "Revision{" +
                "version=" + version +
                ", real=" + real +
                ", isRepack=" + isRepack +
                '}';
    }
}
