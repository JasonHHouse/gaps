package com.jasonhhouse.Gaps;/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.List;
import java.util.Objects;

public class Gaps {

    private String movieDbApiKey;

    private Boolean writeToFile;

    private String movieDbListId;

    private Boolean searchFromPlex;

    private Integer connectTimeout;

    private Integer writeTimeout;

    private Integer readTimeout;

    private List<String> movieUrls;

    private Boolean searchFromFolder;

    public Gaps() {
    }

    public Gaps(String movieDbApiKey, Boolean writeToFile, String movieDbListId, Boolean searchFromPlex, Integer connectTimeout, Integer writeTimeout, Integer readTimeout, List<String> movieUrls, Boolean searchFromFolder) {
        this.movieDbApiKey = movieDbApiKey;
        this.writeToFile = writeToFile;
        this.movieDbListId = movieDbListId;
        this.searchFromPlex = searchFromPlex;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.movieUrls = movieUrls;
        this.searchFromFolder = searchFromFolder;
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

    public String getMovieDbListId() {
        return movieDbListId;
    }

    public void setMovieDbListId(String movieDbListId) {
        this.movieDbListId = movieDbListId;
    }

    public Boolean getSearchFromPlex() {
        return searchFromPlex;
    }

    public void setSearchFromPlex(Boolean searchFromPlex) {
        this.searchFromPlex = searchFromPlex;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Integer writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public List<String> getMovieUrls() {
        return movieUrls;
    }

    public void setMovieUrls(List<String> movieUrls) {
        this.movieUrls = movieUrls;
    }

    public Boolean getSearchFromFolder() {
        return searchFromFolder;
    }

    public void setSearchFromFolder(Boolean searchFromFolder) {
        this.searchFromFolder = searchFromFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gaps gaps = (Gaps) o;
        return Objects.equals(movieDbApiKey, gaps.movieDbApiKey) &&
                Objects.equals(writeToFile, gaps.writeToFile) &&
                Objects.equals(movieDbListId, gaps.movieDbListId) &&
                Objects.equals(searchFromPlex, gaps.searchFromPlex) &&
                Objects.equals(connectTimeout, gaps.connectTimeout) &&
                Objects.equals(writeTimeout, gaps.writeTimeout) &&
                Objects.equals(readTimeout, gaps.readTimeout) &&
                Objects.equals(movieUrls, gaps.movieUrls) &&
                Objects.equals(searchFromFolder, gaps.searchFromFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieDbApiKey, writeToFile, movieDbListId, searchFromPlex, connectTimeout, writeTimeout, readTimeout, movieUrls, searchFromFolder);
    }

    @Override
    public String toString() {
        return "Gaps{" +
                "movieDbApiKey='" + movieDbApiKey + '\'' +
                ", writeToFile=" + writeToFile +
                ", movieDbListId='" + movieDbListId + '\'' +
                ", searchFromPlex=" + searchFromPlex +
                ", connectTimeout=" + connectTimeout +
                ", writeTimeout=" + writeTimeout +
                ", readTimeout=" + readTimeout +
                ", movieUrls=" + movieUrls +
                ", searchFromFolder=" + searchFromFolder +
                '}';
    }

}
