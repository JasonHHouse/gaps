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
import java.util.Collections;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PlexServerTest {

    @NotNull
    private String friendlyName;

    @NotNull
    private String machineIdentifier;

    @NotNull
    private String plexToken;

    @NotNull
    private String address;

    @NotNull
    private Integer port;

    @NotNull
    private PlexServer plexServer;

    @BeforeEach
    void setUp() {
        Faker faker = new Faker();

        friendlyName = faker.lorem().word();
        machineIdentifier = faker.dragonBall().character();
        plexToken = faker.dragonBall().character();
        address = faker.dragonBall().character();
        port = faker.number().randomDigit();

        plexServer = new PlexServer(friendlyName, machineIdentifier, plexToken, address, port, Collections.emptySet());
    }

    @Test
    void getFriendlyName() {
        assertEquals(friendlyName, plexServer.getFriendlyName());
    }

    @Test
    void getMachineIdentifier() {
        assertEquals(machineIdentifier, plexServer.getMachineIdentifier());
    }

    @Test
    void getPlexLibraries() {
        assertEquals(0, plexServer.getPlexLibraries().size());
    }

    @Test
    void getPlexToken() {
        assertEquals(plexToken, plexServer.getPlexToken());
    }

    @Test
    void getAddress() {
        assertEquals(address, plexServer.getAddress());
    }

    @Test
    void getPort() {
        assertEquals(port, plexServer.getPort());
    }

    @Test
    void testEquals() {
        PlexServer plexServer2 = new PlexServer(null, machineIdentifier, null, null, null, null);
        assertEquals(plexServer, plexServer2);
    }

    @Test
    void PlexObjectsTest() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String xml = IOUtils.toString(this.getClass().getResourceAsStream("plexServer.json"), StandardCharsets.UTF_8);
            PlexServer plexServer = objectMapper.readValue(xml, PlexServer.class);
            PlexServer plexServer2 = new PlexServer("RED", "5b4e72f3-e5b4-4e04-9832-43a2412f8058", null, null, null, null);

            assertEquals(plexServer, plexServer2);

        } catch (IOException e) {
            fail("Failed to read plex server json file");
        }
    }
}