package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexServer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RssService {

    private final IoService ioService;

    @Autowired
    public RssService(IoService ioService) {
        this.ioService = ioService;
    }

    @NotNull
    public Map<PlexLibrary, PlexServer> foundAnyRssFeeds() {
        Map<PlexLibrary, PlexServer> plexServerMap = new HashMap<>();
        List<PlexServer> plexServers = ioService.readPlexConfiguration();
        if (CollectionUtils.isEmpty(plexServers)) {
            return Collections.emptyMap();
        }

        for (PlexServer plexServer : plexServers) {
            if (CollectionUtils.isEmpty(plexServer.getPlexLibraries())) {
                continue;
            }

            for (PlexLibrary plexLibrary : plexServer.getPlexLibraries()) {
                if (ioService.doesRssFileExist(plexLibrary.getMachineIdentifier(), plexLibrary.getKey())) {
                    plexServerMap.put(plexLibrary, plexServer);
                }
            }
        }

        return plexServerMap;
    }
}
