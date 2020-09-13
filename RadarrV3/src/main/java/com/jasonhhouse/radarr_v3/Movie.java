/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarr_v3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Movie {
    @NotNull
    private final String title;
    @NotNull
    private final String originalTitle;
    @NotNull
    private final List<AlternateTitle> alternateTitles;
    @NotNull
    private final Integer secondaryYearSourceId;
    @NotNull
    private final String sortTitle;
    @NotNull
    private final Integer sizeOnDisk;
    @NotNull
    private final String status;
    @NotNull
    private final String overview;
    @NotNull
    private final Date inCinemas;
    @NotNull
    private final List<Image> images;
    @NotNull
    private final String website;
    @NotNull
    private final Integer year;
    @NotNull
    private final Boolean hasFile;
    @NotNull
    private final String youTubeTrailerId;
    @NotNull
    private final String studio;
    @NotNull
    private final String path;
    @NotNull
    private final Integer qualityProfileId;
    @NotNull
    private final Boolean monitored;
    @NotNull
    private final String minimumAvailability;
    @NotNull
    private final Boolean isAvailable;
    @NotNull
    private final String folderName;
    @NotNull
    private final Integer runtime;
    @NotNull
    private final String cleanTitle;
    @NotNull
    private final String imdbId;
    @NotNull
    private final Integer tmdbId;
    @NotNull
    private final String titleSlug;
    @NotNull
    private final List<String> genres;
    @NotNull
    private final List<Object> tags;
    @NotNull
    private final Date added;
    @NotNull
    private final Ratings ratings;
    @NotNull
    private final MovieFile movieFile;
    @NotNull
    private final Collection collection;
    @NotNull
    private final Integer id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Movie(@JsonProperty(value = "title") @Nullable String title,
                 @JsonProperty(value = "originalTitle") @Nullable String originalTitle,
                 @JsonProperty(value = "alternateTitles") @Nullable List<AlternateTitle> alternateTitles,
                 @JsonProperty(value = "secondaryYearSourceId") @Nullable Integer secondaryYearSourceId,
                 @JsonProperty(value = "sortTitle") @Nullable String sortTitle,
                 @JsonProperty(value = "sizeOnDisk") @Nullable Integer sizeOnDisk,
                 @JsonProperty(value = "status") @Nullable String status,
                 @JsonProperty(value = "overview") @Nullable String overview,
                 @JsonProperty(value = "inCinemas") @Nullable Date inCinemas,
                 @JsonProperty(value = "images") @Nullable List<Image> images,
                 @JsonProperty(value = "website") @Nullable String website,
                 @JsonProperty(value = "year") @Nullable Integer year,
                 @JsonProperty(value = "hasFile") @Nullable Boolean hasFile,
                 @JsonProperty(value = "youTubeTrailerId") @Nullable String youTubeTrailerId,
                 @JsonProperty(value = "studio") @Nullable String studio,
                 @JsonProperty(value = "path") @Nullable String path,
                 @JsonProperty(value = "qualityProfileId") @Nullable Integer qualityProfileId,
                 @JsonProperty(value = "monitored") @Nullable Boolean monitored,
                 @JsonProperty(value = "minimumAvailability") @Nullable String minimumAvailability,
                 @JsonProperty(value = "isAvailable") @Nullable Boolean isAvailable,
                 @JsonProperty(value = "folderName") @Nullable String folderName,
                 @JsonProperty(value = "runtime") @Nullable Integer runtime,
                 @JsonProperty(value = "cleanTitle") @Nullable String cleanTitle,
                 @JsonProperty(value = "imdbId") @Nullable String imdbId,
                 @JsonProperty(value = "tmdbId") @Nullable Integer tmdbId,
                 @JsonProperty(value = "titleSlug") @Nullable String titleSlug,
                 @JsonProperty(value = "genres") @Nullable List<String> genres,
                 @JsonProperty(value = "tags") @Nullable List<Object> tags,
                 @JsonProperty(value = "added") @Nullable Date added,
                 @JsonProperty(value = "ratings") @Nullable Ratings ratings,
                 @JsonProperty(value = "movieFile") @Nullable MovieFile movieFile,
                 @JsonProperty(value = "collection") @Nullable Collection collection,
                 @JsonProperty(value = "id") @Nullable Integer id) {
        this.title = title == null ? "" : title;
        this.originalTitle = originalTitle == null ? "" : originalTitle;
        this.alternateTitles = alternateTitles == null ? Collections.emptyList() : alternateTitles;
        this.secondaryYearSourceId = secondaryYearSourceId == null ? -1 : secondaryYearSourceId;
        this.sortTitle = sortTitle == null ? "" : sortTitle;
        this.sizeOnDisk = sizeOnDisk == null ? -1 : sizeOnDisk;
        this.status = status == null ? "" : status;
        this.overview = overview == null ? "" : overview;
        this.inCinemas = inCinemas == null ? new Date(0) : inCinemas;
        this.images = images == null ? Collections.emptyList() : images;
        this.website = website == null ? "" : website;
        this.year = year == null ? -1 : year;
        this.hasFile = hasFile != null && hasFile;
        this.youTubeTrailerId = youTubeTrailerId == null ? "" : youTubeTrailerId;
        this.studio = studio == null ? "" : studio;
        this.path = path == null ? "" : path;
        this.qualityProfileId = qualityProfileId == null ? -1 : qualityProfileId;
        this.monitored = monitored != null && monitored;
        this.minimumAvailability = minimumAvailability == null ? "" : minimumAvailability;
        this.isAvailable = isAvailable != null && isAvailable;
        this.folderName = folderName == null ? "" : folderName;
        this.runtime = runtime == null ? -1 : runtime;
        this.cleanTitle = cleanTitle == null ? "" : cleanTitle;
        this.imdbId = imdbId == null ? "" : imdbId;
        this.tmdbId = tmdbId == null ? -1 : tmdbId;
        this.titleSlug = titleSlug == null ? "" : titleSlug;
        this.genres = genres == null ? Collections.emptyList() : genres;
        this.tags = tags == null ? Collections.emptyList() : tags;
        this.added = added == null ? new Date(0) : added;
        this.ratings = ratings == null ? Ratings.getDefault() : ratings;
        this.movieFile = movieFile == null ? MovieFile.getDefault() : movieFile;
        this.collection = collection == null ? Collection.getDefault() : collection;
        this.id = id == null ? -1 : id;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getOriginalTitle() {
        return originalTitle;
    }

    public @NotNull List<AlternateTitle> getAlternateTitles() {
        return alternateTitles;
    }

    public @NotNull Integer getSecondaryYearSourceId() {
        return secondaryYearSourceId;
    }

    public @NotNull String getSortTitle() {
        return sortTitle;
    }

    public @NotNull Integer getSizeOnDisk() {
        return sizeOnDisk;
    }

    public @NotNull String getStatus() {
        return status;
    }

    public @NotNull String getOverview() {
        return overview;
    }

    public @NotNull Date getInCinemas() {
        return inCinemas;
    }

    public @NotNull List<Image> getImages() {
        return images;
    }

    public @NotNull String getWebsite() {
        return website;
    }

    public @NotNull Integer getYear() {
        return year;
    }

    public @NotNull Boolean getHasFile() {
        return hasFile;
    }

    public @NotNull String getYouTubeTrailerId() {
        return youTubeTrailerId;
    }

    public @NotNull String getStudio() {
        return studio;
    }

    public @NotNull String getPath() {
        return path;
    }

    public @NotNull Integer getQualityProfileId() {
        return qualityProfileId;
    }

    public @NotNull Boolean getMonitored() {
        return monitored;
    }

    public @NotNull String getMinimumAvailability() {
        return minimumAvailability;
    }

    public @NotNull Boolean getAvailable() {
        return isAvailable;
    }

    public @NotNull String getFolderName() {
        return folderName;
    }

    public @NotNull Integer getRuntime() {
        return runtime;
    }

    public @NotNull String getCleanTitle() {
        return cleanTitle;
    }

    public @NotNull String getImdbId() {
        return imdbId;
    }

    public @NotNull Integer getTmdbId() {
        return tmdbId;
    }

    public @NotNull String getTitleSlug() {
        return titleSlug;
    }

    public @NotNull List<String> getGenres() {
        return genres;
    }

    public @NotNull List<Object> getTags() {
        return tags;
    }

    public @NotNull Date getAdded() {
        return added;
    }

    public @NotNull Ratings getRatings() {
        return ratings;
    }

    public @NotNull MovieFile getMovieFile() {
        return movieFile;
    }

    public @NotNull Collection getCollection() {
        return collection;
    }

    public @NotNull Integer getId() {
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
