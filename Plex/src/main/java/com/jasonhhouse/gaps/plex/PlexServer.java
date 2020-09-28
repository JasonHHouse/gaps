/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.jasonhhouse.gaps.plex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PlexServer {

    @NotNull
    @Schema(description = "Libraries that are of type movie in your Plex Server")
    private final Set<PlexLibrary> plexLibraries;

    @NotNull
    @Schema(description = "Human readable way to identify your Plex Server")
    private final String friendlyName;

    @NotNull
    @Schema(description = "UID to identify the Plex Server")
    private final String machineIdentifier;

    @NotNull
    @Schema(required = true, description = "Token for your Plex Server. Found via Plex docs.")
    private final String plexToken;

    @NotNull
    @Schema(required = true, defaultValue = "localhost", description = "Address of your Plex Server")
    private final String address;

    @NotNull
    @Schema(required = true, defaultValue = "32400", description = "Port of your Plex Server")
    private final Integer port;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlexServer(@JsonProperty(value = "friendlyName") @Nullable String friendlyName,
                      @JsonProperty(value = "machineIdentifier") @Nullable String machineIdentifier,
                      @JsonProperty(value = "plexToken") @Nullable String plexToken,
                      @JsonProperty(value = "address") @Nullable String address,
                      @JsonProperty(value = "port") @Nullable Integer port,
                      @JsonProperty(value = "plexLibraries") @Nullable Set<PlexLibrary> plexLibraries) {
        this.friendlyName = friendlyName == null ? "" : friendlyName;
        this.machineIdentifier = machineIdentifier == null ? "" : machineIdentifier;
        this.plexToken = plexToken == null ? "" : plexToken;
        this.address = address == null ? "": address;
        this.port = port == null ? -1 : port;
        this.plexLibraries = plexLibraries == null ? new LinkedHashSet<>() : plexLibraries;
    }

    public @NotNull String getFriendlyName() {
        return friendlyName;
    }

    public @NotNull String getMachineIdentifier() {
        return machineIdentifier;
    }

    public @NotNull Set<PlexLibrary> getPlexLibraries() {
        return plexLibraries;
    }

    public @NotNull String getPlexToken() {
        return plexToken;
    }

    public @NotNull String getAddress() {
        return address;
    }

    public @NotNull Integer getPort() {
        return port;
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
