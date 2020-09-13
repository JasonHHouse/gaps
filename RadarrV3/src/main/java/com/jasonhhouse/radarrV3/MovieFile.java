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
public final class MovieFile {
    @NotNull
    private final Integer movieId;
    @NotNull
    private final String relativePath;
    @NotNull
    private final String path;
    @NotNull
    private final Integer size;
    @NotNull
    private final Date dateAdded;
    @NotNull
    private final Integer indexerFlags;
    @NotNull
    private final Quality quality;
    @NotNull
    private final MediaInfo mediaInfo;
    @NotNull
    private final Boolean qualityCutoffNotMet;
    @NotNull
    private final List<Language> languages;
    @NotNull
    private final Integer id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovieFile(@JsonProperty(value = "movieId") @Nullable Integer movieId,
                     @JsonProperty(value = "relativePath") @Nullable String relativePath,
                     @JsonProperty(value = "path") @Nullable String path,
                     @JsonProperty(value = "size") @Nullable Integer size,
                     @JsonProperty(value = "dateAdded") @Nullable Date dateAdded,
                     @JsonProperty(value = "indexerFlags") @Nullable Integer indexerFlags,
                     @JsonProperty(value = "quality") @Nullable Quality quality,
                     @JsonProperty(value = "mediaInfo") @Nullable MediaInfo mediaInfo,
                     @JsonProperty(value = "qualityCutoffNotMet") @Nullable Boolean qualityCutoffNotMet,
                     @JsonProperty(value = "languages") @Nullable List<Language> languages,
                     @JsonProperty(value = "id") @Nullable Integer id) {
        this.movieId = movieId == null ? -1 : movieId;
        this.relativePath = relativePath == null ? "" : relativePath;
        this.path = path == null ? "" : path;
        this.size = size == null ? -1 : size;
        this.dateAdded = dateAdded == null ? new Date(0) : dateAdded;
        this.indexerFlags = indexerFlags == null ? -1 : indexerFlags;
        this.quality = quality == null ? Quality.getDefault() : quality;
        this.mediaInfo = mediaInfo == null ? MediaInfo.getDefault() : mediaInfo;
        this.qualityCutoffNotMet = qualityCutoffNotMet != null && qualityCutoffNotMet;
        this.languages = languages == null ? Collections.emptyList() : languages;
        this.id = id == null ? -1 : id;
    }

    public @NotNull Integer getMovieId() {
        return movieId;
    }

    public @NotNull String getRelativePath() {
        return relativePath;
    }

    public @NotNull String getPath() {
        return path;
    }

    public @NotNull Integer getSize() {
        return size;
    }

    public @NotNull Date getDateAdded() {
        return dateAdded;
    }

    public @NotNull Integer getIndexerFlags() {
        return indexerFlags;
    }

    public @NotNull Quality getQuality() {
        return quality;
    }

    public @NotNull MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public @NotNull Boolean getQualityCutoffNotMet() {
        return qualityCutoffNotMet;
    }

    public @NotNull List<Language> getLanguages() {
        return languages;
    }

    public @NotNull Integer getId() {
        return id;
    }

    @NotNull
    static MovieFile getDefault() {
        return new MovieFile(null, null,null, null,null, null,null, null,null, null,null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieFile movieFile = (MovieFile) o;
        return movieId.equals(movieFile.movieId) &&
                relativePath.equals(movieFile.relativePath) &&
                path.equals(movieFile.path) &&
                size.equals(movieFile.size) &&
                dateAdded.equals(movieFile.dateAdded) &&
                indexerFlags.equals(movieFile.indexerFlags) &&
                quality.equals(movieFile.quality) &&
                mediaInfo.equals(movieFile.mediaInfo) &&
                qualityCutoffNotMet.equals(movieFile.qualityCutoffNotMet) &&
                languages.equals(movieFile.languages) &&
                id.equals(movieFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, relativePath, path, size, dateAdded, indexerFlags, quality, mediaInfo, qualityCutoffNotMet, languages, id);
    }

    @Override
    public String toString() {
        return "MovieFile{" +
                "movieId=" + movieId +
                ", relativePath='" + relativePath + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", dateAdded=" + dateAdded +
                ", indexerFlags=" + indexerFlags +
                ", quality=" + quality +
                ", mediaInfo=" + mediaInfo +
                ", qualityCutoffNotMet=" + qualityCutoffNotMet +
                ", languages=" + languages +
                ", id=" + id +
                '}';
    }
}
