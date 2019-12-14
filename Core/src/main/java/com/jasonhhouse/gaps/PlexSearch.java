package com.jasonhhouse.gaps;

import java.util.HashMap;
import java.util.Map;

public final class PlexSearch {

    private String movieDbApiKey;

    private String plexToken;

    private String address;

    private Integer port;

    private final Map<String, Boolean> libraries;

    public PlexSearch() {
        libraries = new HashMap<>();
    }

    public void setLibrary(String library, Boolean selected) {
        libraries.put(library, selected);
    }

    public Map<String, Boolean> getLibraries() {
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
