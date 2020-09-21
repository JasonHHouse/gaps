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
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {
    @NotNull
    private final String coverType;
    @NotNull
    private final String url;
    @NotNull
    private final String remoteUrl;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Image(@JsonProperty(value = "coverType") @Nullable String coverType,
                 @JsonProperty(value = "url") @Nullable String url,
                 @JsonProperty(value = "remoteUrl") @Nullable String remoteUrl) {
        this.coverType = coverType == null ? "" : coverType;
        this.url = url == null ? "" : url;
        this.remoteUrl = remoteUrl == null ? "" : remoteUrl;
    }

    @NotNull
    static Image getDefault() {
        return new Image(null, null, null);
    }

    public @NotNull String getCoverType() {
        return coverType;
    }

    public @NotNull String getUrl() {
        return url;
    }

    public @NotNull String getRemoteUrl() {
        return remoteUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return coverType.equals(image.coverType) &&
                url.equals(image.url) &&
                remoteUrl.equals(image.remoteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coverType, url, remoteUrl);
    }

    @Override
    public String toString() {
        return "Image{" +
                "coverType='" + coverType + '\'' +
                ", url='" + url + '\'' +
                ", remoteUrl='" + remoteUrl + '\'' +
                '}';
    }
}
