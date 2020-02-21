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

import com.jasonhhouse.gaps.PlexServer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlexServerValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexServerValidator.class);

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PlexServer.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {
        LOGGER.info("validate( " + obj + ", " + errors + " )");

        PlexServer plexSearch = (PlexServer) obj;
        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            errors.rejectValue("address", "address.empty");
        }

        if (plexSearch.getPort() > 65536) {
            errors.rejectValue("port", "port.aboveRange");
        }

        if (StringUtils.isEmpty(plexSearch.getPlexToken())) {
            errors.rejectValue("plexToken", "plexToken.empty");
        }
    }
}

