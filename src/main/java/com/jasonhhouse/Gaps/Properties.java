/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.Gaps;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "gaps")
@Component
@Validated
public class Properties {

    @NotNull(message = "Plex Movie Urls cannot be null")
    private List<String> movieUrls;

    @NotNull(message = "Movie DB API Key cannot be null")
    private String movieDbApiKey;

    @NotNull(message = "Write to file boolean cannot be null")
    private Boolean writeToFile;

    @NotNull(message = "Plex property cannot be null")
    private PlexProperties plex;

    private String movieDbListId;


    public List<String> getMovieUrls() {
        return movieUrls;
    }

    public void setMovieUrls(List<String> movieUrls) {
        this.movieUrls = movieUrls;
    }

    public String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

    public Boolean getWriteToFile() {
        return writeToFile;
    }

    public void setWriteToFile(Boolean writeToFile) {
        this.writeToFile = writeToFile;
    }

    public PlexProperties getPlex() {
        return plex;
    }

    public void setPlex(PlexProperties plex) {
        this.plex = plex;
    }

    public String getMovieDbListId() {
        return movieDbListId;
    }

    public void setMovieDbListId(String listId) {
        this.movieDbListId = listId;
    }

}
