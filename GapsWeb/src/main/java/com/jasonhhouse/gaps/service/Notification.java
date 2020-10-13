package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.plex.libs.PlexLibrary;
import org.jetbrains.annotations.NotNull;

public interface Notification {

    @NotNull Boolean plexServerConnectFailed(@NotNull PlexServer plexServer, @NotNull String error);

    @NotNull Boolean plexServerConnectSuccessful(@NotNull PlexServer plexServer);

    @NotNull Boolean plexLibraryScanFailed(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary, @NotNull String error);

    @NotNull Boolean plexLibraryScanSuccessful(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary);

    @NotNull Boolean tmdbConnectionFailed(@NotNull String error);

    @NotNull Boolean tmdbConnectionSuccessful();

    @NotNull Boolean recommendedMoviesSearchStarted(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary);

    @NotNull Boolean recommendedMoviesSearchFailed(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary,@NotNull  String error);

    @NotNull Boolean recommendedMoviesSearchFinished(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary);

    @NotNull Boolean test();

    @NotNull Boolean test(@NotNull Integer id) throws IllegalArgumentException, IllegalAccessException;
}
