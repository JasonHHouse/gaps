package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.PlexServer;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GapsServiceTest {

    @InjectMocks
    GapsServiceImpl gapsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setPlexPropertiesMovieDbApiKey() {
        PlexProperties plexProperties = new PlexProperties();
        plexProperties.setMovieDbApiKey("123qwe");
        gapsService.updatePlexProperties(plexProperties);
        assertEquals("GapsSearch not updating movie db api key", gapsService.getPlexProperties().getMovieDbApiKey(), plexProperties.getMovieDbApiKey());
    }

    @Test
    public void nukePlexProperties() {
        PlexServer plexServer = new PlexServer();
        plexServer.setAddress("123");
        plexServer.setPort(123);

        PlexProperties plexProperties = new PlexProperties();
        plexProperties.setMovieDbApiKey("123qwe");

        gapsService.updatePlexProperties(plexProperties);
        gapsService.getPlexProperties().addPlexServer(plexServer);

        assertEquals("GapsSearch not updating plex servers", 1, gapsService.getPlexProperties().getPlexServers().size());

        gapsService.nukePlexProperties();
        assertEquals("GapsSearch not nuking movie db api key", "", gapsService.getPlexProperties().getMovieDbApiKey());
        assertEquals("GapsSearch not nuking plex servers", 0, gapsService.getPlexProperties().getPlexServers().size());
    }

    @Test
    public void setSelectedLibraries() {
        PlexServer plexServer = new PlexServer();
        plexServer.setMachineIdentifier("abcqwe");

        PlexLibrary plexLibrary1 = new PlexLibrary();
        plexLibrary1.setKey(1);

        PlexLibrary plexLibrary2 = new PlexLibrary();
        plexLibrary2.setKey(2);

        plexServer.getPlexLibraries().add(plexLibrary1);
        plexServer.getPlexLibraries().add(plexLibrary2);

        PlexProperties plexProperties = new PlexProperties();
        plexProperties.setMovieDbApiKey("123qwe");

        gapsService.updatePlexProperties(plexProperties);
        gapsService.getPlexProperties().addPlexServer(plexServer);

        int count = 0;
        for(PlexServer plexServer1 : gapsService.getPlexProperties().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer1.getPlexLibraries()) {
                if(plexLibrary.getSelected()) {
                    count++;
                }
            }
        }

        assertEquals("Default selected libraries is wrong", 0, count);

        gapsService.updateLibrarySelections(new ArrayList<String>(){{
            add("abcqwe1");
        }});

        count = 0;
        for(PlexServer plexServer1 : gapsService.getPlexProperties().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer1.getPlexLibraries()) {
                if(plexLibrary.getSelected()) {
                    count++;
                }
            }
        }

        assertEquals("Updated selected libraries is wrong", 1, count);
    }
}
