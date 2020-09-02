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
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.Rss;
import com.jasonhhouse.gaps.YamlConfig;
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

    public static final String RSS_FEED_JSON_FILE = "rssFeed.json";
    public static final String PLEX_CONFIGURATION = "plexConfiguration.json";
    public static final String PROPERTIES = "gaps.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileIoService.class);
    private static final String STORAGE = "movieIds.json";
    private static final String OWNED_MOVIES = "ownedMovies.json";
    private static final String RECOMMENDED_MOVIES = "recommendedMovies.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final YamlConfig yamlConfig;

    @Autowired
    public FileIoService(YamlConfig yamlConfig) {
        this.yamlConfig = yamlConfig;
    }

    @Override
    public @NotNull List<Movie> readRecommendedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        LOGGER.info("readRecommendedMovies({}, {} )", machineIdentifier, key);

        final File ownedMovieFile = new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + RECOMMENDED_MOVIES);

        if (!ownedMovieFile.exists()) {
            LOGGER.warn("{} does not exist", ownedMovieFile);
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

    @Override
    public @NotNull Boolean doesRssFileExist(@NotNull String machineIdentifier, @NotNull Integer key) {
        return new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE).exists();
    }

    @Override
    public @NotNull String getRssFile(String machineIdentifier, @NotNull Integer key) {
        try {
            Path path = new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE).toPath();
            return Files.readString(path);
        } catch (IOException e) {
            LOGGER.error("Check for RSS file next time", e);
            return "";
        }
    }

    @Override
    public void writeRssFile(@NotNull String machineIdentifier, @NotNull Integer key, @NotNull Set<Movie> recommended) {
        File file = new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file {}", file.getPath());
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file {}", file.getPath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't create file %s", file.getPath()), e);
            return;
        }

        // Create writer that java will close for us.
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            List<Rss> rssList = recommended.stream().map(movie -> new Rss(movie.getImdbId(), movie.getYear(), movie.getTvdbId(), movie.getName(), movie.getPosterUrl())).collect(Collectors.toList());
            byte[] output = objectMapper.writeValueAsBytes(rssList);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", RECOMMENDED_MOVIES), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", RECOMMENDED_MOVIES), e);
        }
    }

    @Override
    public void writeRecommendedToFile(@NotNull Set<Movie> recommended, @NotNull String machineIdentifier, @NotNull Integer key) {
        LOGGER.info("writeRecommendedToFile()");
        final String fileName = yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + RECOMMENDED_MOVIES;

        makeFolder(machineIdentifier, key);

        File file = new File(fileName);
        writeMovieIdsToFile(recommended, file);
    }

    @Override
    public void writeOwnedMoviesToFile(@NotNull List<Movie> ownedMovies, @NotNull String machineIdentifier, @NotNull Integer key) {
        LOGGER.info("writeOwnedMoviesToFile()");
        final String fileName = yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + OWNED_MOVIES;

        makeFolder(machineIdentifier, key);

        File file = new File(fileName);
        writeMovieIdsToFile(new HashSet<>(ownedMovies), file);
    }

    private void makeFolder(@NotNull String machineIdentifier, @NotNull Integer key) {
        File folder = new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key);
        if (!folder.exists()) {
            boolean isCreated = folder.mkdirs();
            if (isCreated) {
                LOGGER.info("Folder created: {}", folder);
            } else {
                LOGGER.warn("Folder not created: {}", folder);
            }
        }

    }

    @Override
    @NotNull
    public List<Movie> readOwnedMovies(@NotNull String machineIdentifier, @NotNull Integer key) {
        LOGGER.info("readOwnedMovies( {}, {} )", machineIdentifier, key);

        final File ownedMovieFile = new File(yamlConfig.getStorageFolder() + machineIdentifier + File.separator + key + File.separator + OWNED_MOVIES);

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

    @Override
    public void writeMovieIdsToFile(@NotNull Set<Movie> everyMovie) {
        LOGGER.info("writeMovieIdsToFile()");
        final String fileName = yamlConfig.getStorageFolder() + STORAGE;
        File file = new File(fileName);
        writeMovieIdsToFile(everyMovie, file);
    }

    @Override
    public void writeMovieIdsToFile(@NotNull Set<Movie> everyMovie, @NotNull File file) {
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file {}", file.getName());
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file {}", file.getAbsolutePath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't create file %s", file.getAbsolutePath()), e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] output = objectMapper.writeValueAsBytes(everyMovie);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", file.getAbsolutePath()), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", file.getAbsolutePath()), e);
        }
    }

    @Override
    @NotNull
    public Set<Movie> readMovieIdsFromFile() {
        Set<Movie> everyMovie = Collections.emptySet();
        final String fileName = yamlConfig.getStorageFolder() + STORAGE;
        File file = new File(fileName);
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '{}'. Most likely first run.", fileName);
            return everyMovie;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            everyMovie = objectMapper.readValue(fullFile.toString(), new TypeReference<>() {
            });
            LOGGER.info("everyMovie.size():{}", everyMovie.size());
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Can't find file %s", fileName), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Can't write to file %s", fileName), e);
        }

        return everyMovie;
    }

    @Override
    public void writeProperties(@NotNull PlexProperties plexProperties) {
        LOGGER.info("writeProperties( {} )", plexProperties);

        final String properties = yamlConfig.getStorageFolder() + PROPERTIES;
        File propertiesFile = new File(properties);
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

        File file = new File(yamlConfig.getStorageFolder() + PROPERTIES);
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
        File folder = new File(yamlConfig.getStorageFolder());
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
