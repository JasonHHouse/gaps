
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

package com.jasonhhouse.gaps.plex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlexLibrary {

    @NotNull
    private final Integer key;
    @NotNull
    private final String type;
    @NotNull
    private final String title;
    @NotNull
    private final String agent;
    @NotNull
    private final String language;
    @NotNull
    private final String uuid;
    @NotNull
    private final String scanner;

    @JsonCreator
    public PlexLibrary(@JsonProperty(value = "key") @Nullable Integer key,
                       @JsonProperty(value = "type") @Nullable String type,
                       @JsonProperty(value = "title") @Nullable String title,
                       @JsonProperty(value = "agent") @Nullable String agent,
                       @JsonProperty(value = "language") @Nullable String language,
                       @JsonProperty(value = "uuid") @Nullable String uuid,
                       @JsonProperty(value = "scanner") @Nullable String scanner) {
        this.key = key == null ? -1 : key;
        this.type = StringUtils.isEmpty(type) ? "" : type;
        this.title = StringUtils.isEmpty(title) ? "" : title;
        this.agent = StringUtils.isEmpty(agent) ? "" : agent;
        this.language = StringUtils.isEmpty(language) ? "" : language;
        this.uuid = StringUtils.isEmpty(uuid) ? "" : uuid;
        this.scanner = StringUtils.isEmpty(scanner) ? "" : scanner;
    }

    public @NotNull Integer getKey() {
        return key;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getAgent() {
        return agent;
    }

    public @NotNull String getLanguage() {
        return language;
    }

    public @NotNull String getUuid() {
        return uuid;
    }

    public @NotNull String getScanner() {
        return scanner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlexLibrary that = (PlexLibrary) o;
        return key.equals(that.key) &&
                type.equals(that.type) &&
                title.equals(that.title) &&
                agent.equals(that.agent) &&
                language.equals(that.language) &&
                uuid.equals(that.uuid) &&
                scanner.equals(that.scanner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type, title, agent, language, uuid, scanner);
    }

    @Override
    public String toString() {
        return "PlexLibrary{" +
                "key=" + key +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", agent='" + agent + '\'' +
                ", language='" + language + '\'' +
                ", uuid='" + uuid + '\'' +
                ", scanner='" + scanner + '\'' +
                '}';
    }
}
