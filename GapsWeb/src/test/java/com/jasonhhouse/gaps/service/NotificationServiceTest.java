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

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.notifications.DiscordNotificationAgent;
import com.jasonhhouse.gaps.notifications.EmailNotificationAgent;
import com.jasonhhouse.gaps.notifications.GotifyNotificationAgent;
import com.jasonhhouse.gaps.notifications.NotificationAgent;
import com.jasonhhouse.gaps.notifications.PushBulletNotificationAgent;
import com.jasonhhouse.gaps.notifications.PushOverNotificationAgent;
import com.jasonhhouse.gaps.notifications.SlackNotificationAgent;
import com.jasonhhouse.gaps.notifications.TelegramNotificationAgent;
import com.jasonhhouse.gaps.properties.NotificationProperties;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        List<NotificationAgent<? extends NotificationProperties>> notificationTypeList = new ArrayList<>();

        notificationTypeList.add(new DiscordNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new EmailNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new GotifyNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new PushBulletNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new PushOverNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new SlackNotificationAgent(new FakeIoService()));
        notificationTypeList.add(new TelegramNotificationAgent(new FakeIoService()));

        notificationService = new NotificationService(notificationTypeList);
    }

    @Test
    public void plexServerConnectFailed() {
        PlexServer plexServer = new PlexServer();
        plexServer.setFriendlyName("Friendly Name");
        String error = "";

        Boolean result = notificationService.plexServerConnectFailed(plexServer, error);
        assertTrue(result, "Should have sent plex server connect failed message");
    }

    @Test
    public void tmdbConnectionFailed() {
        Boolean result = notificationService.tmdbConnectionFailed("");
        assertFalse(result, "Should have failed to send plex server connect failed");
    }
}