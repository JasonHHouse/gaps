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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GotifyPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void emptyJson() {
        Assertions.assertThrows(MismatchedInputException.class, () -> objectMapper.readValue("{}", GotifyProperties.class));
    }

    @Test
    void addingEnabledAndNotificationTypes() throws JsonProcessingException {
        GotifyProperties gotifyProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", GotifyProperties.class);
        assertTrue(CollectionUtils.isEmpty(gotifyProperties.getNotificationTypes()));
        assertTrue(gotifyProperties.getEnabled());
        assertTrue(StringUtils.isEmpty(gotifyProperties.getAddress()));
        assertTrue(StringUtils.isEmpty(gotifyProperties.getToken()));
    }

    @Test
    void missingOneValue() throws JsonProcessingException {
        GotifyProperties gotifyProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"address\":\"address\"}", GotifyProperties.class);
        assertTrue(CollectionUtils.isEmpty(gotifyProperties.getNotificationTypes()));
        assertTrue(gotifyProperties.getEnabled());
        assertEquals("address", gotifyProperties.getAddress());
        assertTrue(StringUtils.isEmpty(gotifyProperties.getToken()));
    }

    @Test
    void allValues() throws JsonProcessingException {
        GotifyProperties gotifyProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"TEST\"],\"address\":\"address\",\"token\":\"123qwe\"}", GotifyProperties.class);
        assertTrue(CollectionUtils.isNotEmpty(gotifyProperties.getNotificationTypes()));
        assertTrue(gotifyProperties.getEnabled());
        assertEquals("address", gotifyProperties.getAddress());
        assertEquals("123qwe", gotifyProperties.getToken());
    }
}
