package com.jasonhhouse.gaps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.Movie;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class IoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoService.class);

    private static final String STORAGE_FOLDER = "/usr/data/";

    private static final String TEMP_STORAGE_FOLDER = "/tmp/";

    private static final String STORAGE = "movieIds.json";

    private static final String RECOMMENDED_MOVIES = "gaps_recommended_movies.txt";

    public static final String RSS_FEED_JSON_FILE = "rssFeed.json";

    public boolean doesRssFileExist() {
        return new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE).exists();
    }

    public @NotNull String getRssFile() {
        try {
            Path path = new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE).toPath();
            String json = new String(Files.readAllBytes(path));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
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
        JSONArray jsonRecommended = new JSONArray();

        File file = new File(STORAGE_FOLDER + RSS_FEED_JSON_FILE);

        // Create writer that java will close for us.
        try (FileWriter writer = new FileWriter(file)) {

            // Creat the json file for writing to/endpoint access.
            file.createNewFile();

            for (Movie mov : recommended) {
                // Create movie JSONObject for adding to Json Array
                JSONObject obj = new JSONObject();
                obj.put("imdb_id", mov.getImdbId());
                obj.put("tvdb_id", mov.getTvdbId());
                obj.put("title", mov.getName());
                obj.put("release_date", mov.getYear());
                obj.put("poster_path", mov.getPosterUrl());
                jsonRecommended.put(obj);
            }

            // Write the JSONArray of recommended movies to the file.
            jsonRecommended.write(writer);
            writer.flush();

        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
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
            JSONArray movies = new JSONArray();
            for (Movie movie : everyMovie) {
                movies.put(movie.toJSON());
            }
            outputStream.write(movies.toString().getBytes());
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
        List<Movie> everyMovie = new ArrayList<>();
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

            JSONArray movies = new JSONArray(fullFile.toString());
            for (int i = 0; i < movies.length(); i++) {
                Movie movie = jsonToMovie(movies.getJSONObject(i));
                everyMovie.add(movie);
            }

            LOGGER.debug("everyMovie.size():" + everyMovie.size());

        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find file " + fileName);
        } catch (IOException e) {
            LOGGER.error("Can't write to file " + fileName);
        } catch (JSONException e) {
            LOGGER.error("Error parsing JSON file " + fileName, e);
        }

        return everyMovie;
    }

    private Movie jsonToMovie(JSONObject jsonMovie) throws JSONException {
        int tvdbId = jsonMovie.getInt(Movie.TVDB_ID);
        String imdbId = jsonMovie.optString(Movie.IMDB_ID);
        String name = jsonMovie.getString(Movie.NAME);
        int year = jsonMovie.getInt(Movie.YEAR);
        int collectionId = jsonMovie.getInt(Movie.COLLECTION_ID);
        String collection = jsonMovie.optString(Movie.COLLECTION);
        String posterUrl = jsonMovie.optString(Movie.POSTER);

        Movie.Builder builder = new Movie.Builder(name, year)
                .setTvdbId(tvdbId)
                .setImdbId(imdbId)
                .setCollectionId(collectionId)
                .setCollection(collection)
                .setPosterUrl(posterUrl);

        return builder.build();
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
