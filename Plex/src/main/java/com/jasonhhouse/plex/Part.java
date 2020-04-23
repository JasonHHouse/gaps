package com.jasonhhouse.plex;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;

public class Part {
    private Integer id;
    private String key;
    private Long duration;
    private String file;
    private Long size;
    private String container;
    private String videoProfile;

    public Part() {
    }

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
