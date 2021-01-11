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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jasonhhouse.plex.libs.PlexLibrary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PlexServer {

    @Schema(description = "Libraries that are of type movie in your Plex Server")
    private final List<PlexLibrary> plexLibraries;
    @Schema(description = "Human readable way to identify your Plex Server")
    private String friendlyName;
    @Schema(description = "UID to identify the Plex Server")
    private String machineIdentifier;
    @Schema(required = true, description = "Token for your Plex Server. Found via Plex docs.")
    private String plexToken;
    @Schema(required = true, defaultValue = "localhost", description = "Address of your Plex Server")
    private String address;
    @Schema(required = true, defaultValue = "32400", description = "Port of your Plex Server")
    private Integer port;

    public PlexServer() {
        plexLibraries = new ArrayList<>();
    }

    public PlexServer(String friendlyName, String machineIdentifier, String plexToken, String address, Integer port) {
        this.friendlyName = friendlyName;
        this.machineIdentifier = machineIdentifier;
        this.plexToken = plexToken;
        this.address = address;
        this.port = port;
        plexLibraries = new ArrayList<>();
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

    public List<PlexLibrary> getPlexLibraries() {
        Collections.sort(plexLibraries);
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
