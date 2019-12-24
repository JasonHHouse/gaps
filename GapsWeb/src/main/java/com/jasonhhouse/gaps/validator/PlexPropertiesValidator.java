package com.jasonhhouse.gaps.validator;

import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.controller.GapsController;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlexPropertiesValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PlexSearch.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {
        LOGGER.info("validate( " + obj + ", " + errors + " )");

        PlexSearch plexSearch = (PlexSearch) obj;
        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            errors.rejectValue("address", "address.empty");
        }

        if (plexSearch.getPort() == null) {
            errors.rejectValue("port", "port.empty");
        }

        if (plexSearch.getPort() < 0) {
            errors.rejectValue("port", "port.belowRange");
        }

        if (plexSearch.getPort() > 65536) {
            errors.rejectValue("port", "port.aboveRange");
        }

        if (StringUtils.isEmpty(plexSearch.getPlexToken())) {
            errors.rejectValue("plexToken", "plexToken.empty");
        }
    }
}

