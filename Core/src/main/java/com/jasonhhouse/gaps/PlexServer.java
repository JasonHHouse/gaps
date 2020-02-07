package com.jasonhhouse.gaps;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class PlexServer {

    private String friendlyName;
    private String machineIdentifier;
    private String plexToken;
    private String address;
    private Integer port;
    private Set<PlexLibrary> plexLibraries;

    public PlexServer() {
        plexLibraries = new TreeSet<>();
    }

    public PlexServer(String friendlyName, String machineIdentifier, String plexToken, String address, Integer port) {
        this.friendlyName = friendlyName;
        this.machineIdentifier = machineIdentifier;
        this.plexToken = plexToken;
        this.address = address;
        this.port = port;
        plexLibraries = new TreeSet<>();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public Set<PlexLibrary> getPlexLibraries() {
        return plexLibraries;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexServer that = (PlexServer) o;
        return Objects.equals(machineIdentifier, that.machineIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineIdentifier);
    }

    @Override
    public String toString() {
        return "PlexServer{" +
                "friendlyName='" + friendlyName + '\'' +
                ", machineIdentifier='" + machineIdentifier + '\'' +
                ", plexToken='" + plexToken + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", plexLibraries=" + plexLibraries +
                '}';
    }
}
