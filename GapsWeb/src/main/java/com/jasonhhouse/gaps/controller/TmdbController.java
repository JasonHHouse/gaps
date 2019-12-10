package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.Gaps;
import com.jasonhhouse.gaps.GapsSearch;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.service.IOService;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TmdbController {

    private final Logger logger = LoggerFactory.getLogger(TmdbController.class);

    private final GapsSearch gapsSearch;

    private final IOService ioService;

    @Autowired
    TmdbController(GapsSearch gapsSearch, IOService ioService) {
        this.gapsSearch = gapsSearch;
        this.ioService = ioService;
    }

    /**
     * Main REST call to start Gaps searching for missing movies
     *
     * @param gaps Needs the gaps object to get started with Plex information and The Movie DB key
     * @return All missing movies with their release year and collections associated to them
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @SuppressWarnings("unchecked")
    public void submit(@RequestBody Gaps gaps) {
        logger.info("submit()");

        ioService.migrateJsonSeedFileIfNeeded();

        //Error checking
        if (StringUtils.isEmpty(gaps.getMovieDbApiKey())) {
            String reason = "Missing Movie DB Api Key. This field is required for Gaps.";
            logger.error(reason);

            Exception e = new IllegalArgumentException();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        if (BooleanUtils.isNotTrue(gaps.getSearchFromPlex()) && BooleanUtils.isNotTrue(gaps.getSearchFromFolder())) {
            String reason = "Must search from Plex and/or folders. One or both of these fields is required for Gaps.";
            logger.error(reason);

            Exception e = new IllegalArgumentException();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        if (BooleanUtils.isNotFalse(gaps.getSearchFromPlex())) {
            if (CollectionUtils.isEmpty(gaps.getMovieUrls())) {
                String reason = "Missing Plex movie collection urls. This field is required to search from Plex.";
                logger.error(reason);

                Exception e = new IllegalArgumentException();
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
            } else {
                for (String url : gaps.getMovieUrls()) {
                    if (url == null) {
                        String reason = "Found null Plex movie collection url. This field is required to search from Plex.";
                        logger.error(reason);
                        Exception e = new IllegalArgumentException();
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
                    }
                }
            }
        }

        //Fill in default values if missing
        if (gaps.getWriteTimeout() == null) {
            logger.info("Missing write timeout. Setting default to 180 seconds.");
            gaps.setWriteTimeout(180);
        }

        if (gaps.getConnectTimeout() == null) {
            logger.info("Missing connect timeout. Setting default to 180 seconds.");
            gaps.setConnectTimeout(180);
        }

        if (gaps.getReadTimeout() == null) {
            logger.info("Missing read timeout. Setting default to 180 seconds.");
            gaps.setReadTimeout(180);
        }

        //populate read movies
        List<Movie> everyMovie = ioService.readMovieIdsFromFile();
        gapsSearch.run(gaps, everyMovie);

        if (gaps.getWriteToFile()) {
            ioService.writeToFile(gapsSearch.getRecommendedMovies());
        }

        //Always write to log
        ioService.printRecommended(gapsSearch.getRecommendedMovies());
        ioService.writeMovieIdsToFile(gapsSearch.getEveryMovie());
    }
}
