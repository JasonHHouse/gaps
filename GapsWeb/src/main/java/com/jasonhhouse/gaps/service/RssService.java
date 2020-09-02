/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.plex.libs.PlexLibrary;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RssService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RssService.class);

    private final FileIoService fileIoService;

    @Autowired
    public RssService(FileIoService fileIoService) {
        this.fileIoService = fileIoService;
    }

    @NotNull
    public Map<PlexLibrary, PlexServer> foundAnyRssFeeds() {
        Map<PlexLibrary, PlexServer> plexServerMap = new HashMap<>();

        PlexProperties plexProperties = fileIoService.readProperties();
        Set<PlexServer> plexServers = plexProperties.getPlexServers();
        if (CollectionUtils.isEmpty(plexServers)) {
            return Collections.emptyMap();
        }

        for (PlexServer plexServer : plexServers) {
            if (CollectionUtils.isEmpty(plexServer.getPlexLibraries())) {
                continue;
            }

            for (PlexLibrary plexLibrary : plexServer.getPlexLibraries()) {
                if (fileIoService.doesRssFileExist(plexServer.getMachineIdentifier(), plexLibrary.getKey())) {
                    plexServerMap.put(plexLibrary, plexServer);
                }
            }
        }

        return plexServerMap;
    }
}
