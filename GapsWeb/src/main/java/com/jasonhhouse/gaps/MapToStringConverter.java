package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MapToStringConverter implements Converter<Map<?, ?>, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapToStringConverter.class);

    @Override
    public String convert(@NotNull Map<?, ?> map) {
        LOGGER.info("print( " + map + " )");
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting plex servers map", e);
            return "";
        }
    }
}
