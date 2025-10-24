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

public class SlackPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void emptyJson() {
        Assertions.assertThrows(MismatchedInputException.class, () -> objectMapper.readValue("{}", SlackProperties.class));
    }

    @Test
    void addingEnabledAndNotificationTypes() throws JsonProcessingException {
        SlackProperties slackProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", SlackProperties.class);
        assertTrue(CollectionUtils.isEmpty(slackProperties.getNotificationTypes()));
        assertTrue(slackProperties.getEnabled());
        assertTrue(StringUtils.isEmpty(slackProperties.getWebHookUrl()));
    }

    @Test
    void allValues() throws JsonProcessingException {
        SlackProperties slackProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"webHookUrl\":\"http://\"}", SlackProperties.class);
        assertTrue(CollectionUtils.isEmpty(slackProperties.getNotificationTypes()));
        assertTrue(slackProperties.getEnabled());
        assertEquals("http://", slackProperties.getWebHookUrl());
    }
}
