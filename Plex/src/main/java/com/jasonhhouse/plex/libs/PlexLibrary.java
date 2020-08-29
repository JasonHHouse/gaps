
package com.jasonhhouse.plex.libs;

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
 *         &lt;element ref="{}Location"/>
 *       &lt;/sequence>
 *       &lt;attribute name="allowSync" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="art" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="composite" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="filters" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="refreshing" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="thumb" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="agent" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="scanner" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="updatedAt" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="createdAt" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="scannedAt" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="content" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="directory" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="contentChangedAt" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="hidden" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "location"
})
@XmlRootElement(name = "Directory")
public class PlexLibrary {

    @XmlElement(name = "Location", required = true)
    protected Location location;
    @XmlAttribute(name = "allowSync")
    protected Integer allowSync;
    @XmlAttribute(name = "art")
    protected String art;
    @XmlAttribute(name = "composite")
    protected String composite;
    @XmlAttribute(name = "filters")
    protected Integer filters;
    @XmlAttribute(name = "refreshing")
    protected Integer refreshing;
    @XmlAttribute(name = "thumb")
    protected String thumb;
    @XmlAttribute(name = "key")
    protected Integer key;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "agent")
    protected String agent;
    @XmlAttribute(name = "scanner")
    protected String scanner;
    @XmlAttribute(name = "language")
    protected String language;
    @XmlAttribute(name = "uuid")
    protected String uuid;
    @XmlAttribute(name = "updatedAt")
    protected Integer updatedAt;
    @XmlAttribute(name = "createdAt")
    protected Integer createdAt;
    @XmlAttribute(name = "scannedAt")
    protected Integer scannedAt;
    @XmlAttribute(name = "content")
    protected Integer content;
    @XmlAttribute(name = "directory")
    protected Integer directory;
    @XmlAttribute(name = "contentChangedAt")
    protected Short contentChangedAt;
    @XmlAttribute(name = "hidden")
    protected Integer hidden;

    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     * {@link Location }
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link Location }
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the allowSync property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getAllowSync() {
        return allowSync;
    }

    /**
     * Sets the value of the allowSync property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setAllowSync(Integer value) {
        this.allowSync = value;
    }

    /**
     * Gets the value of the art property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArt() {
        return art;
    }

    /**
     * Sets the value of the art property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArt(String value) {
        this.art = value;
    }

    /**
     * Gets the value of the composite property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComposite() {
        return composite;
    }

    /**
     * Sets the value of the composite property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComposite(String value) {
        this.composite = value;
    }

    /**
     * Gets the value of the filters property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setFilters(Integer value) {
        this.filters = value;
    }

    /**
     * Gets the value of the refreshing property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getRefreshing() {
        return refreshing;
    }

    /**
     * Sets the value of the refreshing property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setRefreshing(Integer value) {
        this.refreshing = value;
    }

    /**
     * Gets the value of the thumb property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * Sets the value of the thumb property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setThumb(String value) {
        this.thumb = value;
    }

    /**
     * Gets the value of the key property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setKey(Integer value) {
        this.key = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the agent property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAgent(String value) {
        this.agent = value;
    }

    /**
     * Gets the value of the scanner property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getScanner() {
        return scanner;
    }

    /**
     * Sets the value of the scanner property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setScanner(String value) {
        this.scanner = value;
    }

    /**
     * Gets the value of the language property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the uuid property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the updatedAt property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the value of the updatedAt property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setUpdatedAt(Integer value) {
        this.updatedAt = value;
    }

    /**
     * Gets the value of the createdAt property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCreatedAt(Integer value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the scannedAt property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getScannedAt() {
        return scannedAt;
    }

    /**
     * Sets the value of the scannedAt property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setScannedAt(Integer value) {
        this.scannedAt = value;
    }

    /**
     * Gets the value of the content property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setContent(Integer value) {
        this.content = value;
    }

    /**
     * Gets the value of the directory property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getDirectory() {
        return directory;
    }

    /**
     * Sets the value of the directory property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setDirectory(Integer value) {
        this.directory = value;
    }

    /**
     * Gets the value of the contentChangedAt property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Short getContentChangedAt() {
        return contentChangedAt;
    }

    /**
     * Sets the value of the contentChangedAt property.
     *
     * @param value allowed object is
     *              {@link Short }
     */
    public void setContentChangedAt(Short value) {
        this.contentChangedAt = value;
    }

    /**
     * Gets the value of the hidden property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getHidden() {
        return hidden;
    }

    /**
     * Sets the value of the hidden property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setHidden(Integer value) {
        this.hidden = value;
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "location=" + location +
                ", allowSync=" + allowSync +
                ", art='" + art + '\'' +
                ", composite='" + composite + '\'' +
                ", filters=" + filters +
                ", refreshing=" + refreshing +
                ", thumb='" + thumb + '\'' +
                ", key=" + key +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", agent='" + agent + '\'' +
                ", scanner='" + scanner + '\'' +
                ", language='" + language + '\'' +
                ", uuid='" + uuid + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", scannedAt=" + scannedAt +
                ", content=" + content +
                ", directory=" + directory +
                ", contentChangedAt=" + contentChangedAt +
                ", hidden=" + hidden +
                '}';
    }
}
