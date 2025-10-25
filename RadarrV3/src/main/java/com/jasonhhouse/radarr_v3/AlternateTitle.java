/*
 *
 *  Copyright 2025 Jason H House
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
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlternateTitle {
    @NotNull
    private final String sourceType;
    @NotNull
    private final Integer movieId;
    @NotNull
    private final String title;
    @NotNull
    private final Integer sourceId;
    @NotNull
    private final Integer votes;
    @NotNull
    private final Integer voteCount;
    @NotNull
    private final Language language;
    @NotNull
    private final Integer id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AlternateTitle(@JsonProperty(value = "sourceType") @Nullable String sourceType,
                          @JsonProperty(value = "movieId") @Nullable Integer movieId,
                          @JsonProperty(value = "title") @Nullable String title,
                          @JsonProperty(value = "sourceId") @Nullable Integer sourceId,
                          @JsonProperty(value = "votes") @Nullable Integer votes,
                          @JsonProperty(value = "voteCount") @Nullable Integer voteCount,
                          @JsonProperty(value = "language") @Nullable Language language,
                          @JsonProperty(value = "id") @Nullable Integer id) {
        this.sourceType = sourceType == null ? "" : sourceType;
        this.movieId = movieId == null ? -1 : movieId;
        this.title = title == null ? "" : title;
        this.sourceId = sourceId == null ? -1 : sourceId;
        this.votes = votes == null ? -1 : votes;
        this.voteCount = voteCount == null ? -1 : voteCount;
        this.language = language == null ? Language.getDefault() : language;
        this.id = id == null ? -1 : id;
    }

    public @NotNull String getSourceType() {
        return sourceType;
    }

    public @NotNull Integer getMovieId() {
        return movieId;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull Integer getSourceId() {
        return sourceId;
    }

    public @NotNull Integer getVotes() {
        return votes;
    }

    public @NotNull Integer getVoteCount() {
        return voteCount;
    }

    public @NotNull Language getLanguage() {
        return language;
    }

    public @NotNull Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlternateTitle that = (AlternateTitle) o;
        return sourceType.equals(that.sourceType) &&
                movieId.equals(that.movieId) &&
                title.equals(that.title) &&
                sourceId.equals(that.sourceId) &&
                votes.equals(that.votes) &&
                voteCount.equals(that.voteCount) &&
                language.equals(that.language) &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, movieId, title, sourceId, votes, voteCount, language, id);
    }

    @Override
    public String toString() {
        return "AlternateTitle{" +
                "sourceType='" + sourceType + '\'' +
                ", movieId=" + movieId +
                ", title='" + title + '\'' +
                ", sourceId=" + sourceId +
                ", votes=" + votes +
                ", voteCount=" + voteCount +
                ", language=" + language +
                ", id=" + id +
                '}';
    }
}
