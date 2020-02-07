package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlexServerToStringConverter implements Converter<PlexServer, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexServerToStringConverter.class);

    @Override
    public String convert(@NotNull PlexServer plexServer) {
        LOGGER.info("print( " + plexServer + " )");
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(plexServer);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting plex server", e);
            return "";
        }
    }
}
