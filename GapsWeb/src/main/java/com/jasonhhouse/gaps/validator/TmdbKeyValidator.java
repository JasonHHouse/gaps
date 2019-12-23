package com.jasonhhouse.gaps.validator;

import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.controller.GapsController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TmdbKeyValidator implements Validator {


    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return PlexSearch.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        LOGGER.info("validate( " + obj + ", " + errors + " )");

        PlexSearch plexSearch = (PlexSearch) obj;
        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            errors.rejectValue("movieDbApiKey", "movieDbApiKey.empty");
        }
    }
}
