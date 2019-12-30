package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.Collections;
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
