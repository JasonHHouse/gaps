/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarrV3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Movie {
    private final String title;
    private final String originalTitle;
    private final List<AlternateTitle> alternateTitles;
    private final Integer secondaryYearSourceId;
    private final String sortTitle;
    private final Integer sizeOnDisk;
    private final String status;
    private final String overview;
    private final Date inCinemas;
    private final List<Image> images;
    private final String website;
    private final Integer year;
    private final Boolean hasFile;
    private final String youTubeTrailerId;
    private final String studio;
    private final String path;
    private final Integer qualityProfileId;
    private final Boolean monitored;
    private final String minimumAvailability;
    private final Boolean isAvailable;
    private final String folderName;
    private final Integer runtime;
    private final String cleanTitle;
    private final String imdbId;
    private final Integer tmdbId;
    private final String titleSlug;
    private final List<String> genres;
    private final List<Object> tags;
    private final Date added;
    private final Ratings ratings;
    private final MovieFile movieFile;
    private final Collection collection;
    private final Integer id;

    public Movie(String title, String originalTitle, List<AlternateTitle> alternateTitles, Integer secondaryYearSourceId, String sortTitle, Integer sizeOnDisk, String status,
                 String overview, Date inCinemas, List<Image> images, String website, Integer year, Boolean hasFile, String youTubeTrailerId, String studio, String path,
                 Integer qualityProfileId, Boolean monitored, String minimumAvailability, Boolean isAvailable, String folderName, Integer runtime, String cleanTitle, String imdbId,
                 Integer tmdbId, String titleSlug, List<String> genres, List<Object> tags, Date added, Ratings ratings, MovieFile movieFile, Collection collection, Integer id) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.alternateTitles = alternateTitles;
        this.secondaryYearSourceId = secondaryYearSourceId;
        this.sortTitle = sortTitle;
        this.sizeOnDisk = sizeOnDisk;
        this.status = status;
        this.overview = overview;
        this.inCinemas = inCinemas;
        this.images = images;
        this.website = website;
        this.year = year;
        this.hasFile = hasFile;
        this.youTubeTrailerId = youTubeTrailerId;
        this.studio = studio;
        this.path = path;
        this.qualityProfileId = qualityProfileId;
        this.monitored = monitored;
        this.minimumAvailability = minimumAvailability;
        this.isAvailable = isAvailable;
        this.folderName = folderName;
        this.runtime = runtime;
        this.cleanTitle = cleanTitle;
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.titleSlug = titleSlug;
        this.genres = genres;
        this.tags = tags;
        this.added = added;
        this.ratings = ratings;
        this.movieFile = movieFile;
        this.collection = collection;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<AlternateTitle> getAlternateTitles() {
        return alternateTitles;
    }

    public Integer getSecondaryYearSourceId() {
        return secondaryYearSourceId;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public Integer getSizeOnDisk() {
        return sizeOnDisk;
    }

    public String getStatus() {
        return status;
    }

    public String getOverview() {
        return overview;
    }

    public Date getInCinemas() {
        return inCinemas;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getWebsite() {
        return website;
    }

    public Integer getYear() {
        return year;
    }

    public Boolean getHasFile() {
        return hasFile;
    }

    public String getYouTubeTrailerId() {
        return youTubeTrailerId;
    }

    public String getStudio() {
        return studio;
    }

    public String getPath() {
        return path;
    }

    public Integer getQualityProfileId() {
        return qualityProfileId;
    }

    public Boolean getMonitored() {
        return monitored;
    }

    public String getMinimumAvailability() {
        return minimumAvailability;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public String getFolderName() {
        return folderName;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getCleanTitle() {
        return cleanTitle;
    }

    public String getImdbId() {
        return imdbId;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Object> getTags() {
        return tags;
    }

    public Date getAdded() {
        return added;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public MovieFile getMovieFile() {
        return movieFile;
    }

    public Collection getCollection() {
        return collection;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(imdbId, movie.imdbId) &&
                Objects.equals(tmdbId, movie.tmdbId) &&
                Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId, tmdbId, id);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", alternateTitles=" + alternateTitles +
                ", secondaryYearSourceId=" + secondaryYearSourceId +
                ", sortTitle='" + sortTitle + '\'' +
                ", sizeOnDisk=" + sizeOnDisk +
                ", status='" + status + '\'' +
                ", overview='" + overview + '\'' +
                ", inCinemas=" + inCinemas +
                ", images=" + images +
                ", website='" + website + '\'' +
                ", year=" + year +
                ", hasFile=" + hasFile +
                ", youTubeTrailerId='" + youTubeTrailerId + '\'' +
                ", studio='" + studio + '\'' +
                ", path='" + path + '\'' +
                ", qualityProfileId=" + qualityProfileId +
                ", monitored=" + monitored +
                ", minimumAvailability='" + minimumAvailability + '\'' +
                ", isAvailable=" + isAvailable +
                ", folderName='" + folderName + '\'' +
                ", runtime=" + runtime +
                ", cleanTitle='" + cleanTitle + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", tmdbId=" + tmdbId +
                ", titleSlug='" + titleSlug + '\'' +
                ", genres=" + genres +
                ", tags=" + tags +
                ", added=" + added +
                ", ratings=" + ratings +
                ", movieFile=" + movieFile +
                ", collection=" + collection +
                ", id=" + id +
                '}';
    }
}
