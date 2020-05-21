package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
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
    public void setPlexSearchMovieDbApiKey() {
        PlexSearch plexSearch = new PlexSearch();
        plexSearch.setMovieDbApiKey("123qwe");
        gapsService.updatePlexSearch(plexSearch);
        assertEquals("GapsSearch not updating movie db api key", gapsService.getPlexSearch().getMovieDbApiKey(), plexSearch.getMovieDbApiKey());
    }

    @Test
    public void nukePlexSearch() {
        PlexServer plexServer = new PlexServer();
        plexServer.setAddress("123");
        plexServer.setPort(123);

        PlexSearch plexSearch = new PlexSearch();
        plexSearch.setMovieDbApiKey("123qwe");

        gapsService.updatePlexSearch(plexSearch);
        gapsService.getPlexSearch().addPlexServer(plexServer);

        assertEquals("GapsSearch not updating plex servers", gapsService.getPlexSearch().getPlexServers().size(), 1);

        gapsService.nukePlexSearch();
        assertEquals("GapsSearch not nuking movie db api key", gapsService.getPlexSearch().getMovieDbApiKey(), "");
        assertEquals("GapsSearch not nuking plex servers", gapsService.getPlexSearch().getPlexServers().size(), 0);
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

        PlexSearch plexSearch = new PlexSearch();
        plexSearch.setMovieDbApiKey("123qwe");

        gapsService.updatePlexSearch(plexSearch);
        gapsService.getPlexSearch().addPlexServer(plexServer);

        int count = 0;
        for(PlexServer plexServer1 : gapsService.getPlexSearch().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer1.getPlexLibraries()) {
                if(plexLibrary.getSelected()) {
                    count++;
                }
            }
        }

        assertEquals("Default selected libraries is wrong", count, 0);

        gapsService.updateLibrarySelections(new ArrayList<String>(){{
            add("abcqwe1");
        }});

        count = 0;
        for(PlexServer plexServer1 : gapsService.getPlexSearch().getPlexServers()) {
            for(PlexLibrary plexLibrary : plexServer1.getPlexLibraries()) {
                if(plexLibrary.getSelected()) {
                    count++;
                }
            }
        }

        assertEquals("Updated selected libraries is wrong", count, 1);
    }
}
