package com.jasonhhouse.gaps.service;

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

    }

    @Override
    public void plexServerConnectSuccessful(PlexServer plexServer) {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage("INFO", "Automatic Search", String.format("Connection to Plex Server %s Successful", plexServer.getFriendlyName())));
    }

    @Override
    public void plexLibraryScanFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {

    }

    @Override
    public void plexLibraryScanSuccessful(PlexServer plexServer, PlexLibrary plexLibrary) {

    }

    @Override
    public void tmdbConnectionFailed(String error) {

    }

    @Override
    public void tmdbConnectionSuccessful() {

    }

    @Override
    public void recommendedMoviesSearchStarted(PlexServer plexServer, PlexLibrary plexLibrary) {

    }

    @Override
    public void recommendedMoviesSearchFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {

    }

    @Override
    public void recommendedMoviesSearchFinished(PlexServer plexServer, PlexLibrary plexLibrary) {

    }

    @Override
    public void test() {
        notificationAgents
                .stream()
                .filter(NotificationAgent::isEnabled)
                .forEach(notificationAgent -> notificationAgent.sendMessage("DEBUG", "Gaps Test", "Test Successful"));
    }
}
