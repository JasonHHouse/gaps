/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.Gaps;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GapsSearchBeanTest {

    private final Gaps gaps = new Gaps();
    private GapsUrlGeneratorTest gapsUrlGeneratorTest;
    private GapsSearchBean gapsSearch;

    @BeforeEach
    void setup() throws Exception {
        gapsUrlGeneratorTest = new GapsUrlGeneratorTest();
        SimpMessagingTemplate template = new SimpMessagingTemplate((message, l) -> true);
        gapsSearch = new GapsSearchBean(gapsUrlGeneratorTest, template);

        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();
        gapsUrlGeneratorTest.setMockWebServer(server);
        gapsUrlGeneratorTest.setupServer();
        server.start();
    }

    @Test
    void defaultTotalMovieCount() {
        assertEquals(0, gapsSearch.getTotalMovieCount(), "Should be zero by default");
    }

    @Test
    void defaultSearchedMovieCount() {
        assertEquals(0, gapsSearch.getSearchedMovieCount(), "Should be zero by default");
    }

    @Test
    void defaultRecommendedMovieCount() {
        assertEquals(0, gapsSearch.getRecommendedMovies().size(), "Should be zero by default");
    }

    @Test
    void emptyGapsProperty() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.run(gaps);
        }, "Should throw exception when not searching from folder and Plex");
    }

    @Test
    void searchPlexGapsEmptyOtherwise() throws Exception {
        gaps.setSearchFromPlex(true);
        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        ResponseEntity response = completedFuture.get();
        if (response.getBody() instanceof List) {
            List recommended = (List) response.getBody();
            Assertions.assertEquals(recommended.size(), 0, "Shouldn't have found any movies");
        }
    }

    @Test
    void emptyLibraryXmlFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_EMPTY_URL);

        Assertions.assertThrows(IllegalStateException.class, () -> gapsSearch.getPlexLibraries(baseUrl), "Should throw exception with for an empty body");
    }

    @Test
    void validLibraryXmlFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.FULL_PLEX_XML_URL);

        Set<PlexLibrary> plexLibraries = gapsSearch.getPlexLibraries(baseUrl);
        Assertions.assertEquals(plexLibraries.size(), 2, "Should have found exactly two libraries");
    }

    @Test
    void badLibraryXmlFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.MISSING_TYPE_PLEX_URL);

        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.getPlexLibraries(baseUrl), "Should throw exception for missing 'type' node inside /MediaContainer/Directory element");
    }

    @Test
    void missingTitleFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.MISSING_TITLE_PLEX_URL);

        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.getPlexLibraries(baseUrl), "Should throw exception for missing 'title' node inside /MediaContainer/Directory element");
    }

    @Test
    void missingKeyFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.MISSING_KEY_PLEX_URL);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception for missing 'key' node inside /MediaContainer/Directory element");
    }

    @Test
    void nonNumberKeyFromPlex() {
        HttpUrl baseUrl = gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.NON_NUMBER_KEY_FROM_PLEX_URL);

        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.getPlexLibraries(baseUrl), "Should throw exception for 'key' node not being a number inside /MediaContainer/Directory element");
    }

    @Test
    void noBodyMovieXmlFromPlex() {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_EMPTY_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.PLEX_EMPTY_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.run(gaps), "Should throw exception that the body was empty");
    }

    @Test
    void emptyBodyMovieXmlFromPlex() throws Exception {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.NO_MOVIE_PLEX_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.NO_MOVIE_PLEX_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        ResponseEntity ResponseEntity = completedFuture.get();
        if (ResponseEntity.getBody() instanceof List) {
            List recommended = (List) ResponseEntity.getBody();
            Assertions.assertEquals(recommended.size(), 0, "Shouldn't have found any movies");
        }
    }

    @Test
    void emptyMovieXmlFromPlex() {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.EMPTY_MOVIE_PLEX_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.EMPTY_MOVIE_PLEX_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.run(gaps), "Should throw exception that the title was missing from the video element");
    }


    @Test
    void validGuidFromPlex() throws Exception {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_WITH_GUID_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.PLEX_WITH_GUID_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        completedFuture.get();
        assertEquals(gapsSearch.getTotalMovieCount(), 1, "Should have found exactly one movie");
    }

    @Test
    void validYearXmlFromPlex() throws Exception {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_MOVIE_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.PLEX_MOVIE_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        completedFuture.get();
        assertEquals(gapsSearch.getTotalMovieCount(), 1, "Should have found exactly one movie");
    }

    @Test
    void invalidMovieDbFromPlex() throws Exception {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_MOVIE_URL);

        List<String> movieUrls = new ArrayList<>();
        movieUrls.add(GapsUrlGeneratorTest.PLEX_MOVIE_URL);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);
        gaps.setMovieDbApiKey("fake_id");

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        completedFuture.get();
        assertEquals(gapsSearch.getRecommendedMovies().size(), 3, "Should have found exactly three movies recommended");
    }

}