package com.jasonhhouse.gaps.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jasonhhouse.gaps.GapsService;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LibraryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final IoService ioService;
    private final GapsService gapsService;

    @Autowired
    public LibraryController(IoService ioService, GapsService gapsService) {
        this.ioService = ioService;
        this.gapsService = gapsService;

        if (CollectionUtils.isEmpty(gapsService.getPlexSearch().getPlexServers())) {
            //Only add if empty, otherwise the server information should be correct
            List<PlexServer> plexServers = ioService.readPlexConfiguration();
            gapsService.getPlexSearch().getPlexServers().addAll(plexServers);
        }
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/libraries")
    public ModelAndView getLibraries() {
        LOGGER.info("getLibraries()");
/*
        if (CollectionUtils.isEmpty(gapsService.getPlexSearch().getPlexServers())) {
            //Only add if empty, otherwise the server information should be correct
            List<PlexServer> plexServers = ioService.readPlexConfiguration();
            gapsService.getPlexSearch().getPlexServers().addAll(plexServers);
        }*/

        List<Movie> movies;
        PlexServer plexServer;
        PlexLibrary plexLibrary;
        if (CollectionUtils.isNotEmpty(gapsService.getPlexSearch().getPlexServers())) {
            //Read first plex servers movies
            plexServer = gapsService.getPlexSearch().getPlexServers().stream().findFirst().orElse(new PlexServer());
            plexLibrary = plexServer.getPlexLibraries().stream().findFirst().orElse(new PlexLibrary());
            movies = ioService.readOwnedMovies(plexServer.getMachineIdentifier(), plexLibrary.getKey());
        } else {
            plexServer = new PlexServer();
            plexLibrary = new PlexLibrary();
            movies = Collections.emptyList();
        }

        /*if (!ioService.doOwnedMoviesFilesExist(gapsService.getPlexSearch().getPlexServers())) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {*/
        //ToDo
        //Need to write a way to get all movies or break up the UI per library (Maybe just show the library the movie came from in the table)
        Map<String, PlexServer> plexServerMap = gapsService.getPlexSearch().getPlexServers().stream().collect(Collectors.toMap(PlexServer::getMachineIdentifier, Function.identity()));

        //LOGGER.info("ownedMovies.size():" + ownedMovies.size());

        if (CollectionUtils.isEmpty(movies)) {
            LOGGER.info("No owned movies found.");
        }

        if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
            try {
                PlexSearch plexSearch = ioService.readProperties();
                gapsService.updatePlexSearch(plexSearch);

                if (StringUtils.isEmpty(gapsService.getPlexSearch().getMovieDbApiKey())) {
                    LOGGER.warn("No owned movies found.");
                    //ToDo
                    //Can't search without key, show error message here
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to read gaps properties.", e);
            }
        }

        ModelAndView modelAndView = new ModelAndView("libraries");
        modelAndView.addObject("movies", movies);
        modelAndView.addObject("plexServers", plexServerMap);
        modelAndView.addObject("plexSearch", gapsService.getPlexSearch());
        modelAndView.addObject("plexServer", plexServer);
        modelAndView.addObject("plexLibrary", plexLibrary);
        return modelAndView;
        //}
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/libraries/{machineIdentifier}/{key}")
    @ResponseBody
    public ResponseEntity<String> getLibraries(@PathVariable("machineIdentifier") final String machineIdentifier, @PathVariable("key") final Integer key) {
        LOGGER.info("getLibraries( " + machineIdentifier + ", " + key + " )");

        List<Movie> movies = ioService
                .readOwnedMovies(machineIdentifier, key);
                /*.stream()
                .map(movie -> {
                    String[] output = new String[5];
                    output[0] = movie.getPosterUrl();
                    output[1] = movie.getName();
                    output[2] = String.valueOf(movie.getYear());
                    output[3] = movie.getLanguage();
                    output[4] = movie.getCollection();
                    return output;
                }).collect(Collectors.toList());*/

        ObjectNode objectNode = objectMapper.createObjectNode();

        if (movies == null) {
            objectNode.put("success", false);
            LOGGER.warn("Could not save PlexLibrary");
        } else {

            String output;
            try {
                output = objectMapper.writeValueAsString(movies);
                objectNode.put("success", true);
            } catch (JsonProcessingException e) {
                LOGGER.error("Failed to turn PlexLibrary Movies to JSON", e);
                objectNode.put("success", false);
                output = "";
            }
            objectNode.put("movies", output);
        }

        return ResponseEntity.ok().body(objectNode.toString());
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
/*

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        LOGGER.info("initBinder()");
        binder.addCustomFormatter(new PlexServersFormatter());
    }*/
}
