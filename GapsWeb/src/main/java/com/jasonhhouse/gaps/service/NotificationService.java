/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.notifications.NotificationAgent;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Notification {

    private final List<NotificationAgent> notificationAgents;

    public NotificationService(List<NotificationAgent> notificationAgents) {
        this.notificationAgents = notificationAgents;
    }

    @Override
    public void plexServerConnectFailed(PlexServer plexServer, String error) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.TEST_PLEX_SERVER, "ERROR", "Gaps Search", String.format("Connection to Plex Server %s Failed. %s", plexServer.getFriendlyName(), error)));
    }

    @Override
    public void plexServerConnectSuccessful(PlexServer plexServer) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.TEST_PLEX_SERVER, "INFO", "Gaps Search", String.format("Connection to Plex Server %s Successful", plexServer.getFriendlyName())));
    }

    @Override
    public void plexLibraryScanFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.SCAN_PLEX_SERVER, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Failed. %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error)));
    }

    @Override
    public void plexLibraryScanSuccessful(PlexServer plexServer, PlexLibrary plexLibrary) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.SCAN_PLEX_SERVER, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Successful", plexServer.getFriendlyName(), plexLibrary.getTitle())));
    }

    @Override
    public void tmdbConnectionFailed(String error) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.TEST_TMDB, "INFO", "Gaps Search", String.format("TMDB Connection Failed. %s", error)));
    }

    @Override
    public void tmdbConnectionSuccessful() {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.TEST_TMDB, "INFO", "Gaps Search", "TMDB Connection Successful"));
    }

    @Override
    public void recommendedMoviesSearchStarted(PlexServer plexServer, PlexLibrary plexLibrary) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Started", plexServer.getFriendlyName(), plexLibrary.getTitle())));
    }

    @Override
    public void recommendedMoviesSearchFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Failed %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error)));
    }

    @Override
    public void recommendedMoviesSearchFinished(PlexServer plexServer, PlexLibrary plexLibrary) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Successfully Finished", plexServer.getFriendlyName(), plexLibrary.getTitle())));
    }

    @Override
    public boolean test() {
        boolean passedTest = true;

        for (NotificationAgent notificationAgent : notificationAgents) {
            boolean result = notificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
            if (!result) {
                passedTest = false;
            }
        }

        return passedTest;
    }

    @Override
    public boolean test(int id) throws IllegalArgumentException {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.getId() == id) {
                return notificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
            }
        }
        throw new IllegalArgumentException("Invalid Id for Notification Agent");
    }
}
