package com.jasonhhouse.gaps.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.Rss;
import com.jasonhhouse.gaps.service.BindingErrorsService;
import com.jasonhhouse.gaps.service.IoService;
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
        String rss = null;
        if (ioService.doesRssFileExist()) {
            rss = ioService.getRssFile();
        }

        if (StringUtils.isEmpty(rss)) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Rss[] rssFeed = objectMapper.readValue(rss, Rss[].class);
                LOGGER.info("rssFeed.length:" + rssFeed.length);

                ModelAndView modelAndView = new ModelAndView("recommended");
                modelAndView.addObject("rss", rssFeed);
                return modelAndView;
            } catch (JsonProcessingException e) {
                LOGGER.error("Could not parse RSS JSON", e);
                return bindingErrorsService.getErrorPage();
            }


        }
    }

}
