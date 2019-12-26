package com.jasonhhouse.gaps.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RecommendedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendedController.class);

    private final BindingErrorsService bindingErrorsService;
    private final IoService ioService;

    @Autowired
    public RecommendedController(BindingErrorsService bindingErrorsService, IoService ioService) {
        this.bindingErrorsService = bindingErrorsService;
        this.ioService = ioService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/recommended")
    public ModelAndView getRecommended() {
        LOGGER.info("getRecommended()");
        String recommended = null;
        if (ioService.doesRecommendedFileExist()) {
            recommended = ioService.getRecommendedMovies();
        }

        if (StringUtils.isEmpty(recommended)) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Movie[] recommendedMovies = objectMapper.readValue(recommended, Movie[].class);
                LOGGER.info("recommended.length:" + recommendedMovies.length);

                ModelAndView modelAndView = new ModelAndView("recommended");
                modelAndView.addObject("recommended", recommendedMovies);
                modelAndView.addObject("urls", buildUrls(recommendedMovies));
                return modelAndView;
            } catch (JsonProcessingException e) {
                LOGGER.error("Could not parse Recommended JSON", e);
                return bindingErrorsService.getErrorPage();
            }


        }
    }

    private List<String> buildUrls(Movie[] movies) {
        LOGGER.info("buildUrls( " + Arrays.toString(movies) + " ) ");
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
    }
}
