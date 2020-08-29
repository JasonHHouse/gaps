/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.plex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class PlexLibrary implements Comparable<PlexLibrary> {

    private final @NotNull String allowSync;
    private final @NotNull String art;
    private final @NotNull String composite;
    private final @NotNull String filters;
    private final @NotNull String refreshing;
    private final @NotNull String thumb;
    private final @NotNull Integer key;
    private final @NotNull String type;
    private final @NotNull String title;
    private final @NotNull String agent;
    private final @NotNull String scanner;
    private final @NotNull String language;
    private final @NotNull String uuid;
    private final @NotNull String updatedAt;
    private final @NotNull String createdAt;
    private final @NotNull String scannedAt;
    private final @NotNull String content;
    private final @NotNull String directory;
    private final @NotNull String contentChangedAt;
    private final @NotNull String hidden;
    private final @NotNull String machineIdentifier;
    private final @NotNull Boolean selected;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlexLibrary(@JsonProperty(value = "allowSync", required = true) @NotNull String allowSync,
                       @JsonProperty(value = "art", required = true) @NotNull String art,
                       @JsonProperty(value = "composite", required = true) @NotNull String composite,
                       @JsonProperty(value = "filters", required = true) @NotNull String filters,
                       @JsonProperty(value = "refreshing", required = true) @NotNull String refreshing,
                       @JsonProperty(value = "thumb", required = true) @NotNull String thumb,
                       @JsonProperty(value = "key", required = true) @NotNull Integer key,
                       @JsonProperty(value = "type", required = true) @NotNull String type,
                       @JsonProperty(value = "title", required = true) @NotNull String title,
                       @JsonProperty(value = "agent", required = true) @NotNull String agent,
                       @JsonProperty(value = "scanner", required = true) @NotNull String scanner,
                       @JsonProperty(value = "language", required = true) @NotNull String language,
                       @JsonProperty(value = "uuid", required = true) @NotNull String uuid,
                       @JsonProperty(value = "updatedAt", required = true) @NotNull String updatedAt,
                       @JsonProperty(value = "createdAt", required = true) @NotNull String createdAt,
                       @JsonProperty(value = "scannedAt", required = true) @NotNull String scannedAt,
                       @JsonProperty(value = "content", required = true) @NotNull String content,
                       @JsonProperty(value = "directory", required = true) @NotNull String directory,
                       @JsonProperty(value = "contentChangedAt", required = true) @NotNull String contentChangedAt,
                       @JsonProperty(value = "hidden", required = true) @NotNull String hidden,
                       @JsonProperty(value = "machineIdentifier", required = true) @NotNull String machineIdentifier,
                       @JsonProperty(value = "selected", required = true) @NotNull Boolean selected) {
        this.allowSync = allowSync;
        this.art = art;
        this.composite = composite;
        this.filters = filters;
        this.refreshing = refreshing;
        this.thumb = thumb;
        this.key = key;
        this.type = type;
        this.title = title;
        this.agent = agent;
        this.scanner = scanner;
        this.language = language;
        this.uuid = uuid;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.scannedAt = scannedAt;
        this.content = content;
        this.directory = directory;
        this.contentChangedAt = contentChangedAt;
        this.hidden = hidden;
        this.machineIdentifier = machineIdentifier;
        this.selected = selected;
    }

    public @NotNull Integer getKey() {
        return key;
    }

    public @NotNull String getMachineIdentifier() {
        return machineIdentifier;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull Boolean getSelected() {
        return selected;
    }

    public @NotNull String getAgent() {
        return agent;
    }

    public @NotNull String getAllowSync() {
        return allowSync;
    }

    public @NotNull String getArt() {
        return art;
    }

    public @NotNull String getComposite() {
        return composite;
    }

    public @NotNull String getFilters() {
        return filters;
    }

    public @NotNull String getRefreshing() {
        return refreshing;
    }

    public @NotNull String getThumb() {
        return thumb;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull String getUuid() {
        return uuid;
    }

    public @NotNull String getUpdatedAt() {
        return updatedAt;
    }

    public @NotNull String getCreatedAt() {
        return createdAt;
    }

    public @NotNull String getScannedAt() {
        return scannedAt;
    }

    public @NotNull String getContent() {
        return content;
    }

    public @NotNull String getDirectory() {
        return directory;
    }

    public @NotNull String getContentChangedAt() {
        return contentChangedAt;
    }

    public @NotNull String getHidden() {
        return hidden;
    }

    public @NotNull String getScanner() {
        return scanner;
    }

    public @NotNull String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "plexapp='" + plexapp + '\'' +
                ", art='" + art + '\'' +
                ", composite='" + composite + '\'' +
                ", filters='" + filters + '\'' +
                ", refreshing='" + refreshing + '\'' +
                ", thumb='" + thumb + '\'' +
                ", key=" + key +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", agent='" + agent + '\'' +
                ", scanner='" + scanner + '\'' +
                ", language='" + language + '\'' +
                ", uuid='" + uuid + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", scannedAt='" + scannedAt + '\'' +
                ", content='" + content + '\'' +
                ", directory='" + directory + '\'' +
                ", contentChangedAt='" + contentChangedAt + '\'' +
                ", hidden='" + hidden + '\'' +
                ", machineIdentifier='" + machineIdentifier + '\'' +
                ", selected=" + selected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexLibrary that = (PlexLibrary) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(title, that.title) &&
                Objects.equals(machineIdentifier, that.machineIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, title, machineIdentifier);
    }

    @Override
    public int compareTo(@NotNull PlexLibrary o) {
        return this.title.compareTo(o.title);
    }
}
