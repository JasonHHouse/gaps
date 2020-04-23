package com.jasonhhouse.gaps;

import javax.xml.bind.annotation.XmlAttribute;

public class Collection {
    private String tag;

    public Collection() {
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
        return "Collection{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
