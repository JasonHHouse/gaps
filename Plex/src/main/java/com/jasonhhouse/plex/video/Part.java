/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.plex.video;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;

public final class Part {

    private Integer id;
    private String key;
    private Long duration;
    private String file;
    private Long size;
    private String container;
    private String videoProfile;

    public Integer getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    @XmlAttribute
    public void setKey(String key) {
        this.key = key;
    }

    public Long getDuration() {
        return duration;
    }

    @XmlAttribute
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFile() {
        return file;
    }

    @XmlAttribute
    public void setFile(String file) {
        this.file = file;
    }

    public Long getSize() {
        return size;
    }

    @XmlAttribute
    public void setSize(Long size) {
        this.size = size;
    }

    public String getContainer() {
        return container;
    }

    @XmlAttribute
    public void setContainer(String container) {
        this.container = container;
    }

    public String getVideoProfile() {
        return videoProfile;
    }

    @XmlAttribute
    public void setVideoProfile(String videoProfile) {
        this.videoProfile = videoProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(id, part.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlexPart{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", duration=" + duration +
                ", file='" + file + '\'' +
                ", size=" + size +
                ", container='" + container + '\'' +
                ", videoProfile='" + videoProfile + '\'' +
                '}';
    }
}
