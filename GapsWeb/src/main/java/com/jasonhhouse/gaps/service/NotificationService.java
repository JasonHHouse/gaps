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

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.notifications.NotificationAgent;
import com.jasonhhouse.gaps.properties.NotificationProperties;
import com.jasonhhouse.plex.libs.PlexLibrary;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Notification {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final List<NotificationAgent<? extends NotificationProperties>> notificationAgents;

    public NotificationService(List<NotificationAgent<? extends NotificationProperties>> notificationAgents) {
        this.notificationAgents = notificationAgents;
    }

    @Override
    public @NotNull Boolean plexServerConnectFailed(@NotNull PlexServer plexServer, @NotNull String error) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.PLEX_SERVER_CONNECTION, "ERROR", "Gaps Search", String.format("Connection to Plex Server %s Failed. %s", plexServer.getFriendlyName(), error));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex server connection failed to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean plexServerConnectSuccessful(@NotNull PlexServer plexServer) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.PLEX_SERVER_CONNECTION, "INFO", "Gaps Search", String.format("Connection to Plex Server %s Successful", plexServer.getFriendlyName()));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex server connection successful to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean plexLibraryScanFailed(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary, @NotNull String error) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.PLEX_METADATA_UPDATE, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Failed. %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex library scan failed to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean plexLibraryScanSuccessful(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.PLEX_METADATA_UPDATE, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Successful", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex library scan succeeded to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean tmdbConnectionFailed(@NotNull String error) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.TMDB_API_CONNECTION, "INFO", "Gaps Search", String.format("TMDB Connection Failed. %s", error));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when TMDB connection test failed to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean tmdbConnectionSuccessful() {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.TMDB_API_CONNECTION, "INFO", "Gaps Search", "TMDB Connection Successful");
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when TMDB connection test succeeded to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean recommendedMoviesSearchStarted(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.GAPS_MISSING_COLLECTIONS, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Started", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies started to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean recommendedMoviesSearchFailed(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary, @NotNull String error) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.GAPS_MISSING_COLLECTIONS, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Failed %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies failed to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean recommendedMoviesSearchFinished(@NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary) {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    Boolean result = notificationAgent.sendMessage(NotificationType.GAPS_MISSING_COLLECTIONS, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Successfully Finished", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                    if (!result) {
                        sentAllNotifications = false;
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies finished to %s", notificationAgent.getName()), e);
                    sentAllNotifications = false;
                }
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean test() {
        boolean sentAllNotifications = true;
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            try {
                Boolean result = notificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
                if (!result) {
                    sentAllNotifications = false;
                }
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to send test all message to %s", notificationAgent.getName()), e);
                sentAllNotifications = false;
            }
        }
        return sentAllNotifications;
    }

    @Override
    public @NotNull Boolean test(@NotNull Integer id) throws IllegalArgumentException {
        for (NotificationAgent<? extends NotificationProperties> notificationAgent : notificationAgents) {
            if (notificationAgent.getId().equals(id)) {
                try {
                    return notificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send test message to %s", notificationAgent.getName()), e);
                    return false;
                }
            }
        }
        throw new IllegalArgumentException("Invalid Id for Notification Agent");
    }
}
