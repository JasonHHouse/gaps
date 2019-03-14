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

public class FolderProperties {

    @NotNull(message = "Search from folder boolean cannot be null")
    private Boolean searchFromFolder;

    private Boolean recursive;

    private List<String> folders;

    private List<String> movieFormats;

    private String yearRegex;

    public String getYearRegex() {
        return yearRegex;
    }

    public void setYearRegex(String yearRegex) {
        this.yearRegex = yearRegex;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    public Boolean getSearchFromFolder() {
        return searchFromFolder;
    }

    public void setSearchFromFolder(Boolean searchFromFolder) {
        this.searchFromFolder = searchFromFolder;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public List<String> getMovieFormats() {
        return movieFormats;
    }

    public void setMovieFormats(List<String> movieFormats) {
        this.movieFormats = movieFormats;
    }

}
