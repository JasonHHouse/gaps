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

import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.plex.PlexServer;
import com.jasonhhouse.gaps.properties.DiscordProperties;
import com.jasonhhouse.gaps.properties.EmailProperties;
import com.jasonhhouse.gaps.properties.GotifyProperties;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.properties.PushBulletProperties;
import com.jasonhhouse.gaps.properties.PushOverProperties;
import com.jasonhhouse.gaps.properties.SlackProperties;
import com.jasonhhouse.gaps.properties.TelegramProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class FakeIoService implements IO {
    @Override
    public @NotNull List<BasicMovie> readRecommendedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public @NotNull Boolean doesRssFileExist(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public @NotNull String getRssFile(String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public void writeRssFile(@NotNull String machineIdentifier, @NotNull Integer key, @NotNull Set<BasicMovie> recommended) {

    }

    @Override
    public void writeRecommendedToFile(@NotNull Set<BasicMovie> recommended, @NotNull String machineIdentifier, @NotNull Integer key) {

    }

    @Override
    public void writeOwnedMoviesToFile(@NotNull List<BasicMovie> ownedBasicMovies, @NotNull String machineIdentifier, @NotNull Integer key) {

    }

    @Override
    public @NotNull List<BasicMovie> readOwnedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie) {

    }

    @Override
    public void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie, @NotNull File file) {

    }

    @Override
    public @NotNull Set<BasicMovie> readMovieIdsFromFile() {
        return null;
    }

    @Override
    public void writeProperties(@NotNull PlexProperties plexProperties) {

    }

    @Override
    public @NotNull PlexProperties readProperties() {
        List<NotificationType> notificationTypes = new ArrayList<>();
        notificationTypes.add(NotificationType.TEST);
        notificationTypes.add(NotificationType.PLEX_SERVER_CONNECTION);

        String telegramArg1 = new String(Base64.getDecoder().decode("MTMwMjEzOTExOTpBQUV1cGh0RXVvcFhhVHBpdGlHbFdDWS0zbDMzU0JrUGUybw"));
        String telegramArg2 = new String(Base64.getDecoder().decode("MTA0MTA4MTMxNw=="));
        TelegramProperties telegramProperties = new TelegramProperties(true, notificationTypes, telegramArg1, telegramArg2);

        String slackArg1 = new String(Base64.getDecoder().decode("aHR0cHM6Ly9ob29rcy5zbGFjay5jb20vc2VydmljZXMvVDAxN1hSMEJNUlYvQjAxOEs0TkE3NjAvemsxcVVWMno1N0w4c3lqQ0ExUU8xRFFE"));
        SlackProperties slackProperties = new SlackProperties(true, notificationTypes, slackArg1);

        String pushOverArg1 = new String(Base64.getDecoder().decode("YWM1ZjJya2JwejEyMnBqenNlMnBlbmJkdmYxZ2dh"));
        String pushOverArg2 = new String(Base64.getDecoder().decode("dTdiemZkZW5kdmRqbnM1eXRrMmV3YjIxZDduaWJ5"));
        Integer priority = 1;
        String sound = "spacealarm";
        Integer retry = 1;
        Integer expire = 2;
        PushOverProperties pushOverProperties = new PushOverProperties(true, notificationTypes, pushOverArg1, pushOverArg2, priority, sound, retry, expire);

        String pushBulletArg1 = new String(Base64.getDecoder().decode("Z2Fwcw=="));
        String pushBulletArg2 = new String(Base64.getDecoder().decode("by5pdjBXbXRuTFdJM3hmaFRTTmh1dVF2Tm1vdlVGSHN6dA=="));
        PushBulletProperties pushBulletProperties = new PushBulletProperties(true, notificationTypes, pushBulletArg1, pushBulletArg2);

        String gotifyArg1 = new String(Base64.getDecoder().decode("aHR0cDovLzE5Mi4xNjguMS44OjgwNzA="));
        String gotifyArg2 = new String(Base64.getDecoder().decode("QVJkbS55SHRQUTlhaW5i"));
        GotifyProperties gotifyProperties = new GotifyProperties(true, notificationTypes, gotifyArg1, gotifyArg2);

        String emailArg1 = new String(Base64.getDecoder().decode("amg1OTc1"));
        String emailArg2 = new String(Base64.getDecoder().decode("aWJnYnR3aXdyd2xvZWNucA=="));
        String emailArg3 = new String(Base64.getDecoder().decode("amg1OTc1QGdtYWlsLmNvbQ=="));
        String emailArg4 = new String(Base64.getDecoder().decode("amg1OTc1QGdtYWlsLmNvbQ=="));
        String emailArg5 = new String(Base64.getDecoder().decode("c210cC5nbWFpbC5jb20="));
        Integer emailArg6 = Integer.valueOf(new String(Base64.getDecoder().decode("NTg3")));
        String emailArg7 = new String(Base64.getDecoder().decode("c210cA=="));
        String emailArg8 = new String(Base64.getDecoder().decode("amg1OTc1"));
        EmailProperties emailProperties = new EmailProperties(true, notificationTypes, emailArg1, emailArg2, emailArg3, emailArg4, emailArg5, emailArg6, emailArg7, emailArg8, true);

        String discordArg1 = new String(Base64.getDecoder().decode("aHR0cHM6Ly9kaXNjb3JkYXBwLmNvbS9hcGkvd2ViaG9va3MvNzU2MzExMTkwOTU0NTczOTU2L0Uwa25VRF91akxIQU5oZTA3MGEwYW9PMUNnMGRNQUV4Z1FBbEZGTS1GZUgwNnl3VGExLU42c2ZhREpQSC1lM2dDVEZz"));
        DiscordProperties discordProperties = new DiscordProperties(true, notificationTypes, discordArg1);

        PlexServer plexServer = new PlexServer();
        //plexServer.setAddress();

        PlexProperties plexProperties = new PlexProperties();
        plexProperties.setTelegramProperties(telegramProperties);
        plexProperties.setSlackProperties(slackProperties);
        plexProperties.setPushOverProperties(pushOverProperties);
        plexProperties.setPushBulletProperties(pushBulletProperties);
        plexProperties.setGotifyProperties(gotifyProperties);
        plexProperties.setEmailProperties(emailProperties);
        plexProperties.setDiscordProperties(discordProperties);

        return plexProperties;
    }

    @Override
    public @NotNull Payload nuke() {
        return null;
    }
}
