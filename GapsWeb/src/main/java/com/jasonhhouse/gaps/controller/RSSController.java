package com.jasonhhouse.gaps.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RSSController {

    @GetMapping(path = "/rss")
    public String rss() throws IOException {
        return new String(Files.readAllBytes(new File("rssFeed.json").toPath()));
    }

}