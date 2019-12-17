package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.List;

public final class PlexSearch {

    private String movieDbApiKey;

    private String plexToken;

    private String address;

    private Integer port;

    private final List<PlexLibrary> libraries;

    public PlexSearch() {
        libraries = new ArrayList<>();
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
