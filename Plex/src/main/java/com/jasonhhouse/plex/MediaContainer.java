package com.jasonhhouse.plex;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MediaContainer")
public class MediaContainer {

    private List<Video> videos;
    private Integer size;
    private String allowSync;
    private String art;
    private String identifier;
    private Integer librarySectionID;
    private String librarySectionTitle;
    private String librarySectionUUID;
    private String mediaTagPrefix;
    private Long mediaTagVersion;
    private String thumb;
    private String title1;
    private String title2;
    private String viewGroup;
    private Integer viewMode;

    public MediaContainer() {
        videos = new ArrayList<>();
    }

    public Integer getSize() {
        return size;
    }

    @XmlAttribute
    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAllowSync() {
        return allowSync;
    }

    @XmlAttribute
    public void setAllowSync(String allowSync) {
        this.allowSync = allowSync;
    }

    public String getArt() {
        return art;
    }

    @XmlAttribute
    public void setArt(String art) {
        this.art = art;
    }

    public String getIdentifier() {
        return identifier;
    }

    @XmlAttribute
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getLibrarySectionID() {
        return librarySectionID;
    }

    @XmlAttribute
    public void setLibrarySectionID(Integer librarySectionID) {
        this.librarySectionID = librarySectionID;
    }

    public String getLibrarySectionTitle() {
        return librarySectionTitle;
    }

    @XmlAttribute
    public void setLibrarySectionTitle(String librarySectionTitle) {
        this.librarySectionTitle = librarySectionTitle;
    }

    public String getLibrarySectionUUID() {
        return librarySectionUUID;
    }

    @XmlAttribute
    public void setLibrarySectionUUID(String librarySectionUUID) {
        this.librarySectionUUID = librarySectionUUID;
    }

    public String getMediaTagPrefix() {
        return mediaTagPrefix;
    }

    @XmlAttribute
    public void setMediaTagPrefix(String mediaTagPrefix) {
        this.mediaTagPrefix = mediaTagPrefix;
    }

    public Long getMediaTagVersion() {
        return mediaTagVersion;
    }

    @XmlAttribute
    public void setMediaTagVersion(Long mediaTagVersion) {
        this.mediaTagVersion = mediaTagVersion;
    }

    public String getThumb() {
        return thumb;
    }

    @XmlAttribute
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle1() {
        return title1;
    }

    @XmlAttribute
    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    @XmlAttribute
    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getViewGroup() {
        return viewGroup;
    }

    @XmlAttribute
    public void setViewGroup(String viewGroup) {
        this.viewGroup = viewGroup;
    }

    public Integer getViewMode() {
        return viewMode;
    }

    @XmlAttribute
    public void setViewMode(Integer viewMode) {
        this.viewMode = viewMode;
    }

    public List<Video> getVideos() {
        return videos;
    }

    @XmlElement(name = "Video")
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "MediaContainer{" +
                "videos=" + videos +
                ", size=" + size +
                ", allowSync='" + allowSync + '\'' +
                ", art='" + art + '\'' +
                ", identifier='" + identifier + '\'' +
                ", librarySectionID=" + librarySectionID +
                ", librarySectionTitle='" + librarySectionTitle + '\'' +
                ", librarySectionUUID='" + librarySectionUUID + '\'' +
                ", mediaTagPrefix='" + mediaTagPrefix + '\'' +
                ", mediaTagVersion=" + mediaTagVersion +
                ", thumb='" + thumb + '\'' +
                ", title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                ", viewGroup='" + viewGroup + '\'' +
                ", viewMode=" + viewMode +
                '}';
    }
}
