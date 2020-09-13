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
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaInfo {
    @NotNull
    private final String audioAdditionalFeatures;
    @NotNull
    private final Integer audioBitrate;
    @NotNull
    private final Double audioChannels;
    @NotNull
    private final String audioCodec;
    @NotNull
    private final String audioLanguages;
    @NotNull
    private final Integer audioStreamCount;
    @NotNull
    private final Integer videoBitDepth;
    @NotNull
    private final Integer videoBitrate;
    @NotNull
    private final String videoCodec;
    @NotNull
    private final Double videoFps;
    @NotNull
    private final String resolution;
    @NotNull
    private final String runTime;
    @NotNull
    private final String scanType;
    @NotNull
    private final String subtitles;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MediaInfo(@JsonProperty(value = "audioAdditionalFeatures") @Nullable String audioAdditionalFeatures,
                     @JsonProperty(value = "audioBitrate") @Nullable Integer audioBitrate,
                     @JsonProperty(value = "audioChannels") @Nullable Double audioChannels,
                     @JsonProperty(value = "audioCodec") @Nullable String audioCodec,
                     @JsonProperty(value = "audioLanguages") @Nullable String audioLanguages,
                     @JsonProperty(value = "audioStreamCount") @Nullable Integer audioStreamCount,
                     @JsonProperty(value = "videoBitDepth") @Nullable Integer videoBitDepth,
                     @JsonProperty(value = "videoBitrate") @Nullable Integer videoBitrate,
                     @JsonProperty(value = "videoCodec") @Nullable String videoCodec,
                     @JsonProperty(value = "videoFps") @Nullable Double videoFps,
                     @JsonProperty(value = "resolution") @Nullable String resolution,
                     @JsonProperty(value = "runTime") @Nullable String runTime,
                     @JsonProperty(value = "scanType") @Nullable String scanType,
                     @JsonProperty(value = "subtitles") @Nullable String subtitles) {
        this.audioAdditionalFeatures = audioAdditionalFeatures == null ? "" : audioAdditionalFeatures;
        this.audioBitrate = audioBitrate == null ? -1 : audioBitrate;
        this.audioChannels = audioChannels == null ? -1.0 : audioChannels;
        this.audioCodec = audioCodec == null ? "" : audioCodec;
        this.audioLanguages = audioLanguages == null ? "" : audioLanguages;
        this.audioStreamCount = audioStreamCount == null ? -1 : audioStreamCount;
        this.videoBitDepth = videoBitDepth == null ? -1 : videoBitDepth;
        this.videoBitrate = videoBitrate == null ? -1 : videoBitrate;
        this.videoCodec = videoCodec == null ? "" : videoCodec;
        this.videoFps = videoFps == null ? -1 : videoFps;
        this.resolution = resolution == null ? "" : resolution;
        this.runTime = runTime == null ? "" : runTime;
        this.scanType = scanType == null ? "" : scanType;
        this.subtitles = subtitles == null ? "" : subtitles;
    }

    public @NotNull String getAudioAdditionalFeatures() {
        return audioAdditionalFeatures;
    }

    public @NotNull Integer getAudioBitrate() {
        return audioBitrate;
    }

    public @NotNull Double getAudioChannels() {
        return audioChannels;
    }

    public @NotNull String getAudioCodec() {
        return audioCodec;
    }

    public @NotNull String getAudioLanguages() {
        return audioLanguages;
    }

    public @NotNull Integer getAudioStreamCount() {
        return audioStreamCount;
    }

    public @NotNull Integer getVideoBitDepth() {
        return videoBitDepth;
    }

    public @NotNull Integer getVideoBitrate() {
        return videoBitrate;
    }

    public @NotNull String getVideoCodec() {
        return videoCodec;
    }

    public @NotNull Double getVideoFps() {
        return videoFps;
    }

    public @NotNull String getResolution() {
        return resolution;
    }

    public @NotNull String getRunTime() {
        return runTime;
    }

    public @NotNull String getScanType() {
        return scanType;
    }

    public @NotNull String getSubtitles() {
        return subtitles;
    }

    @NotNull
    static MediaInfo getDefault() {
        return new MediaInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaInfo mediaInfo = (MediaInfo) o;
        return audioAdditionalFeatures.equals(mediaInfo.audioAdditionalFeatures) &&
                audioBitrate.equals(mediaInfo.audioBitrate) &&
                audioChannels.equals(mediaInfo.audioChannels) &&
                audioCodec.equals(mediaInfo.audioCodec) &&
                audioLanguages.equals(mediaInfo.audioLanguages) &&
                audioStreamCount.equals(mediaInfo.audioStreamCount) &&
                videoBitDepth.equals(mediaInfo.videoBitDepth) &&
                videoBitrate.equals(mediaInfo.videoBitrate) &&
                videoCodec.equals(mediaInfo.videoCodec) &&
                videoFps.equals(mediaInfo.videoFps) &&
                resolution.equals(mediaInfo.resolution) &&
                runTime.equals(mediaInfo.runTime) &&
                scanType.equals(mediaInfo.scanType) &&
                subtitles.equals(mediaInfo.subtitles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audioAdditionalFeatures, audioBitrate, audioChannels, audioCodec, audioLanguages, audioStreamCount, videoBitDepth, videoBitrate, videoCodec, videoFps, resolution, runTime, scanType, subtitles);
    }

    @Override
    public String toString() {
        return "MediaInfo{" +
                "audioAdditionalFeatures='" + audioAdditionalFeatures + '\'' +
                ", audioBitrate=" + audioBitrate +
                ", audioChannels=" + audioChannels +
                ", audioCodec='" + audioCodec + '\'' +
                ", audioLanguages='" + audioLanguages + '\'' +
                ", audioStreamCount=" + audioStreamCount +
                ", videoBitDepth=" + videoBitDepth +
                ", videoBitrate=" + videoBitrate +
                ", videoCodec='" + videoCodec + '\'' +
                ", videoFps=" + videoFps +
                ", resolution='" + resolution + '\'' +
                ", runTime='" + runTime + '\'' +
                ", scanType='" + scanType + '\'' +
                ", subtitles='" + subtitles + '\'' +
                '}';
    }
}
