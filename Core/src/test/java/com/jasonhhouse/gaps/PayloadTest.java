/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PayloadTest {

    @Test
    public void testPayloadSearchSuccessful() {
        Payload payload = Payload.SEARCH_SUCCESSFUL;
        assertEquals(0, payload.getCode());
        assertEquals("Search successful.", payload.getReason());
    }

    @Test
    public void testPayloadTmdbKeyValid() {
        Payload payload = Payload.TMDB_KEY_VALID;
        assertEquals(20, payload.getCode());
        assertEquals("TMDB Key valid.", payload.getReason());
    }

    @Test
    public void testPayloadTmdbKeyInvalid() {
        Payload payload = Payload.TMDB_KEY_INVALID;
        assertEquals(21, payload.getCode());
        assertEquals("TMDB Key invalid", payload.getReason());
    }

    @Test
    public void testPayloadPlexConnectionSucceeded() {
        Payload payload = Payload.PLEX_CONNECTION_SUCCEEDED;
        assertEquals(10, payload.getCode());
        assertEquals("Connection to Plex succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadPlexConnectionFailed() {
        Payload payload = Payload.PLEX_CONNECTION_FAILED;
        assertEquals(11, payload.getCode());
        assertEquals("Connection to Plex failed.", payload.getReason());
    }

    @Test
    public void testPayloadWithExtras() {
        Payload payload = Payload.SEARCH_SUCCESSFUL;

        String extraData = "Additional information";
        Payload result = payload.setExtras(extraData);

        assertEquals(extraData, payload.getExtras());
        assertSame(payload, result, "setExtras should return the same instance");

        // Clean up after test to avoid affecting other tests
        payload.setExtras(null);
    }

    @Test
    public void testPayloadExtrasWithObject() {
        Payload payload = Payload.PLEX_LIBRARIES_FOUND;
        Object complexObject = new Object() {
            @SuppressWarnings("unused")
            public String data = "test";
        };

        payload.setExtras(complexObject);
        assertSame(complexObject, payload.getExtras());

        // Clean up after test to avoid affecting other tests
        payload.setExtras(null);
    }

    @Test
    public void testPayloadSearchCancelled() {
        Payload payload = Payload.SEARCH_CANCELLED;
        assertEquals(1, payload.getCode());
        assertEquals("Search cancelled.", payload.getReason());
    }

    @Test
    public void testPayloadSearchFailed() {
        Payload payload = Payload.SEARCH_FAILED;
        assertEquals(2, payload.getCode());
        assertEquals("Search failed. Check docker Gaps log file.", payload.getReason());
    }

    @Test
    public void testPayloadUnknownError() {
        Payload payload = Payload.UNKNOWN_ERROR;
        assertEquals(-1, payload.getCode());
        assertEquals("Unknown error.", payload.getReason());
    }

    @Test
    public void testPayloadNukeSuccessful() {
        Payload payload = Payload.NUKE_SUCCESSFUL;
        assertEquals(30, payload.getCode());
        assertEquals("Nuke successful. All files deleted.", payload.getReason());
    }

    @Test
    public void testPayloadNotificationTestSucceeded() {
        Payload payload = Payload.NOTIFICATION_TEST_SUCCEEDED;
        assertEquals(70, payload.getCode());
        assertEquals("Notification test succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadNotificationTestFailed() {
        Payload payload = Payload.NOTIFICATION_TEST_FAILED;
        assertEquals(71, payload.getCode());
        assertEquals("Notification test failed.", payload.getReason());
    }

    @Test
    public void testPayloadTelegramNotificationUpdateSucceeded() {
        Payload payload = Payload.TELEGRAM_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(80, payload.getCode());
        assertEquals("Telegram Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadSlackNotificationUpdateSucceeded() {
        Payload payload = Payload.SLACK_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(90, payload.getCode());
        assertEquals("Slack Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadPushBulletNotificationUpdateSucceeded() {
        Payload payload = Payload.PUSH_BULLET_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(100, payload.getCode());
        assertEquals("PushBullet Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadGotifyNotificationUpdateSucceeded() {
        Payload payload = Payload.GOTIFY_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(110, payload.getCode());
        assertEquals("Gotify Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadEmailNotificationUpdateSucceeded() {
        Payload payload = Payload.EMAIL_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(120, payload.getCode());
        assertEquals("Email Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadPushOverNotificationUpdateSucceeded() {
        Payload payload = Payload.PUSH_OVER_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(130, payload.getCode());
        assertEquals("PushOver Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadDiscordNotificationUpdateSucceeded() {
        Payload payload = Payload.DISCORD_NOTIFICATION_UPDATE_SUCCEEDED;
        assertEquals(140, payload.getCode());
        assertEquals("Discord Notification Update Succeeded.", payload.getReason());
    }

    @Test
    public void testPayloadMovieStatusFound() {
        Payload payload = Payload.MOVIE_STATUS_FOUND;
        assertEquals(150, payload.getCode());
        assertEquals("Movie status found.", payload.getReason());
    }

    @Test
    public void testPayloadMovieStatusUpdated() {
        Payload payload = Payload.MOVIE_STATUS_UPDATED;
        assertEquals(152, payload.getCode());
        assertEquals("Movie status updated successfully.", payload.getReason());
    }

    @Test
    public void testAllPayloadEnumValues() {
        // Verify all enum values can be accessed
        Payload[] payloads = Payload.values();
        assertTrue(payloads.length > 0, "Should have payload values");

        // Verify each payload has valid code and reason
        for (Payload payload : payloads) {
            assertNotNull(payload.getReason(), "Reason should not be null for " + payload.name());
            assertFalse(payload.getReason().isEmpty(), "Reason should not be empty for " + payload.name());
        }
    }

    @Test
    public void testPayloadEnumValueOf() {
        Payload payload = Payload.valueOf("SEARCH_SUCCESSFUL");
        assertEquals(Payload.SEARCH_SUCCESSFUL, payload);
        assertEquals(0, payload.getCode());
    }

    @Test
    public void testPayloadExtrasChaining() {
        Payload payload = Payload.SEARCH_SUCCESSFUL
                .setExtras("First")
                .setExtras("Second")
                .setExtras("Third");

        assertEquals("Third", payload.getExtras(), "Should have the last set value");
    }
}
