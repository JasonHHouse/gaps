/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarrV3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection {
    @NotNull
    private final String name;
    @NotNull
    private final Integer tmdbId;
    @NotNull
    private final List<Object> images;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Collection(@JsonProperty(value = "name") @Nullable String name,
                      @JsonProperty(value = "tmdbId") @Nullable Integer tmdbId,
                      @JsonProperty(value = "images") @Nullable List<Object> images) {
        this.name = name == null ? "" : name;
        this.tmdbId = tmdbId == null ? -1 : tmdbId;
        this.images = images == null ? Collections.emptyList() : images;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public @NotNull List<Object> getImages() {
        return images;
    }

    @NotNull
    static Collection getDefault() {
        return new Collection(null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return name.equals(that.name) &&
                tmdbId.equals(that.tmdbId) &&
                images.equals(that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tmdbId, images);
    }

    @Override
    public String toString() {
        return "Collection{" +
                "name='" + name + '\'' +
                ", tmdbId=" + tmdbId +
                ", images=" + images +
                '}';
    }
}
