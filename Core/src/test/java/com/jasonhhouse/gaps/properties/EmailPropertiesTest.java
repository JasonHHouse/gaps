/*
 * Copyright 2025 Jason H House
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void emptyJson() {
        Assertions.assertThrows(MismatchedInputException.class, () -> objectMapper.readValue("{}", EmailProperties.class));
    }

    @Test
    void addingEnabledAndNotificationTypes() throws JsonProcessingException {
        EmailProperties emailProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[]}", EmailProperties.class);
        assertTrue(CollectionUtils.isEmpty(emailProperties.getNotificationTypes()));
        assertTrue(emailProperties.getEnabled());
        assertTrue(StringUtils.isEmpty(emailProperties.getUsername()));
        assertTrue(StringUtils.isEmpty(emailProperties.getPassword()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailTo()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailFrom()));
        assertEquals(0, emailProperties.getMailPort());
        assertTrue(StringUtils.isEmpty(emailProperties.getMailTransportProtocol()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailSmtpAuth()));
        assertFalse(emailProperties.getMailSmtpTlsEnabled());
    }

    @Test
    void missingOneValue() throws JsonProcessingException {
        EmailProperties emailProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"username\":\"123\"}", EmailProperties.class);
        assertTrue(CollectionUtils.isEmpty(emailProperties.getNotificationTypes()));
        assertTrue(emailProperties.getEnabled());
        assertEquals("123", emailProperties.getUsername());
        assertTrue(StringUtils.isEmpty(emailProperties.getPassword()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailTo()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailFrom()));
        assertEquals(0, emailProperties.getMailPort());
        assertTrue(StringUtils.isEmpty(emailProperties.getMailTransportProtocol()));
        assertTrue(StringUtils.isEmpty(emailProperties.getMailSmtpAuth()));
        assertFalse(emailProperties.getMailSmtpTlsEnabled());
    }

    @Test
    void allValues() throws JsonProcessingException {
        EmailProperties emailProperties = objectMapper.readValue("{\"enabled\":true,\"notificationTypes\":[],\"username\":\"jason\",\"password\":\"password\",\"mailTo\":\"mail2\",\"mailFrom\":\"mailFrom\",\"mailPort\":4444,\"mailTransportProtocol\":\"TLS\",\"mailSmtpAuth\":\"AUTH\",\"mailSmtpTlsEnabled\":true}", EmailProperties.class);
        assertTrue(CollectionUtils.isEmpty(emailProperties.getNotificationTypes()));
        assertTrue(emailProperties.getEnabled());
        assertEquals("jason", emailProperties.getUsername());
        assertEquals("password",emailProperties.getPassword());
        assertEquals("mail2", emailProperties.getMailTo());
        assertEquals("mailFrom", emailProperties.getMailFrom());
        assertEquals(4444, emailProperties.getMailPort());
        assertEquals("TLS", emailProperties.getMailTransportProtocol());
        assertEquals("AUTH", emailProperties.getMailSmtpAuth());
        assertTrue(emailProperties.getMailSmtpTlsEnabled());
    }
}
