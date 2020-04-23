package com.jasonhhouse.plex;

import javax.xml.bind.annotation.XmlAttribute;

public class Genre {
    private String tag;

    public Genre() {
    }

    public String getTag() {
        return tag;
    }

    @XmlAttribute
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
