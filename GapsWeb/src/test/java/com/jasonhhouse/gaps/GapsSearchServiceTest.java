/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.service.GapsSearchService;
import com.jasonhhouse.gaps.service.IoService;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = GapsApplication.class)
class GapsSearchServiceTest {

    private GapsUrlGeneratorTest gapsUrlGeneratorTest;
    private GapsSearchService gapsSearch;

    @Mock
    private IoService ioService;

    @BeforeEach
    void setup() throws Exception {
        gapsUrlGeneratorTest = new GapsUrlGeneratorTest();
        SimpMessagingTemplate template = new SimpMessagingTemplate((message, l) -> true);

        GapsService gapsService = new GapsServiceTest();

        gapsSearch = new GapsSearchService(gapsUrlGeneratorTest, template, ioService, gapsService);

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
    void emptyGapsProperty() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            gapsSearch.run();
        }, "Should throw exception when not searching from folder and Plex");
    }

    @Test
    void noBodyMovieXmlFromPlex() {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.PLEX_EMPTY_URL);
        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.run(), "Should throw exception that the body was empty");
    }

    @Test
    void emptyMovieXmlFromPlex() {
        gapsUrlGeneratorTest.generatePlexUrl(GapsUrlGeneratorTest.EMPTY_MOVIE_PLEX_URL);
        Assertions.assertThrows(ResponseStatusException.class, () -> gapsSearch.run(), "Should throw exception that the title was missing from the video element");
    }

}