/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.plex.video;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Entity
public final class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<Genre> genres;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Collection> collections;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Director> directors;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Writer> writers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Country> counties;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Media> media;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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