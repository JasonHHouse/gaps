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
import com.jasonhhouse.gaps.movie.PlexMovie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlexFileInputIo implements FileInputIo<PlexInputFileConfig, PlexMovie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbFileOutputIo.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final GapsConfiguration gapsConfiguration;

    public PlexFileInputIo(GapsConfiguration gapsConfiguration) {
        this.gapsConfiguration = gapsConfiguration;
    }

    @Override
    public @NotNull Boolean writeOwnedMovies(@NotNull PlexInputFileConfig plexInputFileConfig, @NotNull Collection<PlexMovie> plexMovies) {
        LOGGER.info("writeOwnedMoviesToFile()");
        makeFolder(plexInputFileConfig.getMachineIdentifier(), plexInputFileConfig.getKey());

        if (plexInputFileConfig.getJsonFile().exists()) {
            boolean deleted = plexInputFileConfig.getJsonFile().delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file {}", plexInputFileConfig.getJsonFile().getName());
                return false;
            }
        }

        try {
            boolean created = plexInputFileConfig.getJsonFile().createNewFile();
            if (!created) {
                LOGGER.error("Can't create file {}", plexInputFileConfig.getJsonFile().getAbsolutePath());
                return false;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't create file %s", plexInputFileConfig.getJsonFile().getAbsolutePath()), e);
            return false;
        }

        try (FileOutputStream outputStream = new FileOutputStream(plexInputFileConfig.getJsonFile())) {
            byte[] output = objectMapper.writeValueAsBytes(plexMovies);
            outputStream.write(output);
            return true;
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", plexInputFileConfig.getJsonFile().getAbsolutePath()), e);
            return false;
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", plexInputFileConfig.getJsonFile().getAbsolutePath()), e);
            return false;
        }
    }

    @Override
    public @NotNull List<PlexMovie> readOwnedMovies(@NotNull PlexInputFileConfig plexInputFileConfig) {
        LOGGER.info("readOwnedMovies( {} )", plexInputFileConfig);

        final File ownedMovieFile = Paths.get(gapsConfiguration.getStorageFolder(), plexInputFileConfig.getJsonFile().getAbsolutePath()).toFile();

        if (!ownedMovieFile.exists()) {
            LOGGER.warn(ownedMovieFile + " does not exist");
            return Collections.emptyList();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ownedMovieFile), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            return objectMapper.readValue(fullFile.toString(), new TypeReference<>() {
            });
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", ownedMovieFile), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't read the file %s", ownedMovieFile), e);
        }

        return Collections.emptyList();
    }

    private void makeFolder(@NotNull String machineIdentifier, @NotNull Integer key) {
        File folder = Paths.get(gapsConfiguration.getStorageFolder(), machineIdentifier, key.toString()).toFile();
        if (!folder.exists()) {
            boolean isCreated = folder.mkdirs();
            if (isCreated) {
                LOGGER.info("Folder created: {}", folder);
            } else {
                LOGGER.warn("Folder not created: {}", folder);
            }
        }
    }

}
