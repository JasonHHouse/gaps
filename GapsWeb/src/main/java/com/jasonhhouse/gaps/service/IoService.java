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
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.Rss;
import com.jasonhhouse.gaps.YamlConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IoService {

    public static final String RSS_FEED_JSON_FILE = "rssFeed.json";
    public static final String PLEX_CONFIGURATION = "plexConfiguration.json";
    public static final String PROPERTIES = "gaps.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(IoService.class);
    private static final String STORAGE = "movieIds.json";
    private static final String OWNED_MOVIES = "ownedMovies.json";
    private static final String RECOMMENDED_MOVIES = "recommendedMovies.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String STORAGE_FOLDER;

    private final YamlConfig yamlConfig;

    @Autowired
    public IoService(YamlConfig yamlConfig) {
        this.yamlConfig = yamlConfig;
        //Look for properties file for file locations
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            //Default to the same folder as the jar
            String decodedPath = "";
            try {
                String path = new File(new File(new File(IoService.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()).getParent()).getParent();
                decodedPath = URLDecoder.decode(path, "UTF-8");
                decodedPath = decodedPath.startsWith("file:\\") ? decodedPath.substring("file:\\".length()) : decodedPath;
            } catch (UnsupportedEncodingException e) {
                //Do nothing
            }
            STORAGE_FOLDER = decodedPath + "\\";
        } else {
            STORAGE_FOLDER = "/usr/data/";
        }
    }

    public @NotNull List<Movie> readRecommendedMovies(String machineIdentifier, int key) {
        LOGGER.info("readRecommendedMovies( " + machineIdentifier + ", " + key + " )");

        final File ownedMovieFile = new File(STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + RECOMMENDED_MOVIES);

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

            return objectMapper.readValue(fullFile.toString(), new TypeReference<List<Movie>>() {
            });
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + ownedMovieFile, e);
        } catch (IOException e) {
            LOGGER.error("Can't read the file " + ownedMovieFile, e);
        }

        return Collections.emptyList();
    }

    public boolean doesRssFileExist(String machineIdentifier, int key) {
        return new File(STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE).exists();
    }

    public @NotNull String getRssFile(String machineIdentifier, int key) {
        try {
            Path path = new File(STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE).toPath();
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Check for RSS file next time", e);
            return "";
        }
    }

    /**
     * Write the recommended movie list to the RSS file for endpoint to display.
     *
     * @param recommended The recommended movies. (IMDB ID is required.)
     */
    public void writeRssFile(String machineIdentifier, int key, Set<Movie> recommended) {
        File file = new File(STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + RSS_FEED_JSON_FILE);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + file.getPath());
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + file.getPath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + file.getPath(), e);
            return;
        }

        // Create writer that java will close for us.
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            List<Rss> rssList = recommended.stream().map(movie -> new Rss(movie.getImdbId(), movie.getYear(), movie.getTvdbId(), movie.getName(), movie.getPosterUrl())).collect(Collectors.toList());
            byte[] output = objectMapper.writeValueAsBytes(rssList);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + RECOMMENDED_MOVIES, e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + RECOMMENDED_MOVIES, e);
        }
    }

    /**
     * Prints out all recommended movies to recommendedMovies.json
     */
    public void writeRecommendedToFile(Set<Movie> recommended, String machineIdentifier, Integer key) {
        LOGGER.info("writeRecommendedToFile()");
        final String fileName = STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + RECOMMENDED_MOVIES;

        makeFolder(machineIdentifier, key);

        File file = new File(fileName);
        writeMovieIdsToFile(recommended, file);
    }

    /**
     * Prints out all recommended movies to recommendedMovies.json
     */
    public void writeOwnedMoviesToFile(List<Movie> ownedMovies, String machineIdentifier, int key) {
        LOGGER.info("writeOwnedMoviesToFile()");
        final String fileName = STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + OWNED_MOVIES;

        makeFolder(machineIdentifier, key);

        File file = new File(fileName);
        writeMovieIdsToFile(new HashSet<>(ownedMovies), file);
    }

    private void makeFolder(String machineIdentifier, int key) {
        File folder = new File(STORAGE_FOLDER + machineIdentifier + File.separator + key);
        if (!folder.exists()) {
            boolean isCreated = folder.mkdirs();
            if (isCreated) {
                LOGGER.info("Folder created: " + folder);
            } else {
                LOGGER.warn("Folder not created: " + folder);
            }
        }

    }

    public List<Movie> readOwnedMovies(String machineIdentifier, Integer key) {
        LOGGER.info("readOwnedMovies( " + machineIdentifier + ", " + key + " )");

        final File ownedMovieFile = new File(STORAGE_FOLDER + machineIdentifier + File.separator + key + File.separator + OWNED_MOVIES);

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

            return objectMapper.readValue(fullFile.toString(), new TypeReference<List<Movie>>() {
            });
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + ownedMovieFile, e);
        } catch (IOException e) {
            LOGGER.error("Can't read the file " + ownedMovieFile, e);
        }

        return Collections.emptyList();
    }

    /**
     * Prints out all movies to a text file movieIds.json
     */
    public void writeMovieIdsToFile(Set<Movie> everyMovie) {
        LOGGER.info("writeMovieIdsToFile()");
        final String fileName = STORAGE_FOLDER + STORAGE;
        File file = new File(fileName);
        writeMovieIdsToFile(everyMovie, file);
    }

    public void writeMovieIdsToFile(Set<Movie> everyMovie, File file) {
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + file.getName());
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + file.getAbsolutePath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + file.getAbsolutePath(), e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] output = objectMapper.writeValueAsBytes(everyMovie);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + file.getAbsolutePath(), e);
        }
    }

    public void writePlexConfiguration(Set<PlexServer> plexServers) {
        final String fileName = STORAGE_FOLDER + PLEX_CONFIGURATION;
        File file = new File(fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + file.getName());
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + file.getAbsolutePath());
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + file.getAbsolutePath(), e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] output = objectMapper.writeValueAsBytes(plexServers);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + file.getAbsolutePath(), e);
        }
    }

    public @NotNull List<PlexServer> readPlexConfiguration() {
        final String fileName = STORAGE_FOLDER + PLEX_CONFIGURATION;
        File file = new File(fileName);
        List<PlexServer> plexServers = new ArrayList<>();
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '" + fileName + "'. Most likely first run.");
            return plexServers;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            LOGGER.info(fullFile.toString());
            plexServers = objectMapper.readValue(fullFile.toString(), new TypeReference<List<PlexServer>>() {
            });
            LOGGER.info(plexServers.toString());
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            LOGGER.error("Can't read file " + fileName, e);
        }

        return plexServers;
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    public Set<Movie> readMovieIdsFromFile() {
        Set<Movie> everyMovie = Collections.emptySet();
        final String fileName = STORAGE_FOLDER + STORAGE;
        File file = new File(fileName);
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '" + fileName + "'. Most likely first run.");
            return everyMovie;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            everyMovie = objectMapper.readValue(fullFile.toString(), new TypeReference<Set<Movie>>() {
            });
            LOGGER.info("everyMovie.size():" + everyMovie.size());
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + fileName, e);
        }

        return everyMovie;
    }

    public void writeProperties(PlexSearch plexSearch) throws IOException {
        LOGGER.info("writeProperties( " + plexSearch + " )");
        Properties properties = new Properties();

        if (StringUtils.isNotEmpty(plexSearch.getMovieDbApiKey())) {
            properties.setProperty(PlexSearch.MOVIE_DB_API_KEY, plexSearch.getMovieDbApiKey());
        }

        properties.setProperty(PlexSearch.VERSION_KEY, yamlConfig.getVersion());
        properties.setProperty(PlexSearch.USERNAME_KEY, PlexSearch.USERNAME_VALUE);

        if (StringUtils.isNotEmpty(plexSearch.getPassword())) {
            properties.setProperty(PlexSearch.PASSWORD, plexSearch.getPassword());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(STORAGE_FOLDER + PROPERTIES)) {
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8)) {
                properties.store(outputStreamWriter, "");
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn(STORAGE_FOLDER + PROPERTIES + " does not exist");
            throw e;
        } catch (IOException e) {
            LOGGER.warn(STORAGE_FOLDER + PROPERTIES + " failed to parse");
            throw e;
        }

    }

    public PlexSearch readProperties() throws IOException {
        LOGGER.info("readProperties()");
        File file = new File(STORAGE_FOLDER + PROPERTIES);
        PlexSearch plexSearch = new PlexSearch();

        LOGGER.info("Can Read " + file + ": " + file.canRead());
        LOGGER.info("Can Write " + file + ": " + file.canWrite());
        LOGGER.info("Can Execute " + file + ": " + file.canExecute());

        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
                properties.load(inputStreamReader);

                if (properties.containsKey(PlexSearch.MOVIE_DB_API_KEY)) {
                    String movieDbApiKey = properties.getProperty(PlexSearch.MOVIE_DB_API_KEY);
                    plexSearch.setMovieDbApiKey(movieDbApiKey);
                }

                if (properties.containsKey(PlexSearch.PASSWORD)) {
                    String password = properties.getProperty(PlexSearch.PASSWORD);
                    plexSearch.setPassword(password);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn(file + " does not exist");
            throw e;
        } catch (IOException e) {
            LOGGER.warn(file + " failed to parse");
            throw e;
        }
        LOGGER.info("plexSearch: " + plexSearch);
        return plexSearch;
    }

    public Payload nuke() {
        LOGGER.info("nuke()");
        File folder = new File(STORAGE_FOLDER);
        try {
            nuke(folder);
            return Payload.NUKE_SUCCESSFUL;
        } catch (Exception e) {
            LOGGER.error(Payload.NUKE_UNSUCCESSFUL.getReason(), e);
            return Payload.NUKE_UNSUCCESSFUL;
        }
    }

    private void nuke(File file) {
        LOGGER.info("nuke( " + file + " )");
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
                LOGGER.info("File deleted: " + file);
            } else {
                LOGGER.warn("File not deleted: " + file);
            }
        }
    }
}
