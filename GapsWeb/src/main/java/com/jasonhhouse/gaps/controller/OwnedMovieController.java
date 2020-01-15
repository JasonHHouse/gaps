package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.OwnedMovie;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OwnedMovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnedMovieController.class);

    private final BindingErrorsService bindingErrorsService;
    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public OwnedMovieController(BindingErrorsService bindingErrorsService, IoService ioService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/ownedMovies")
    public ModelAndView getOwnedMovies() {
        LOGGER.info("getOwnedMovies()");

        if (!ioService.doesOwnedMoviesFileExist()) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {
            List<OwnedMovie> ownedMovies = ioService.getOwnedMovies();
            LOGGER.info("ownedMovies.size():" + ownedMovies.size());

            if (CollectionUtils.isEmpty(ownedMovies)) {
                LOGGER.error("Could not parse Owned Movie JSON");
                return bindingErrorsService.getErrorPage();
            }

            try {
                PlexSearch plexSearch = ioService.readProperties();
                gapsService.updatePlexSearch(plexSearch);
            } catch (IOException e) {
                LOGGER.warn("Failed to read gaps properties.", e);
            }

            ModelAndView modelAndView = new ModelAndView("ownedMovies");
            modelAndView.addObject("ownedMovies", ownedMovies);
            modelAndView.addObject("urls", buildUrls(ownedMovies));
            modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
            return modelAndView;
        }
    }

    private List<String> buildUrls(List<OwnedMovie> movies) {
        LOGGER.info("buildUrls( " + movies + " ) ");
        List<String> urls = new ArrayList<>();
        for (OwnedMovie movie : movies) {
            if (movie.getTvdbId() != -1) {
                urls.add("https://www.themoviedb.org/movie/" + movie.getTvdbId());
                continue;
            }

            if (StringUtils.isNotEmpty(movie.getImdbId())) {
                urls.add("https://www.imdb.com/title/" + movie.getImdbId() + "/");
                continue;
            }

            urls.add(null);
        }

        return urls;
    }
}
