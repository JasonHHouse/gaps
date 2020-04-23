package com.jasonhhouse.gaps;

import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Video {

    private Integer ratingKey;
    private String key;
    private String guid;
    private String studio;
    private String type;
    private String title;
    private String titleSort;
    private String contentRating;
    private String summary;
    private Double rating;
    private Integer year;
    private String tagline;
    private String thumb;
    private String art;
    private Long duration;
    private String originallyAvailableAt;
    private Long addedAt;
    private Long updatedAt;
    private String primaryExtra;
    private List<Genre> genres;
    private List<Collection> collections;
    private List<Director> directors;
    private List<Writer> writers;
    private List<Country> counties;
    private List<Role> roles;
    private List<Media> media;

    public Video() {
    }

    public String getKey() {
        return key;
    }

    @XmlAttribute
    public void setKey(String key) {
        this.key = key;
    }

    public String getGuid() {
        return guid;
    }

    @XmlAttribute
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStudio() {
        return studio;
    }

    @XmlAttribute
    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    @XmlAttribute
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSort() {
        return titleSort;
    }

    @XmlAttribute
    public void setTitleSort(String titleSort) {
        this.titleSort = titleSort;
    }

    public String getContentRating() {
        return contentRating;
    }

    @XmlAttribute
    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public List<Collection> getCollections() {
        return collections;
    }
    @XmlElement(name = "Collection")
    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public String getSummary() {
        return summary;
    }

    @XmlAttribute
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getRating() {
        return rating;
    }

    @XmlAttribute
    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getYear() {
        return year;
    }

    @XmlAttribute
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTagline() {
        return tagline;
    }

    @XmlAttribute
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getThumb() {
        return thumb;
    }

    @XmlAttribute
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArt() {
        return art;
    }

    @XmlAttribute
    public void setArt(String art) {
        this.art = art;
    }

    public Long getDuration() {
        return duration;
    }

    @XmlAttribute
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getOriginallyAvailableAt() {
        return originallyAvailableAt;
    }

    @XmlAttribute
    public void setOriginallyAvailableAt(String originallyAvailableAt) {
        this.originallyAvailableAt = originallyAvailableAt;
    }

    public Long getAddedAt() {
        return addedAt;
    }

    @XmlAttribute
    public void setAddedAt(Long addedAt) {
        this.addedAt = addedAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    @XmlAttribute
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrimaryExtra() {
        return primaryExtra;
    }

    @XmlAttribute
    public void setPrimaryExtra(String primaryExtra) {
        this.primaryExtra = primaryExtra;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @XmlElement(name = "Genre")
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    @XmlElement(name = "Director")
    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    @XmlElement(name = "Writer")
    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public List<Country> getCounties() {
        return counties;
    }

    @XmlElement(name = "Country")
    public void setCounties(List<Country> counties) {
        this.counties = counties;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @XmlElement(name = "Role")
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Media> getMedia() {
        return media;
    }

    @XmlElement(name = "Media")
    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Integer getRatingKey() {
        return ratingKey;
    }

    @XmlAttribute
    public void setRatingKey(Integer ratingKey) {
        this.ratingKey = ratingKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(guid, video.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid);
    }

    @Override
    public String toString() {
        return "PlexVideo{" +
                "ratingKey=" + ratingKey +
                ", key='" + key + '\'' +
                ", guid='" + guid + '\'' +
                ", studio='" + studio + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", titleSort='" + titleSort + '\'' +
                ", contentRating='" + contentRating + '\'' +
                ", summary='" + summary + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                ", tagline='" + tagline + '\'' +
                ", thumb='" + thumb + '\'' +
                ", art='" + art + '\'' +
                ", duration=" + duration +
                ", originallyAvailableAt='" + originallyAvailableAt + '\'' +
                ", addedAt=" + addedAt +
                ", updatedAt=" + updatedAt +
                ", primaryExtra='" + primaryExtra + '\'' +
                ", genre=" + genres +
                ", director=" + directors +
                ", writer=" + writers +
                ", country=" + counties +
                ", role=" + roles +
                ", plexMedia=" + media +
                '}';
    }

}