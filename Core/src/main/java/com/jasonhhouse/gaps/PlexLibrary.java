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

public class PlexLibrary implements Comparable<PlexLibrary> {
    private Integer key;
    private String title;
    private String machineIdentifier;
    private Boolean selected;

    public PlexLibrary() {
    }

    public PlexLibrary(Integer key, String title, String machineIdentifier, Boolean selected) {
        this.key = key;
        this.title = title;
        this.machineIdentifier = machineIdentifier;
        this.selected = selected;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
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
