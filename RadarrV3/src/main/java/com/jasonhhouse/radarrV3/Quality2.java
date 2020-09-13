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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Quality2 {
    @NotNull
    private final Integer id;
    @NotNull
    private final String name;
    @NotNull
    private final String source;
    @NotNull
    private final Integer resolution;
    @NotNull
    private final String modifier;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Quality2(@JsonProperty(value = "id") @Nullable Integer id,
                    @JsonProperty(value = "name") @Nullable String name,
                    @JsonProperty(value = "source") @Nullable String source,
                    @JsonProperty(value = "resolution") @Nullable Integer resolution,
                    @JsonProperty(value = "modifier") @Nullable String modifier) {
        this.id = id == null ? -1 : id;
        this.name = name == null ? "" : name;
        this.source = source == null ? "" : source;
        this.resolution = resolution == null ? -1 : resolution;
        this.modifier = modifier == null ? "" : modifier;
    }

    @NotNull
    public Integer getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getSource() {
        return source;
    }

    @NotNull
    public Integer getResolution() {
        return resolution;
    }

    @NotNull
    public String getModifier() {
        return modifier;
    }

    @NotNull
    static Quality2 getDefault() {
        return new Quality2(null, null, null, null, null);
    }

    @Override
    public String toString() {
        return "Quality2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", resolution=" + resolution +
                ", modifier='" + modifier + '\'' +
                '}';
    }
}
