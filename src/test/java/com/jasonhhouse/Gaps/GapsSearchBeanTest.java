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
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GapsSearchBeanTest {

    private final static String GOOD_PLEX_XML = "<MediaContainer size=\"3\" allowSync=\"0\" identifier=\"com.plexapp.plugins.library\" mediaTagPrefix=\"/system/bundle/media/flags/\"\n" +
            "mediaTagVersion=\"1390169701\" title1=\"Plex Library\">\n" +
            "   <Directory allowSync=\"0\" art=\"/:/resources/movie-fanart.jpg\" filters=\"1\" refreshing=\"0\" thumb=\"/:/resources/movie.png\"\n" +
            "   key=\"29\" type=\"movie\" title=\"Movies\" agent=\"com.plexapp.agents.imdb\" scanner=\"Plex Movie Scanner\"\n" +
            "   language=\"en\" uuid=\"07a4b132-a67b-477e-a245-585935d08c0b\" updatedAt=\"1394559305\" createdAt=\"1390438950\">\n" +
            "      <Location id=\"4\" path=\"/Users/plexuser/Movies/Media/Movies\"/>\n" +
            "   </Directory>\n" +
            "   <Directory allowSync=\"0\" art=\"/:/resources/artist-fanart.jpg\" filters=\"1\" refreshing=\"0\" thumb=\"/:/resources/artist.png\"\n" +
            "   key=\"31\" type=\"movie\" title=\"Movies HD\" agent=\"com.plexapp.agents.lastfm\" scanner=\"Plex Music Scanner\"\n" +
            "   language=\"en\" uuid=\"10254ef0-a0a4-481b-ad9c-46ab3db39d0b\" updatedAt=\"1394039950\"\n" +
            "   createdAt=\"1390440566\">\n" +
            "      <Location id=\"7\" path=\"/Users/plexuser/Movies/Media/Music\"/>\n" +
            "   </Directory>\n" +
            "   <Directory allowSync=\"0\" art=\"/:/resources/show-fanart.jpg\" filters=\"1\" refreshing=\"0\" thumb=\"/:/resources/show.png\"\n" +
            "   key=\"30\" type=\"show\" title=\"Television\" agent=\"com.plexapp.agents.thetvdb\" scanner=\"Plex Series Scanner\"\n" +
            "   language=\"en\" uuid=\"540e7c98-5a92-4e8f-b255-9cca2870060c\" updatedAt=\"1394482680\"\n" +
            "   createdAt=\"1390438925\">\n" +
            "      <Location id=\"3\" path=\"/Users/plexuser/Movies/Media/TV Shows\"/>\n" +
            "   </Directory>\n" +
            "</MediaContainer>";

    private final static String MISSING_TYPE_PLEX_XML = "<MediaContainer><Directory></Directory></MediaContainer>";
    private final static String MISSING_TITLE_PLEX_XML = "<MediaContainer><Directory type=\"movie\"></Directory></MediaContainer>";
    private final static String MISSING_KEY_PLEX_XML = "<MediaContainer><Directory type=\"movie\" title=\"Movies\"></Directory></MediaContainer>";
    private final static String NON_NUMBER_KEY_PLEX_XML = "<MediaContainer><Directory type=\"movie\" title=\"Movies\" key=\"a\" ></Directory></MediaContainer>";

    private final static String NO_MOVIE_PLEX_XML = "<MediaContainer></MediaContainer>";
    private final static String EMPTY_MOVIE_PLEX_XML = "<MediaContainer><Video></Video></MediaContainer>";
    private final static String GOOD_MOVIE_PLEX_XML = "<MediaContainer><Video title=\"Alien\" year=\"1979\"></Video></MediaContainer>";

    private final GapsUrlGeneratorTest gapsUrlGeneratorTest = new GapsUrlGeneratorTest();
    private final GapsSearchBean gapsSearch = new GapsSearchBean(gapsUrlGeneratorTest);
    private final Gaps gaps = new Gaps();

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
        assertThrows(ResponseStatusException.class, () -> {
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
            assertEquals(recommended.size(), 0, "Shouldn't have found any movies");
        }
    }

    @Test
    void emptyLibraryXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache");

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        assertThrows(IllegalStateException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception with for an empty body");
    }

    @Test
    void validLibraryXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        Set<PlexLibrary> plexLibraries = gapsSearch.getPlexLibraries(baseUrl);
        assertEquals(plexLibraries.size(), 2, "Should have found exactly two libraries");
    }

    @Test
    void badLibraryXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(MISSING_TYPE_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception for missing 'type' node inside /MediaContainer/Directory element");
    }

    @Test
    void missingTitleFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(MISSING_TITLE_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception for missing 'title' node inside /MediaContainer/Directory element");
    }

    @Test
    void missingKeyFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(MISSING_KEY_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception for missing 'key' node inside /MediaContainer/Directory element");
    }

    @Test
    void nonNumberKeyFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(NON_NUMBER_KEY_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("library/sections?X-Plex-Token=fake");

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.getPlexLibraries(baseUrl);
        }, "Should throw exception for 'key' node not being a number inside /MediaContainer/Directory element");
    }

    @Test
    void noBodyMovieXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache");

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        String movieUrl = "movie/url/1";
        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url(movieUrl);

        List<HttpUrl> movieUrls = new ArrayList<>();
        movieUrls.add(baseUrl);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.run(gaps);
        }, "Should throw exception that the body was empty");
    }

    @Test
    void emptyBodyMovieXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(NO_MOVIE_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        String movieUrl = "movie/url/1";
        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url(movieUrl);

        List<HttpUrl> movieUrls = new ArrayList<>();
        movieUrls.add(baseUrl);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        ResponseEntity ResponseEntity = completedFuture.get();
        if (ResponseEntity.getBody() instanceof List) {
            List recommended = (List) ResponseEntity.getBody();
            assertEquals(recommended.size(), 0, "Shouldn't have found any movies");
        }
    }

    @Test
    void emptyMovieXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(EMPTY_MOVIE_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        String movieUrl = "movie/url/1";
        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url(movieUrl);

        List<HttpUrl> movieUrls = new ArrayList<>();
        movieUrls.add(baseUrl);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.run(gaps);
        }, "Should throw exception that the title was missing from the video element");
    }

    @Test
    void validYearXmlFromPlex() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_MOVIE_PLEX_XML);

        // Schedule some responses.
        server.enqueue(response);

        // Start the server.
        server.start();

        String movieUrl = "movie/url/1";
        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url(movieUrl);

        List<HttpUrl> movieUrls = new ArrayList<>();
        movieUrls.add(baseUrl);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        completedFuture.get();
        assertEquals(gapsSearch.getTotalMovieCount(), 1, "Should have found exactly one movie");
    }

    @Test
    void invalidMovieDbFromPlex() throws Exception {
        GapsUrlGeneratorTest gapsUrlGeneratorTest = new GapsUrlGeneratorTest();
        GapsSearchBean gapsSearch = new GapsSearchBean(gapsUrlGeneratorTest);

        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();
        gapsUrlGeneratorTest.setMockWebServer(server);
        gapsUrlGeneratorTest.setupServer();
        server.start();

        HttpUrl plexMovieUrl = gapsUrlGeneratorTest.getPlexUrl();

        List<HttpUrl> movieUrls = new ArrayList<>();
        movieUrls.add(plexMovieUrl);

        gaps.setMovieUrls(movieUrls);
        gaps.setSearchFromPlex(true);
        gaps.setMovieDbApiKey("fake_id");

        Future<ResponseEntity> completedFuture = gapsSearch.run(gaps);

        completedFuture.get();
        assertEquals(gapsSearch.getRecommendedMovies().size(), 3, "Should have found exactly three movies recommended");
    }

}