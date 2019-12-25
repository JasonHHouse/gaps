package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.Rss;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoService.class);

    private static final String STORAGE_FOLDER = "/usr/data/";

    private static final String TEMP_STORAGE_FOLDER = "/tmp/";

    private static final String STORAGE = "movieIds.json";

    private static final String RECOMMENDED_MOVIES = "gaps_recommended_movies.txt";

    public static final String RSS_FEED_JSON_FILE = "rssFeed.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public boolean doesRssFileExist() {
        return new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE).exists();
    }

    public @NotNull String getRssFile() {
        try {
            Path path = new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE).toPath();
            return new String(Files.readAllBytes(path));
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
    public void writeRssFile(List<Movie> recommended) {
        File file = new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + STORAGE_FOLDER + RSS_FEED_JSON_FILE);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + STORAGE_FOLDER + RSS_FEED_JSON_FILE);
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + STORAGE_FOLDER + RSS_FEED_JSON_FILE, e);
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

    public void migrateJsonSeedFileIfNeeded() {
        final File seedFile = new File(STORAGE_FOLDER + STORAGE);
        if (seedFile.exists()) {
            LOGGER.info("Seed file exists, not copying over");
            return;
        }

        final File tempSeed = new File(TEMP_STORAGE_FOLDER + STORAGE);
        try {
            Files.move(tempSeed.toPath(), seedFile.toPath());
            LOGGER.info("Seed file doesn't exist, copying over");
        } catch (IOException e) {
            LOGGER.error("Failed to copy seed file over", e);
        }
    }

    /**
     * Prints out all recommended files to the terminal or command line
     */
    public void printRecommended(List<Movie> recommended) {
        LOGGER.info(recommended.size() + " Recommended Movies");
        for (Movie movie : recommended) {
            LOGGER.info(movie.toString());
        }
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    public void writeMovieIdsToFile(List<Movie> everyMovie) {
        final String fileName = STORAGE_FOLDER + STORAGE;
        File file = new File(fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + fileName);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + fileName);
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + fileName, e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] output = objectMapper.writeValueAsBytes(everyMovie);
            outputStream.write(output);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + fileName, e);
        }
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    public List<Movie> readMovieIdsFromFile() {
        List<Movie> everyMovie = Collections.emptyList();
        final String fileName = STORAGE_FOLDER + STORAGE;
        File file = new File(fileName);
        if (!file.exists()) {
            LOGGER.warn("Can't find json file '" + fileName + "'. Most likely first run.");
            return everyMovie;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            LOGGER.debug(fullFile.toString());
            everyMovie = objectMapper.readValue(fullFile.toString(), new TypeReference<List<Movie>>() {
            });
            LOGGER.debug("everyMovie.size():" + everyMovie.size());
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + fileName);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + fileName);
        }

        return everyMovie;
    }

    /**
     * Prints out all recommended files to a text file called gaps_recommended_movies.txt
     */
    public void writeToFile(List<Movie> recommended) {
        File file = new File(RECOMMENDED_MOVIES);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.error("Can't delete existing file " + RECOMMENDED_MOVIES);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                LOGGER.error("Can't create file " + RECOMMENDED_MOVIES);
                return;
            }
        } catch (IOException e) {
            LOGGER.error("Can't create file " + RECOMMENDED_MOVIES, e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(RECOMMENDED_MOVIES)) {
            for (Movie movie : recommended) {
                String output = movie.toString() + System.lineSeparator();
                outputStream.write(output.getBytes());
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + RECOMMENDED_MOVIES, e);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + RECOMMENDED_MOVIES, e);
        }
    }
}
