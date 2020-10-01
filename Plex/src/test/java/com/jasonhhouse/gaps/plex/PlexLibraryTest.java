/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.plex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PlexLibraryTest {

    @NotNull
    private Integer key;

    @NotNull
    private String scanner;

    @NotNull
    private String title;

    @NotNull
    private String type;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean defaultLibrary;

    @NotNull
    private PlexLibrary plexLibrary;

    @BeforeEach
    void setUp() {
        Faker faker = new Faker();

        key = faker.number().randomDigit();
        scanner = faker.lorem().word();
        title = faker.pokemon().name();
        type = faker.lorem().word();
        enabled = faker.bool().bool();
        defaultLibrary = faker.bool().bool();

        plexLibrary = new PlexLibrary(key, scanner, title, type, enabled, defaultLibrary);
    }

    @Test
    void getKey() {
        assertEquals(key, plexLibrary.getKey());
    }

    @Test
    void getScanner() {
        assertEquals(scanner, plexLibrary.getScanner());
    }

    @Test
    void getTitle() {
        assertEquals(title, plexLibrary.getTitle());
    }

    @Test
    void getType() {
        assertEquals(type, plexLibrary.getType());
    }

    @Test
    void getEnabled() {
        assertEquals(enabled, plexLibrary.getEnabled());
    }

    @Test
    void getDefaultLibrary() {
        assertEquals(defaultLibrary, plexLibrary.getDefaultLibrary());
    }

    @Test
    void testEquals() {
        PlexLibrary plexLibrary2 = new PlexLibrary(key, scanner, title, type, enabled, defaultLibrary);
        assertEquals(plexLibrary, plexLibrary2);
    }

    @Test
    void PlexObjectsTest() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String xml = IOUtils.toString(this.getClass().getResourceAsStream("plexLibrary.json"), StandardCharsets.UTF_8);
            PlexLibrary plexLibrary = objectMapper.readValue(xml, PlexLibrary.class);
            PlexLibrary plexLibrary2 = new PlexLibrary(123412431, "SUPER SCANNER", "MY AWESOME title", "movie", false, true);

            assertEquals(plexLibrary, plexLibrary2);

        } catch (IOException e) {
            fail("Failed to read plex library json file");
        }
    }

}