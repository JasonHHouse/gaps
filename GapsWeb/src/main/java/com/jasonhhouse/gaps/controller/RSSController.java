package com.jasonhhouse.gaps.controller;

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
public class RSSController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSSController.class);

    private final IoService ioService;

    @Autowired
    public RSSController(IoService ioService) {
        this.ioService = ioService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/rss")
    public ModelAndView getRss() {
        LOGGER.info("getRss()");
        String rss = null;
        if (ioService.doesRssFileExist()) {
            rss = ioService.getRssFile();
        }

        if (StringUtils.isEmpty(rss)) {
            //Show empty page
            return new ModelAndView("emptyState");
        } else {
            ModelAndView modelAndView = new ModelAndView("rss");
            modelAndView.addObject("rss", rss);
            return modelAndView;
        }
    }

}