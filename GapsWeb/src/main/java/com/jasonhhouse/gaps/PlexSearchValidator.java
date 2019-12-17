package com.jasonhhouse.gaps;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlexSearchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PlexSearch.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PlexSearch plexSearch = (PlexSearch) obj;
        if (StringUtils.isEmpty(plexSearch.getMovieDbApiKey())) {
            errors.rejectValue("movieDbApiKey", "movieDbApiKey.empty");
        }
    }
}
