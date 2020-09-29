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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonhhouse.gaps.plex.PlexServer;
import com.jasonhhouse.gaps.Schedule;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PlexProperties {

    @NotNull
    private final Set<PlexServer> plexServers;
    @NotNull
    private String movieDbApiKey;
    @NotNull
    private String password;
    @NotNull
    private TelegramProperties telegramProperties;
    @NotNull
    private PushBulletProperties pushBulletProperties;
    @NotNull
    private EmailProperties emailProperties;
    @NotNull
    private GotifyProperties gotifyProperties;
    @NotNull
    private SlackProperties slackProperties;
    @NotNull
    private PushOverProperties pushOverProperties;
    @NotNull
    private DiscordProperties discordProperties;
    @NotNull
    private Schedule schedule;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlexProperties(@JsonProperty(value = "plexServers") @Nullable Set<PlexServer> plexServers,
                          @JsonProperty(value = "telegramProperties") @Nullable TelegramProperties telegramProperties,
                          @JsonProperty(value = "pushBulletProperties") @Nullable PushBulletProperties pushBulletProperties,
                          @JsonProperty(value = "emailProperties") @Nullable EmailProperties emailProperties,
                          @JsonProperty(value = "gotifyProperties") @Nullable GotifyProperties gotifyProperties,
                          @JsonProperty(value = "slackProperties") @Nullable SlackProperties slackProperties,
                          @JsonProperty(value = "pushOverProperties") @Nullable PushOverProperties pushOverProperties,
                          @JsonProperty(value = "discordProperties") @Nullable DiscordProperties discordProperties,
                          @JsonProperty(value = "movieDbApiKey") @Nullable String movieDbApiKey,
                          @JsonProperty(value = "password") @Nullable String password,
                          @JsonProperty(value = "schedule") @Nullable Schedule schedule) {
        this.plexServers = plexServers == null ? new HashSet<>() : plexServers;
        this.telegramProperties = telegramProperties == null ? TelegramProperties.getDefault() : telegramProperties;
        this.pushBulletProperties = pushBulletProperties == null ? PushBulletProperties.getDefault() : pushBulletProperties;
        this.emailProperties = emailProperties == null ? EmailProperties.getDefault() : emailProperties;
        this.gotifyProperties = gotifyProperties == null ? GotifyProperties.getDefault() : gotifyProperties;
        this.slackProperties = slackProperties == null ? SlackProperties.getDefault() : slackProperties;
        this.pushOverProperties = pushOverProperties == null ? PushOverProperties.getDefault() : pushOverProperties;
        this.discordProperties = discordProperties == null ? DiscordProperties.getDefault() : discordProperties;
        this.movieDbApiKey = movieDbApiKey == null ? "" : movieDbApiKey;
        this.password = password == null ? "" : password;
        this.schedule = schedule == null ? Schedule.EVERY_MONDAY : schedule;
    }

    public PlexProperties() {
        this.plexServers = new HashSet<>();
        this.telegramProperties = TelegramProperties.getDefault();
        this.pushBulletProperties = PushBulletProperties.getDefault();
        this.emailProperties = EmailProperties.getDefault();
        this.gotifyProperties = GotifyProperties.getDefault();
        this.slackProperties = SlackProperties.getDefault();
        this.pushOverProperties = PushOverProperties.getDefault();
        this.discordProperties = DiscordProperties.getDefault();
        this.movieDbApiKey = "";
        this.password = "";
        this.schedule = Schedule.EVERY_MONDAY;
    }

    @NotNull
    public Set<PlexServer> getPlexServers() {
        return plexServers;
    }

    @NotNull
    public SlackProperties getSlackProperties() {
        return slackProperties;
    }

    public void setSlackProperties(@NotNull SlackProperties slackProperties) {
        this.slackProperties = slackProperties;
    }

    @NotNull
    public GotifyProperties getGotifyProperties() {
        return gotifyProperties;
    }

    public void setGotifyProperties(@NotNull GotifyProperties gotifyProperties) {
        this.gotifyProperties = gotifyProperties;
    }

    @NotNull
    public EmailProperties getEmailProperties() {
        return emailProperties;
    }

    public void setEmailProperties(@NotNull EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @NotNull
    public PushBulletProperties getPushBulletProperties() {
        return pushBulletProperties;
    }

    public void setPushBulletProperties(@NotNull PushBulletProperties pushBulletProperties) {
        this.pushBulletProperties = pushBulletProperties;
    }

    @NotNull
    public TelegramProperties getTelegramProperties() {
        return telegramProperties;
    }

    public void setTelegramProperties(@NotNull TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;
    }

    public void addPlexServer(@NotNull PlexServer plexServer) {
        this.plexServers.add(plexServer);
    }

    @NotNull
    public String getMovieDbApiKey() {
        return movieDbApiKey;
    }

    public void setMovieDbApiKey(@NotNull String movieDbApiKey) {
        this.movieDbApiKey = movieDbApiKey;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public PushOverProperties getPushOverProperties() {
        return pushOverProperties;
    }

    public void setPushOverProperties(@NotNull PushOverProperties pushOverProperties) {
        this.pushOverProperties = pushOverProperties;
    }

    @NotNull
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(@NotNull Schedule schedule) {
        this.schedule = schedule;
    }

    public @NotNull DiscordProperties getDiscordProperties() {
        return discordProperties;
    }

    public void setDiscordProperties(@NotNull DiscordProperties discordProperties) {
        this.discordProperties = discordProperties;
    }

    @Override
    public String toString() {
        return "PlexProperties{" +
                "plexServers=" + plexServers +
                ", movieDbApiKey='" + movieDbApiKey + '\'' +
                ", password='" + password + '\'' +
                ", telegramProperties=" + telegramProperties +
                ", pushBulletProperties=" + pushBulletProperties +
                ", emailProperties=" + emailProperties +
                ", gotifyProperties=" + gotifyProperties +
                ", slackProperties=" + slackProperties +
                ", pushOverProperties=" + pushOverProperties +
                ", discordProperties=" + discordProperties +
                ", schedule=" + schedule +
                '}';
    }
}
