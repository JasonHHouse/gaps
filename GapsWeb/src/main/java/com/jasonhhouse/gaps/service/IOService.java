package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Movie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IOService {

    private final Logger logger = LoggerFactory.getLogger(IOService.class);

    private static final String STORAGE_FOLDER = "/usr/data/";

    private static final String TEMP_STORAGE_FOLDER = "/tmp/";

    private static final String STORAGE = "movieIds.json";

    private static final String RECOMMENDED_MOVIES = "gaps_recommended_movies.txt";

    public void migrateJsonSeedFileIfNeeded() {
        final File seedFile = new File(STORAGE_FOLDER + STORAGE);
        if (seedFile.exists()) {
            logger.info("Seed file exists, not copying over");
            return;
        }

        final File tempSeed = new File(TEMP_STORAGE_FOLDER + STORAGE);
        try {
            Files.move(tempSeed.toPath(), seedFile.toPath());
            logger.info("Seed file doesn't exist, copying over");
        } catch (IOException e) {
            logger.error("Failed to copy seed file over", e);
        }
    }

    /**
     * Prints out all recommended files to the terminal or command line
     */
    public void printRecommended(List<Movie> recommended) {
        System.out.println(recommended.size() + " Recommended Movies");
        for (Movie movie : recommended) {
            System.out.println(movie.toString());
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
                logger.error("Can't delete existing file " + fileName);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                logger.error("Can't create file " + fileName);
                return;
            }
        } catch (IOException e) {
            logger.error("Can't create file " + fileName, e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            JSONArray movies = new JSONArray();
            for (Movie movie : everyMovie) {
                movies.put(movie.toJSON());
            }
            outputStream.write(movies.toString().getBytes());
        } catch (FileNotFoundException e) {
            logger.error("Can't find file " + fileName, e);
        } catch (IOException e) {
            logger.error("Can't write to file " + fileName, e);
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
            logger.warn("Can't find json file '" + fileName + "'. Most likely first run.");
            return everyMovie;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder fullFile = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullFile.append(line);
            }

            logger.debug(fullFile.toString());

            JSONArray movies = new JSONArray(fullFile.toString());
            for (int i = 0; i < movies.length(); i++) {
                Movie movie = jsonToMovie(movies.getJSONObject(i));
                everyMovie.add(movie);
            }

            logger.debug("everyMovie.size():" + everyMovie.size());

        } catch (FileNotFoundException e) {
            logger.error("Can't find file " + fileName);
        } catch (IOException e) {
            logger.error("Can't write to file " + fileName);
        } catch (JSONException e) {
            logger.error("Error parsing JSON file " + fileName, e);
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
                logger.error("Can't delete existing file " + RECOMMENDED_MOVIES);
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                logger.error("Can't create file " + RECOMMENDED_MOVIES);
                return;
            }
        } catch (IOException e) {
            logger.error("Can't create file " + RECOMMENDED_MOVIES, e);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(RECOMMENDED_MOVIES)) {
            for (Movie movie : recommended) {
                String output = movie.toString() + System.lineSeparator();
                outputStream.write(output.getBytes());
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't find file " + RECOMMENDED_MOVIES, e);
        } catch (IOException e) {
            logger.error("Can't write to file " + RECOMMENDED_MOVIES, e);
        }
    }
}
