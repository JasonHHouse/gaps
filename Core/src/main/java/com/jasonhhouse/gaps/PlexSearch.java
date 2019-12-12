package com.jasonhhouse.gaps;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlexSearch {

    @Nullable
    private String movieDbApiKey;

    @Nullable
    private String plexToken;

    @Nullable
    private String address;

    @Nullable
    private int port;

    @NotNull
    private final Map<String, Boolean> libraries;

    public PlexSearch() {
        libraries = new HashMap<>();
    }

    public void setLibrary(@NotNull String library, @NotNull Boolean selected) {
        libraries.put(library, selected);
    }

    public @NotNull Map<String, Boolean> getLibraries() {
        return libraries;
    }

    public @Nullable String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(@NotNull String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

    public @Nullable String getPlexToken() {
        return plexToken;
    }

    public void setPlexToken(@NotNull String plexToken) {
        this.plexToken = plexToken;
    }

    public @Nullable String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "PlexSearch{" +
                "movieDbApiKey='" + movieDbApiKey + '\'' +
                ", plexToken='" + plexToken + '\'' +
                ", address='" + address + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
