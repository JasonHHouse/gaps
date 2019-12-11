package com.jasonhhouse.gaps;

public class PlexSearch {
    private String movieDbApiKey;
    private String plexToken;
    private String address;
    private String port;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
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
