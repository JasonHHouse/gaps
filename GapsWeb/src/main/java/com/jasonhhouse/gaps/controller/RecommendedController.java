package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.service.IoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RecommendedController {

    private final IoService ioService;

    @Autowired
    public RecommendedController(IoService ioService) {
        this.ioService = ioService;
    }

    @GetMapping(path = "/recommended")
    public ModelAndView rss() {
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
