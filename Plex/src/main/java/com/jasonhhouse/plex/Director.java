package com.jasonhhouse.plex;

import javax.xml.bind.annotation.XmlAttribute;

public class Director {
    private String tag;

    public Director() {
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
        return "Director{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
