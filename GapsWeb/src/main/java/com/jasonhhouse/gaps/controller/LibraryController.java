package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LibraryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);

    private final BindingErrorsService bindingErrorsService;
    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public LibraryController(BindingErrorsService bindingErrorsService, IoService ioService, GapsService gapsService) {
        this.bindingErrorsService = bindingErrorsService;
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/libraries")
    public ModelAndView getLibraries() {
        LOGGER.info("getLibraries()");

        List<PlexServer> plexServers = ioService.readPlexConfiguration();
        gapsService.getPlexSearch().getPlexServers().addAll(plexServers);

        if (!ioService.doOwnedMoviesFilesExist(gapsService.getPlexSearch().getPlexServers())) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {
            //ToDo
            //Need to write a way to get all movies or break up the UI per library (Maybe just show the library the movie came from in the table)
            Map<PlexLibrary, List<Movie>> ownedMovies = ioService.readAllOwnedMovies(gapsService.getPlexSearch().getPlexServers());
            Map<String, PlexServer> plexServerMap = gapsService.getPlexSearch().getPlexServers().stream().collect(Collectors.toMap(PlexServer::getMachineIdentifier, Function.identity()));

            //LOGGER.info("ownedMovies.size():" + ownedMovies.size());

            if (MapUtils.isEmpty(ownedMovies)) {
                LOGGER.error("Could not parse Owned Movie JSON");
                return bindingErrorsService.getErrorPage();
            }

            try {
                PlexSearch plexSearch = ioService.readProperties();
                gapsService.updatePlexSearch(plexSearch);
            } catch (IOException e) {
                LOGGER.warn("Failed to read gaps properties.", e);
            }

            ModelAndView modelAndView = new ModelAndView("libraries");
            modelAndView.addObject("movies", ownedMovies);
            modelAndView.addObject("plexServers", plexServerMap);
            modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
            return modelAndView;
        }
    }

  /*  private List<String> buildUrls(List<Movie> movies) {
        LOGGER.info("buildUrls( " + movies + " ) ");
        List<String> urls = new ArrayList<>();
        for (Movie movie : movies) {
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
    }*/
}
