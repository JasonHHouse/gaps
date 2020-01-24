package com.jasonhhouse.gaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.list.SetUniqueList;

public class PlexServer {

    private final String friendlyName;
    private final String machineIdentifier;
    private final SetUniqueList<PlexLibrary> plexLibraries;

    public PlexServer(String friendlyName, String machineIdentifier) {
        this.friendlyName = friendlyName;
        this.machineIdentifier = machineIdentifier;
        plexLibraries = SetUniqueList.setUniqueList(new ArrayList<>());
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public List<PlexLibrary> getPlexLibraries() {
        return plexLibraries;
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
                ", plexLibraries=" + plexLibraries +
                '}';
    }
}
