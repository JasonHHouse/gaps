/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

    @NotNull Boolean recommendedMoviesSearchFailed(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary, @NotNull String error);

    @NotNull Boolean recommendedMoviesSearchFinished(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary);

    @NotNull Boolean test();

    @NotNull Boolean test(@NotNull Integer id);

    @NotNull Boolean isAnyNotificationAgentEnabled();
}
