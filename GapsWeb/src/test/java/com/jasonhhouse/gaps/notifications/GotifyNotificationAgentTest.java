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

package com.jasonhhouse.gaps.notifications;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.service.FakeIoService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GotifyNotificationAgentTest {

    @NotNull
    private GotifyNotificationAgent gotifyNotificationAgent;

    @BeforeEach
    void setUp() {
        gotifyNotificationAgent = new GotifyNotificationAgent(new FakeIoService());
    }

    @Test
    void sendMessage() {
        Boolean sentSuccessfully = gotifyNotificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
        assertTrue(sentSuccessfully, "Should have sent test gotify message");
    }

    @Test
    void failToMessage() {
        Boolean sentUnsuccessfully = gotifyNotificationAgent.sendMessage(NotificationType.GAPS_MISSING_COLLECTIONS, "DEBUG", "Gaps Test", "Test Successful");
        assertFalse(sentUnsuccessfully, "Should have not sent test gotify message");
    }
}