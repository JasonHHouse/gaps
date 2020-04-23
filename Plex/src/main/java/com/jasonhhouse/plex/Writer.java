package com.jasonhhouse.plex;

import javax.xml.bind.annotation.XmlAttribute;

public class Writer {
    private String tag;

    public Writer() {
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
        return "Writer{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
