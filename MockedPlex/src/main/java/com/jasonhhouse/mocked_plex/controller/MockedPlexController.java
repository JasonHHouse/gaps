/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.mocked_plex.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MockedPlexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockedPlexController.class);

    @GetMapping(path = "/",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getPlex(@RequestParam(value = "X-Plex-Token", required = false) final String xPlexToken) {
        LOGGER.info("getPlex( {} )", xPlexToken);
        return ResponseEntity.ok(getPlexXmlFile("plex.xml"));
    }

    @GetMapping(path = "/library/sections",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getPlexLibrarySections(@RequestParam(value = "X-Plex-Token", required = false) final String xPlexToken) {
        LOGGER.info("getPlex( {} )", xPlexToken);
        return ResponseEntity.ok(getPlexXmlFile("sections.xml"));
    }

    @GetMapping(path = "/library/sections/5/all",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getSaw(@RequestParam(value = "X-Plex-Token", required = false) final String xPlexToken) {
        LOGGER.info("getPlex( {} )", xPlexToken);
        return ResponseEntity.ok(getPlexXmlFile("saw.xml"));
    }

    @GetMapping(path = "/library/sections/4/all",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getBestMovies(@RequestParam(value = "X-Plex-Token", required = false) final String xPlexToken) {
        LOGGER.info("getPlex( {} )", xPlexToken);
        return ResponseEntity.ok(getPlexXmlFile("bestMovies.xml"));
    }

    @GetMapping(path = "/library/sections/6/all",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getNewMetadata(@RequestParam(value = "X-Plex-Token", required = false) final String xPlexToken) {
        LOGGER.info("getPlex( {} )", xPlexToken);
        return ResponseEntity.ok(getPlexXmlFile("newMetadata.xml"));
    }

    private @NotNull String getPlexXmlFile(@NotNull String fileName) {
        final File file;
        try {
            file = ResourceUtils.getFile("classpath:" + fileName);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                StringBuilder fullFile = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    fullFile.append(line);
                }

                return fullFile.toString();
            } catch (IOException e) {
                LOGGER.error(String.format("Failed to read file %s", fileName), e);
                return "";
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("Failed to find file %s", fileName), e);
            return "";
        }
    }
}
