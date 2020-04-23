package com.jasonhhouse.gaps;

import javax.xml.bind.annotation.XmlAttribute;

public class Role {
    private String tag;

    public Role() {
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
        return "Role{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
