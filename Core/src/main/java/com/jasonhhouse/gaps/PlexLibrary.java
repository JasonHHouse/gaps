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

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class PlexLibrary implements Comparable<PlexLibrary> {
    private @NotNull Integer key;
    private @NotNull String title;
    private @NotNull String machineIdentifier;
    private @NotNull Boolean selected;

    public PlexLibrary() {
        key = -1;
        title = "";
        machineIdentifier = "";
        selected = false;
    }

    public PlexLibrary(@NotNull Integer key, @NotNull String title, @NotNull String machineIdentifier, @NotNull Boolean selected) {
        this.key = key;
        this.title = title;
        this.machineIdentifier = machineIdentifier;
        this.selected = selected;
    }

    public @NotNull Integer getKey() {
        return key;
    }

    public void setKey(@NotNull Integer key) {
        this.key = key;
    }

    public @NotNull String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(@NotNull String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public @NotNull Boolean getSelected() {
        return selected;
    }

    public void setSelected(@NotNull Boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexLibrary that = (PlexLibrary) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(title, that.title) &&
                Objects.equals(machineIdentifier, that.machineIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, title, machineIdentifier);
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "key=" + key +
                ", title='" + title + '\'' +
                ", machineIdentifier='" + machineIdentifier + '\'' +
                ", selected=" + selected +
                '}';
    }

    @Override
    public int compareTo(@NotNull PlexLibrary o) {
        return this.title.compareTo(o.title);
    }
}
