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

public class PushBulletPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void emptyJson() {
        Assertions.assertThrows(MismatchedInputException.class, () -> objectMapper.readValue("{}", PushBulletProperties.class));
    }

    @Test
    void addingEnabledAndNotificationTypes() throws JsonProcessingException {
        PushBulletProperties telegramProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", PushBulletProperties.class);
        assertTrue(CollectionUtils.isEmpty(telegramProperties.getNotificationTypes()));
        assertTrue(telegramProperties.getEnabled());
        assertTrue(StringUtils.isEmpty(telegramProperties.getChannel_tag()));
        assertTrue(StringUtils.isEmpty(telegramProperties.getAccessToken()));
    }

    @Test
    void missingOneValue() throws JsonProcessingException {
        PushBulletProperties telegramProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"channel_tag\":\"123\"}", PushBulletProperties.class);
        assertTrue(CollectionUtils.isEmpty(telegramProperties.getNotificationTypes()));
        assertTrue(telegramProperties.getEnabled());
        assertEquals("123", telegramProperties.getChannel_tag());
        assertTrue(StringUtils.isEmpty(telegramProperties.getAccessToken()));
    }

    @Test
    void allValues() throws JsonProcessingException {
        PushBulletProperties telegramProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[\"TEST\"],\"channel_tag\":\"123\",\"accessToken\":\"abc\"}", PushBulletProperties.class);
        assertTrue(CollectionUtils.isNotEmpty(telegramProperties.getNotificationTypes()));
        assertTrue(telegramProperties.getEnabled());
        assertEquals("123", telegramProperties.getChannel_tag());
        assertEquals("abc", telegramProperties.getAccessToken());
    }
}
