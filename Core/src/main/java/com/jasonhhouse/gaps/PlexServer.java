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

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public final class PlexServer {

    private String friendlyName;
    private String machineIdentifier;
    private String plexToken;
    private String address;
    private Integer port;
    private final Set<PlexLibrary> plexLibraries;

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
