package com.jasonhhouse.Gaps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    @Value("${plexToken}")
    private String plexToken;

    @Value("${plexIpAddress}")
    private String plexIpAddress;

    @Value("${plexPort}")
    private Integer plexPort;

    @Value("${outputFolder}")
    private String outputFolder;

    @Value("${movieDbApiKey}")
    private String movieDbApiKey;

    public Properties() {
    }

    public String getPlexIpAddress() {
        return plexIpAddress;
    }

    public void setPlexIpAddress(String plexIpAddress) {
        this.plexIpAddress = plexIpAddress;
    }

    public Integer getPlexPort() {
        return plexPort;
    }

    public void setPlexPort(Integer plexPort) {
        this.plexPort = plexPort;
    }

    public String getPlexToken() {
        return plexToken;
    }

    public void setPlexToken(String plexToken) {
        this.plexToken = plexToken;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

}
