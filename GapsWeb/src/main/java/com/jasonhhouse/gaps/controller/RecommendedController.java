/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
