/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

public class PlexSearchFormatter implements Formatter<PlexSearch> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexSearchFormatter.class);

    @NotNull
    @Override
    public PlexSearch parse(@NotNull String text, @NotNull Locale locale) throws ParseException {
        LOGGER.info("parse( " + text + ", " + locale + " )");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(text, PlexSearch.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing player.", e);
            throw new ParseException("Error parsing player.", 0);
        }
    }

    @NotNull
    @Override
    public String print(@NotNull PlexSearch plexSearch, @NotNull Locale locale) {
        LOGGER.info("print( " + plexSearch + ", " + locale + " )");
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(plexSearch);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting room id and issue id to JSON", e);
            return "";
        }
    }
}
