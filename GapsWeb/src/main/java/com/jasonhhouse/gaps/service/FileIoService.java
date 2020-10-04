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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.GapsConfiguration;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.Rss;
import com.jasonhhouse.gaps.movie.BasicMovie;
import com.jasonhhouse.gaps.movie.GapsMovie;
import com.jasonhhouse.gaps.movie.PlexMovie;
import com.jasonhhouse.gaps.movie.TmdbMovie;
import com.jasonhhouse.gaps.properties.PlexProperties;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileIoService implements IO {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileIoService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final GapsConfiguration gapsConfiguration;

    @Autowired
    public FileIoService(GapsConfiguration gapsConfiguration) {
        this.gapsConfiguration = gapsConfiguration;
    }

    @NotNull
    public <T extends GapsMovie> Set<T> readMovieIdsFromFile() {
        Set<T> everyBasicMovie = Collections.emptySet();
        final File file = Paths.get(gapsConfiguration.getStorageFolder(), gapsConfiguration.getProperties().getMovieIds()).toFile();
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '{}'. Most likely first run.", file);
            return everyBasicMovie;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            everyBasicMovie = objectMapper.readValue(fullFile.toString(), new TypeReference<>() {
            });
            LOGGER.info("everyMovie.size():{}", everyBasicMovie.size());
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", file), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", file), e);
        }

        return everyBasicMovie;
    }

    @Override
    public void writeProperties(@NotNull PlexProperties plexProperties) {
        LOGGER.info("writeProperties( {} )", plexProperties);

        final File propertiesFile = Paths.get(gapsConfiguration.getStorageFolder(), gapsConfiguration.getProperties().getGapsProperties()).toFile();
        if (propertiesFile.exists()) {
            try {
                Files.delete(propertiesFile.toPath());
            } catch (IOException e) {
                LOGGER.error(String.format("Can't delete existing file %s", propertiesFile.getName()), e);
                return;
            }
        }

        try {
            boolean created = propertiesFile.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file {}", propertiesFile.getAbsolutePath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't create file %s", propertiesFile.getAbsolutePath()), e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(propertiesFile)) {
            byte[] output = objectMapper.writeValueAsBytes(plexProperties);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", propertiesFile.getAbsolutePath()), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", propertiesFile.getAbsolutePath()), e);
        }
    }

    @Override
    @NotNull
    public PlexProperties readProperties() {
        LOGGER.info("readProperties()");

        final File file = Paths.get(gapsConfiguration.getStorageFolder(), gapsConfiguration.getProperties().getGapsProperties()).toFile();
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '{}'. Most likely first run.", file);
            return new PlexProperties();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            return objectMapper.readValue(fullFile.toString(), PlexProperties.class);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", file), e);
            return new PlexProperties();
        } catch (IOException e) {
            LOGGER.error(String.format("Can't read file %s", file), e);
            return new PlexProperties();
        }
    }

    @Override
    @NotNull
    public Payload nuke() {
        LOGGER.info("nuke()");
        File folder = new File(gapsConfiguration.getStorageFolder());
        try {
            nuke(folder);
            return Payload.NUKE_SUCCESSFUL;
        } catch (Exception e) {
            LOGGER.error(Payload.NUKE_UNSUCCESSFUL.getReason(), e);
            return Payload.NUKE_UNSUCCESSFUL;
        }
    }

    private void nuke(File file) {
        LOGGER.info("nuke( {} )", file);
        if (!file.isFile()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File children : files) {
                nuke(children);
            }
        } else {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                LOGGER.info("File deleted: {}", file);
            } else {
                LOGGER.warn("File not deleted: {}", file);
            }
        }
    }
}
