/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscordPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void emptyJson() {
        Assertions.assertThrows(MismatchedInputException.class, () -> objectMapper.readValue("{}", DiscordProperties.class));
    }

    @Test
    void addingEnabledAndNotificationTypes() throws JsonProcessingException {
        DiscordProperties discordProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertTrue(CollectionUtils.isEmpty(discordProperties.getNotificationTypes()));
        assertTrue(discordProperties.getEnabled());
        assertTrue(StringUtils.isEmpty(discordProperties.getWebHookUrl()));
    }

    @Test
    void allValues() throws JsonProcessingException {
        DiscordProperties discordProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"webHookUrl\":\"http://\"}", DiscordProperties.class);
        assertTrue(CollectionUtils.isEmpty(discordProperties.getNotificationTypes()));
        assertTrue(discordProperties.getEnabled());
        assertEquals("http://", discordProperties.getWebHookUrl());
    }

    @Test
    void testHasTmdbConnectionApi() throws JsonProcessingException {
        DiscordProperties withTmdb = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"TMDB_API_CONNECTION\"]}", DiscordProperties.class);
        assertTrue(withTmdb.hasTmdbConnectionApi());

        DiscordProperties withoutTmdb = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertFalse(withoutTmdb.hasTmdbConnectionApi());
    }

    @Test
    void testHasPlexServerConnection() throws JsonProcessingException {
        DiscordProperties withPlex = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"PLEX_SERVER_CONNECTION\"]}", DiscordProperties.class);
        assertTrue(withPlex.hasPlexServerConnection());

        DiscordProperties withoutPlex = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertFalse(withoutPlex.hasPlexServerConnection());
    }

    @Test
    void testHasPlexMetadataUpdate() throws JsonProcessingException {
        DiscordProperties withMetadata = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"PLEX_METADATA_UPDATE\"]}", DiscordProperties.class);
        assertTrue(withMetadata.hasPlexMetadataUpdate());

        DiscordProperties withoutMetadata = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertFalse(withoutMetadata.hasPlexMetadataUpdate());
    }

    @Test
    void testHasPlexLibraryUpdate() throws JsonProcessingException {
        DiscordProperties withLibrary = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"PLEX_LIBRARY_UPDATE\"]}", DiscordProperties.class);
        assertTrue(withLibrary.hasPlexLibraryUpdate());

        DiscordProperties withoutLibrary = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertFalse(withoutLibrary.hasPlexLibraryUpdate());
    }

    @Test
    void testHasGapsMissingCollections() throws JsonProcessingException {
        DiscordProperties withGaps = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"GAPS_MISSING_COLLECTIONS\"]}", DiscordProperties.class);
        assertTrue(withGaps.hasGapsMissingCollections());

        DiscordProperties withoutGaps = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", DiscordProperties.class);
        assertFalse(withoutGaps.hasGapsMissingCollections());
    }

    @Test
    void testGetDefault() {
        DiscordProperties defaultProps = DiscordProperties.getDefault();

        assertNotNull(defaultProps);
        assertFalse(defaultProps.getEnabled());
        assertTrue(defaultProps.getNotificationTypes().isEmpty());
        assertEquals("", defaultProps.getWebHookUrl());
    }

    @Test
    void testToString() throws JsonProcessingException {
        DiscordProperties discordProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"webHookUrl\":\"http://test\"}", DiscordProperties.class);
        String result = discordProperties.toString();

        assertNotNull(result);
        assertTrue(result.contains("DiscordProperties"));
        assertTrue(result.contains("webHookUrl"));
    }
}
