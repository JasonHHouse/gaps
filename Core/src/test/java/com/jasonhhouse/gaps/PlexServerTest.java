/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps;

import com.jasonhhouse.plex.libs.PlexLibrary;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlexServerTest {

    @Test
    public void testDefaultConstructor() {
        PlexServer server = new PlexServer();

        assertNotNull(server.getPlexLibraries());
        assertTrue(server.getPlexLibraries().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        PlexServer server = new PlexServer("My Plex", "machine123", "token123", "192.168.1.1", 32400);

        assertEquals("My Plex", server.getFriendlyName());
        assertEquals("machine123", server.getMachineIdentifier());
        assertEquals("token123", server.getPlexToken());
        assertEquals("192.168.1.1", server.getAddress());
        assertEquals(32400, server.getPort());
        assertNotNull(server.getPlexLibraries());
        assertTrue(server.getPlexLibraries().isEmpty());
    }

    @Test
    public void testSetFriendlyName() {
        PlexServer server = new PlexServer();
        server.setFriendlyName("Test Server");

        assertEquals("Test Server", server.getFriendlyName());
    }

    @Test
    public void testSetMachineIdentifier() {
        PlexServer server = new PlexServer();
        server.setMachineIdentifier("machine456");

        assertEquals("machine456", server.getMachineIdentifier());
    }

    @Test
    public void testSetPlexToken() {
        PlexServer server = new PlexServer();
        server.setPlexToken("new-token");

        assertEquals("new-token", server.getPlexToken());
    }

    @Test
    public void testSetAddress() {
        PlexServer server = new PlexServer();
        server.setAddress("localhost");

        assertEquals("localhost", server.getAddress());
    }

    @Test
    public void testSetPort() {
        PlexServer server = new PlexServer();
        server.setPort(8080);

        assertEquals(8080, server.getPort());
    }

    @Test
    public void testSetPlexLibraries() {
        PlexServer server = new PlexServer();
        List<PlexLibrary> libraries = new ArrayList<>();

        PlexLibrary library1 = new PlexLibrary();
        library1.setKey(1);
        library1.setTitle("Movies");
        library1.setType("movie");

        PlexLibrary library2 = new PlexLibrary();
        library2.setKey(2);
        library2.setTitle("TV Shows");
        library2.setType("show");

        libraries.add(library1);
        libraries.add(library2);

        server.setPlexLibraries(libraries);

        assertEquals(2, server.getPlexLibraries().size());
        assertTrue(server.getPlexLibraries().contains(library1));
        assertTrue(server.getPlexLibraries().contains(library2));
    }

    @Test
    public void testSetPlexLibraries_ClearsExistingList() {
        PlexServer server = new PlexServer();
        List<PlexLibrary> libraries1 = new ArrayList<>();

        PlexLibrary library1 = new PlexLibrary();
        library1.setKey(1);
        library1.setTitle("Movies");
        library1.setType("movie");
        libraries1.add(library1);
        server.setPlexLibraries(libraries1);

        List<PlexLibrary> libraries2 = new ArrayList<>();
        PlexLibrary library2 = new PlexLibrary();
        library2.setKey(2);
        library2.setTitle("TV Shows");
        library2.setType("show");
        libraries2.add(library2);
        server.setPlexLibraries(libraries2);

        assertEquals(1, server.getPlexLibraries().size());
        assertEquals(2, server.getPlexLibraries().get(0).getKey());
    }

    @Test
    public void testEquals_SameObject() {
        PlexServer server = new PlexServer("Test", "machine1", "token", "localhost", 32400);
        assertEquals(server, server);
    }

    @Test
    public void testEquals_SameMachineIdentifier() {
        PlexServer server1 = new PlexServer("Test1", "machine1", "token1", "localhost", 32400);
        PlexServer server2 = new PlexServer("Test2", "machine1", "token2", "192.168.1.1", 8080);

        assertEquals(server1, server2);
    }

    @Test
    public void testEquals_DifferentMachineIdentifier() {
        PlexServer server1 = new PlexServer("Test", "machine1", "token", "localhost", 32400);
        PlexServer server2 = new PlexServer("Test", "machine2", "token", "localhost", 32400);

        assertNotEquals(server1, server2);
    }

    @Test
    public void testEquals_Null() {
        PlexServer server = new PlexServer("Test", "machine1", "token", "localhost", 32400);
        assertNotEquals(server, null);
    }

    @Test
    public void testEquals_DifferentClass() {
        PlexServer server = new PlexServer("Test", "machine1", "token", "localhost", 32400);
        assertNotEquals(server, "Not a PlexServer");
    }

    @Test
    public void testHashCode_SameMachineIdentifier() {
        PlexServer server1 = new PlexServer("Test1", "machine1", "token1", "localhost", 32400);
        PlexServer server2 = new PlexServer("Test2", "machine1", "token2", "192.168.1.1", 8080);

        assertEquals(server1.hashCode(), server2.hashCode());
    }

    @Test
    public void testHashCode_DifferentMachineIdentifier() {
        PlexServer server1 = new PlexServer("Test", "machine1", "token", "localhost", 32400);
        PlexServer server2 = new PlexServer("Test", "machine2", "token", "localhost", 32400);

        assertNotEquals(server1.hashCode(), server2.hashCode());
    }

    @Test
    public void testToString() {
        PlexServer server = new PlexServer("My Server", "machine123", "token123", "192.168.1.1", 32400);
        String result = server.toString();

        assertNotNull(result);
        assertTrue(result.contains("PlexServer"));
        assertTrue(result.contains("friendlyName='My Server'"));
        assertTrue(result.contains("machineIdentifier='machine123'"));
        assertTrue(result.contains("plexToken='token123'"));
        assertTrue(result.contains("address='192.168.1.1'"));
        assertTrue(result.contains("port=32400"));
        assertTrue(result.contains("plexLibraries="));
    }
}
