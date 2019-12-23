package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PlexSearchFormatter implements Formatter<PlexSearch> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexSearchFormatter.class);

    @NotNull
    @Override
    public PlexSearch parse(@NotNull String text, @NotNull Locale locale) throws ParseException {
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
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(plexSearch);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting room id and issue id to JSON", e);
            return "";
        }
    }
}
