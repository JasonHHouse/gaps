/*
 *  Copyright 2025 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.plex.libs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Directory" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="allowSync" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mediaTagPrefix" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mediaTagVersion" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="title1" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "plexLibrary"
})
@XmlRootElement(name = "MediaContainer")
public class MediaContainer {

    @XmlElement(name = "Directory")
    protected List<PlexLibrary> plexLibrary;
    @XmlAttribute(name = "size")
    protected Byte size;
    @XmlAttribute(name = "allowSync")
    protected Byte allowSync;
    @XmlAttribute(name = "identifier")
    protected String identifier;
    @XmlAttribute(name = "mediaTagPrefix")
    protected String mediaTagPrefix;
    @XmlAttribute(name = "mediaTagVersion")
    protected Integer mediaTagVersion;
    @XmlAttribute(name = "title1")
    protected String title1;

    /**
     * Gets the value of the directory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirectory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlexLibrary }
     * 
     * 
     */
    public List<PlexLibrary> getPlexLibraries() {
        if (plexLibrary == null) {
            plexLibrary = new ArrayList<>();
        }
        return this.plexLibrary;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setSize(Byte value) {
        this.size = value;
    }

    /**
     * Gets the value of the allowSync property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getAllowSync() {
        return allowSync;
    }

    /**
     * Sets the value of the allowSync property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setAllowSync(Byte value) {
        this.allowSync = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the mediaTagPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaTagPrefix() {
        return mediaTagPrefix;
    }

    /**
     * Sets the value of the mediaTagPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaTagPrefix(String value) {
        this.mediaTagPrefix = value;
    }

    /**
     * Gets the value of the mediaTagVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMediaTagVersion() {
        return mediaTagVersion;
    }

    /**
     * Sets the value of the mediaTagVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMediaTagVersion(Integer value) {
        this.mediaTagVersion = value;
    }

    /**
     * Gets the value of the title1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle1() {
        return title1;
    }

    /**
     * Sets the value of the title1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle1(String value) {
        this.title1 = value;
    }

    @Override
    public String toString() {
        return "MediaContainer{" +
                "plexLibrary=" + plexLibrary +
                ", size=" + size +
                ", allowSync=" + allowSync +
                ", identifier='" + identifier + '\'' +
                ", mediaTagPrefix='" + mediaTagPrefix + '\'' +
                ", mediaTagVersion=" + mediaTagVersion +
                ", title1='" + title1 + '\'' +
                '}';
    }
}
