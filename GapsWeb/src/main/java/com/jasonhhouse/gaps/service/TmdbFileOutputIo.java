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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.GapsConfiguration;
import com.jasonhhouse.gaps.Rss;
import com.jasonhhouse.gaps.movie.OutputMovie;
import com.jasonhhouse.gaps.movie.PlexMovie;
import com.jasonhhouse.gaps.movie.TmdbMovie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TmdbFileOutputIo implements FileOutputIo<TmdbOutputFileConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbFileOutputIo.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @NotNull
    private final GapsConfiguration gapsConfiguration;

    public TmdbFileOutputIo(@NotNull GapsConfiguration gapsConfiguration) {
        this.gapsConfiguration = gapsConfiguration;
    }

    @Override
    public @NotNull Boolean doesRssFileExist(@NotNull TmdbOutputFileConfig tmdbOutputFileConfig) {
        return tmdbOutputFileConfig.getRssFile().exists();
    }

    @Override
    public @NotNull String readRss(@NotNull TmdbOutputFileConfig tmdbOutputFileConfig) {
        try {
            Path path = Paths.get(tmdbOutputFileConfig.getRssFile().toURI());
            return Files.readString(path);
        } catch (IOException e) {
            LOGGER.error("Check for RSS file next time", e);
            return "";
        }
    }

    @Override
    public @NotNull <OM extends OutputMovie> Boolean writeRss(@NotNull TmdbOutputFileConfig tmdbOutputFileConfig, @NotNull Collection<OM> outputMovies) {
        final File file = Paths.get(gapsConfiguration.getStorageFolder(), tmdbOutputFileConfig.getRssFile().getAbsolutePath()).toFile();

        if (!handleFolderWritePrep(file)) {
            return false;
        }

        // Create writer that java will close for us.
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            List<Rss> rssList = outputMovies.stream().map(movie -> new Rss(movie.getImdbId(), movie.getYear(), movie.getTmdbId(), movie.getName(), movie.getPosterUrl())).collect(Collectors.toList());
            byte[] output = objectMapper.writeValueAsBytes(rssList);
            outputStream.write(output);
            return true;
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", tmdbOutputFileConfig.getRssFile()), e);
            return false;
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", tmdbOutputFileConfig.getRssFile()), e);
            return false;
        }
    }

    @Override
    public @NotNull <OM extends OutputMovie> List<OM> readRecommendedMovies(@NotNull TmdbOutputFileConfig tmdbOutputFileConfig) {
        LOGGER.info("readRecommendedMovies( {} )", tmdbOutputFileConfig);

        final File file = Paths.get(gapsConfiguration.getStorageFolder(), tmdbOutputFileConfig.getRssFile().getAbsolutePath()).toFile();

        if (!handleFolderReadPrep(file)) {
            return Collections.emptyList();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            return objectMapper.readValue(fullFile.toString(), new TypeReference<>() {
            });
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", file), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't read the file %s", file), e);
        }

        return Collections.emptyList();
    }

    @Override
    public @NotNull <OM extends OutputMovie> Boolean writeRecommendedToFile(@NotNull TmdbOutputFileConfig tmdbOutputFileConfig, @NotNull Set<OM> recommended) {
        LOGGER.info("writeRecommendedToFile( {} )", tmdbOutputFileConfig);

        final File file = Paths.get(gapsConfiguration.getStorageFolder(), tmdbOutputFileConfig.getRssFile().getAbsolutePath()).toFile();

        if(!handleFolderWritePrep(file)) {
            return false;
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] output = objectMapper.writeValueAsBytes(recommended);
            outputStream.write(output);
            return true;
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", file.getAbsolutePath()), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", file.getAbsolutePath()), e);
        }

        return false;
    }

    private @NotNull Boolean handleFolderWritePrep(@NotNull File file) {
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file {}", file.getPath());
                return false;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file {}", file.getPath());
                return false;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't create file %s", file.getPath()), e);
            return false;
        }

        return true;
    }

    private @NotNull Boolean handleFolderReadPrep(@NotNull File file) {
        if (!file.exists()) {
            LOGGER.warn("{} does not exist", file);
            return false;
        }
        return true;
    }

}
