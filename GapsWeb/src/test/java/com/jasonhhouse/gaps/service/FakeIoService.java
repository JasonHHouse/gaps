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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.properties.DiscordProperties;
import com.jasonhhouse.gaps.properties.EmailProperties;
import com.jasonhhouse.gaps.properties.GotifyProperties;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.properties.PushBulletProperties;
import com.jasonhhouse.gaps.properties.PushOverProperties;
import com.jasonhhouse.gaps.properties.SlackProperties;
import com.jasonhhouse.gaps.properties.TelegramProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class FakeIoService implements IO {

    @Override
    public @NotNull List<BasicMovie> readRecommendedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public @NotNull Boolean doesRssFileExist(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public @NotNull String getRssFile(String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public void writeRssFile(@NotNull String machineIdentifier, @NotNull Integer key, @NotNull Set<BasicMovie> recommended) {

    }

    @Override
    public void writeRecommendedToFile(@NotNull Set<BasicMovie> recommended, @NotNull String machineIdentifier, @NotNull Integer key) {

    }

    @Override
    public void writeOwnedMoviesToFile(@NotNull List<BasicMovie> ownedBasicMovies, @NotNull String machineIdentifier, @NotNull Integer key) {

    }

    @Override
    public @NotNull List<BasicMovie> readOwnedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        return null;
    }

    @Override
    public void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie) {

    }

    @Override
    public void writeMovieIdsToFile(@NotNull Set<BasicMovie> everyBasicMovie, @NotNull File file) {

    }

    @Override
    public @NotNull Set<BasicMovie> readMovieIdsFromFile() {
        return null;
    }

    @Override
    public void writeProperties(@NotNull PlexProperties plexProperties) {

    }

    @Override
    public @NotNull PlexProperties readProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        final File file = Paths.get("src", "test", "resources", "gaps-test.json").toFile();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            return objectMapper.readValue(fullFile.toString(), PlexProperties.class);
        } catch (IOException e) {
            return new PlexProperties();
        }
    }

    @Override
    public @NotNull Payload nuke() {
        return null;
    }
}
