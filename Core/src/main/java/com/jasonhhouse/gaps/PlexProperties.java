/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class PlexProperties {

    public static final String MOVIE_DB_API_KEY = "movieDbApiKey";
    public static final String VERSION_KEY = "version";
    public static final String USERNAME_KEY = "username";
    public static final String USERNAME_VALUE = "user";
    public static final String PASSWORD = "password";
    public static final String SCHEDULE = "schedule";

    private final Set<PlexServer> plexServers;
    private String movieDbApiKey;
    private String password;

    @NotNull
    private Schedule schedule;

    public PlexProperties() {
        plexServers = new HashSet<>();
        schedule = Schedule.DAILY_4AM;
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

    @NotNull
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(@NotNull Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "PlexSearch{" +
                "plexServers=" + plexServers +
                ", movieDbApiKey='" + movieDbApiKey + '\'' +
                ", password='" + password + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }
}
