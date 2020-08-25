/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.properties;

import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.Schedule;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class PlexProperties {

    private final Set<PlexServer> plexServers;
    private TelegramProperties telegramProperties;
    private PushBulletProperties pushBulletProperties;
    private EmailProperties emailProperties;
    private GotifyProperties gotifyProperties;
    private SlackProperties slackProperties;
    private PushOverProperties pushOverProperties;
    private String movieDbApiKey;
    private String password;

    @NotNull
    private Schedule schedule;

    public PlexProperties() {
        plexServers = new HashSet<>();
        schedule = Schedule.EVERY_MONDAY;
    }

    public SlackProperties getSlackProperties() {
        return slackProperties;
    }

    public void setSlackProperties(SlackProperties slackProperties) {
        this.slackProperties = slackProperties;
    }

    public GotifyProperties getGotifyProperties() {
        return gotifyProperties;
    }

    public void setGotifyProperties(GotifyProperties gotifyProperties) {
        this.gotifyProperties = gotifyProperties;
    }

    public EmailProperties getEmailProperties() {
        return emailProperties;
    }

    public void setEmailProperties(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public PushBulletProperties getPushBulletProperties() {
        return pushBulletProperties;
    }

    public void setPushBulletProperties(PushBulletProperties pushBulletProperties) {
        this.pushBulletProperties = pushBulletProperties;
    }

    public TelegramProperties getTelegramProperties() {
        return telegramProperties;
    }

    public void setTelegramProperties(TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;
    }

    public void addPlexServer(PlexServer plexServer) {
        this.plexServers.add(plexServer);
    }

    public Set<PlexServer> getPlexServers() {
        return plexServers;
    }

    public String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PushOverProperties getPushOverProperties() {
        return pushOverProperties;
    }

    public void setPushOverProperties(PushOverProperties pushOverProperties) {
        this.pushOverProperties = pushOverProperties;
    }

    @NotNull
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(@NotNull Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "PlexProperties{" +
                "plexServers=" + plexServers +
                ", telegramProperties=" + telegramProperties +
                ", pushBulletProperties=" + pushBulletProperties +
                ", emailProperties=" + emailProperties +
                ", gotifyProperties=" + gotifyProperties +
                ", slackProperties=" + slackProperties +
                ", pushOverProperties=" + pushOverProperties +
                ", movieDbApiKey='" + movieDbApiKey + '\'' +
                ", password='" + password + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}
