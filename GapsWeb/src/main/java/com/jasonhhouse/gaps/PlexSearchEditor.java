package com.jasonhhouse.gaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.PropertyEditorSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlexSearchEditor extends PropertyEditorSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyEditorSupport.class);

    @Override
    public String getAsText() {
        LOGGER.debug("getAsText()");

        PlexSearch plexSearch = (PlexSearch) getValue();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(plexSearch);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting room id and issue id to JSON", e);
        }

        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        LOGGER.debug("setAsText( " + text + " )");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PlexSearch plexSearch = objectMapper.readValue(text, PlexSearch.class);
            setValue(plexSearch);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing player.", e);
            super.setAsText(text);
        }
    }
}
