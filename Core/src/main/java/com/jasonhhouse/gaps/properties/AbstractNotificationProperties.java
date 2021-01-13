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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jasonhhouse.gaps.NotificationType;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class AbstractNotificationProperties implements NotificationProperties {

    @NotNull
    protected final Boolean enabled;

    @NotNull
    protected final List<NotificationType> notificationTypes;

    protected AbstractNotificationProperties(@NotNull Boolean enabled, @NotNull List<NotificationType> notificationTypes) {
        this.enabled = enabled;
        this.notificationTypes = notificationTypes;
    }

    @Override
    @NotNull
    public Boolean getEnabled() {
        return BooleanUtils.isTrue(enabled);
    }

    @Override
    @NotNull
    public List<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    @Override
    public @NotNull Boolean hasTmdbConnectionApi() {
        return notificationTypes.contains(NotificationType.TMDB_API_CONNECTION);
    }

    @Override
    public @NotNull Boolean hasPlexServerConnection() {
        return notificationTypes.contains(NotificationType.PLEX_SERVER_CONNECTION);
    }

    @Override
    public @NotNull Boolean hasPlexMetadataUpdate() {
        return notificationTypes.contains(NotificationType.PLEX_METADATA_UPDATE);
    }

    @Override
    public @NotNull Boolean hasPlexLibraryUpdate() {
        return notificationTypes.contains(NotificationType.PLEX_LIBRARY_UPDATE);
    }

    @Override
    public @NotNull Boolean hasGapsMissingCollections() {
        return notificationTypes.contains(NotificationType.GAPS_MISSING_COLLECTIONS);
    }

    @Override
    public String toString() {
        return "AbstractNotificationProperties{" +
                "enabled=" + enabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
