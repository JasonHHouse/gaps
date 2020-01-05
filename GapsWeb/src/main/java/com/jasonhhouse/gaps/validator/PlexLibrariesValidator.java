/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

        PlexSearch plexSearch = (PlexSearch) obj;
        if (CollectionUtils.isEmpty(plexSearch.getLibraries())) {
            errors.rejectValue("libraries", "libraries.empty");
            return;
        }

        for (PlexLibrary plexLibrary : plexSearch.getLibraries()) {
            if (StringUtils.isEmpty(plexLibrary.getTitle())) {
                errors.rejectValue("libraries", "plexLibrary.getTitle().empty");
            }

            if (plexLibrary.getKey() == null) {
                errors.rejectValue("libraries", "plexLibrary.getKey().empty");
            }

            if (plexLibrary.getSelected() == null) {
                plexLibrary.setSelected(false);
            }
        }

    }
}

