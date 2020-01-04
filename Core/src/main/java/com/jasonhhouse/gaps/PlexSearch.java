/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.list.SetUniqueList;

public final class PlexSearch {

    public static final String MOVIE_DB_API_KEY = "movieDbApiKey";

    public static final String PLEX_TOKEN = "plexToken";

    public static final String ADDRESS = "address";

    public static final String PORT = "port";

    private String movieDbApiKey;

    private String plexToken;

    private String address;

    private Integer port;

    private final SetUniqueList<PlexLibrary> libraries;

    public PlexSearch() {
        libraries = SetUniqueList.setUniqueList(new ArrayList<>());
    }

    public void setLibrarySelected(PlexLibrary plexLibrary) {
        libraries.get(libraries.indexOf(plexLibrary)).setSelected(true);
    }

    public List<PlexLibrary> getLibraries() {
        return libraries;
    }

    public String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

    public String getPlexToken() {
        return plexToken;
    }

    public void setPlexToken(String plexToken) {
        this.plexToken = plexToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "PlexSearch{" +
                "movieDbApiKey='" + movieDbApiKey + '\'' +
                ", plexToken='" + plexToken + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", libraries=" + libraries +
                '}';
    }
}
