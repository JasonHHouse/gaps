package com.jasonhhouse.gaps.validator;

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.controller.GapsController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlexLibrariesValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PlexSearch.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {
        LOGGER.info("validate( " + obj + ", " + errors + " )");

        int position = 0;

        PlexSearch plexSearch = (PlexSearch) obj;
        if (CollectionUtils.isEmpty(plexSearch.getLibraries())) {
            errors.rejectValue("libraries", "libraries.empty");
            return;
        }

        for (PlexLibrary plexLibrary : plexSearch.getLibraries()) {
            if (StringUtils.isNotEmpty(plexLibrary.getTitle())) {
                errors.rejectValue("plexLibraries[" + position + "].getTitle()", "plexLibrary.getTitle().empty");
            }

            if (plexLibrary.getKey() == null) {
                errors.rejectValue("plexLibraries[" + position + "].getKey()", "plexLibrary.getKey().empty");
            }

            if (plexLibrary.getSelected() == null) {
                errors.rejectValue("plexLibraries[" + position + "].selected()", "plexLibrary.selected().empty");
            }

            position++;
        }
    }
}

