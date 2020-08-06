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
import com.jasonhhouse.gaps.notifications.TelegramNotificationAgent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Notification {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationAgent.class);

    private final List<NotificationAgent> notificationAgents;

    public NotificationService(List<NotificationAgent> notificationAgents) {
        this.notificationAgents = notificationAgents;
    }

    @Override
    public void plexServerConnectFailed(PlexServer plexServer, String error) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.TEST_PLEX_SERVER, "ERROR", "Gaps Search", String.format("Connection to Plex Server %s Failed. %s", plexServer.getFriendlyName(), error));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex server connection failed to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void plexServerConnectSuccessful(PlexServer plexServer) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.TEST_PLEX_SERVER, "INFO", "Gaps Search", String.format("Connection to Plex Server %s Successful", plexServer.getFriendlyName()));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex server connection successful to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void plexLibraryScanFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.SCAN_PLEX_SERVER, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Failed. %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex library scan failed to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void plexLibraryScanSuccessful(PlexServer plexServer, PlexLibrary plexLibrary) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.SCAN_PLEX_SERVER, "INFO", "Gaps Search", String.format("Scanning Plex Server %s in %s Library Successful", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when plex library scan succeeded to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void tmdbConnectionFailed(String error) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.TEST_TMDB, "INFO", "Gaps Search", String.format("TMDB Connection Failed. %s", error));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when TMDB connection test failed to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void tmdbConnectionSuccessful() {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.TEST_TMDB, "INFO", "Gaps Search", "TMDB Connection Successful");
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when TMDB connection test succeeded to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void recommendedMoviesSearchStarted(PlexServer plexServer, PlexLibrary plexLibrary) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Started", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies started to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void recommendedMoviesSearchFailed(PlexServer plexServer, PlexLibrary plexLibrary, String error) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Failed %s", plexServer.getFriendlyName(), plexLibrary.getTitle(), error));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies failed to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public void recommendedMoviesSearchFinished(PlexServer plexServer, PlexLibrary plexLibrary) {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.isEnabled()) {
                try {
                    notificationAgent.sendMessage(NotificationType.RECOMMENDED_MOVIES, "INFO", "Gaps Search", String.format("Scanning Plex Server %s on Library %s Successfully Finished", plexServer.getFriendlyName(), plexLibrary.getTitle()));
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to send message when recommending movies finished to %s", notificationAgent.getName()), e);
                }
            }
        }
    }

    @Override
    public boolean test() {
        boolean passedTest = true;

        for (NotificationAgent notificationAgent : notificationAgents) {
            try {
                boolean result = notificationAgent.sendMessage(NotificationType.TEST, "DEBUG", "Gaps Test", "Test Successful");
                if (!result) {
                    passedTest = false;
                }
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to send test all message to %s", notificationAgent.getName()), e);
                return false;
            }
        }

        return passedTest;
    }

    @Override
    public boolean test(int id) throws IllegalArgumentException {
        for (NotificationAgent notificationAgent : notificationAgents) {
            if (notificationAgent.getId() == id) {
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
