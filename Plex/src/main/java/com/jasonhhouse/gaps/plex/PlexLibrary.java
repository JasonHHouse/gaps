/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.plex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PlexLibrary {

    @NotNull
    private final Integer key;

    @NotNull
    private final String scanner;

    @NotNull
    private final String title;

    @NotNull
    private final String type;

    @NotNull
    private final Boolean enabled;

    @NotNull
    private final Boolean defaultLibrary;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlexLibrary(@JsonProperty(value = "key") @Nullable Integer key,
                       @JsonProperty(value = "scanner") @Nullable String scanner,
                       @JsonProperty(value = "title") @Nullable String title,
                       @JsonProperty(value = "type") @Nullable String type,
                       @JsonProperty(value = "enabled") @Nullable Boolean enabled,
                       @JsonProperty(value = "defaultLibrary") @Nullable Boolean defaultLibrary) {
        this.key = key == null ? -1 : key;
        this.scanner = scanner == null ? "" : scanner;
        this.title = title == null ? "" : title;
        this.type = type == null ? "" : type;
        this.enabled = enabled == null || enabled;
        this.defaultLibrary = defaultLibrary != null && defaultLibrary;
    }

    public @NotNull Integer getKey() {
        return key;
    }

    public @NotNull String getScanner() {
        return scanner;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull Boolean getEnabled() {
        return enabled;
    }

    public @NotNull Boolean getDefaultLibrary() {
        return defaultLibrary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexLibrary that = (PlexLibrary) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(scanner, that.scanner) &&
                Objects.equals(title, that.title) &&
                Objects.equals(type, that.type) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(defaultLibrary, that.defaultLibrary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, scanner, title, type, enabled, defaultLibrary);
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "key=" + key +
                ", scanner='" + scanner + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", enabled=" + enabled +
                ", defaultLibrary=" + defaultLibrary +
                '}';
    }
}
