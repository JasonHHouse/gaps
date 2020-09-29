/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.plex.video;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Entity
public final class Media {

    @Id
    private Integer id;
    private Integer duration;
    private Integer bitrate;
    private Short width;
    private Short height;
    private Float aspectRatio;
    private Float audioChannels;
    private String audioCodec;
    private String videoCodec;
    private String videoResolution;
    private String container;
    private String videoProfile;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Part> parts;

    public Integer getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    @XmlAttribute
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    @XmlAttribute
    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Short getWidth() {
        return width;
    }

    @XmlAttribute
    public void setWidth(Short width) {
        this.width = width;
    }

    public Short getHeight() {
        return height;
    }

    @XmlAttribute
    public void setHeight(Short height) {
        this.height = height;
    }

    public Float getAspectRatio() {
        return aspectRatio;
    }

    @XmlAttribute
    public void setAspectRatio(Float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Float getAudioChannels() {
        return audioChannels;
    }

    @XmlAttribute
    public void setAudioChannels(Float audioChannels) {
        this.audioChannels = audioChannels;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    @XmlAttribute
    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    @XmlAttribute
    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    @XmlAttribute
    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
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

    public List<Part> getParts() {
        return parts;
    }

    @XmlElement(name = "Part")
    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlexMedia{" +
                "id=" + id +
                ", duration=" + duration +
                ", bitrate=" + bitrate +
                ", width=" + width +
                ", height=" + height +
                ", aspectRatio=" + aspectRatio +
                ", audioChannels=" + audioChannels +
                ", audioCodec='" + audioCodec + '\'' +
                ", videoCodec='" + videoCodec + '\'' +
                ", videoResolution=" + videoResolution +
                ", container='" + container + '\'' +
                ", videoProfile='" + videoProfile + '\'' +
                ", plexParts=" + parts +
                '}';
    }
}

