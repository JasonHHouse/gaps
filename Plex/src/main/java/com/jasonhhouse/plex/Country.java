package com.jasonhhouse.plex;

import javax.xml.bind.annotation.XmlAttribute;

public class Country {
    private String tag;

    public Country() {
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
        return "Country{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
