package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexServer;

public interface Notification {

    void plexServerConnectFailed(PlexServer plexServer, String error);

    void plexServerConnectSuccessful(PlexServer plexServer);

    void plexLibraryScanFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error);

    void plexLibraryScanSuccessful(PlexServer plexServer, PlexLibrary plexLibrary);

    void tmdbConnectionFailed(String error);

    void tmdbConnectionSuccessful();

    void recommendedMoviesSearchStarted(PlexServer plexServer, PlexLibrary plexLibrary);

    void recommendedMoviesSearchFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error);

    void recommendedMoviesSearchFinished(PlexServer plexServer, PlexLibrary plexLibrary);
}
