package com.jasonhhouse.gaps.validator;

import com.google.common.net.InetAddresses;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.controller.GapsController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlexLibrariesValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GapsController.class);

    private int position;

    @Override
    public boolean supports(Class<?> clazz) {
        return PlexSearch.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        LOGGER.info("validate( " + obj + ", " + errors + " )");

        position = 0;

        PlexSearch plexSearch = (PlexSearch) obj;
        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            errors.rejectValue("movieDbApiKey", "movieDbApiKey.empty");
        }

        if (StringUtils.isEmpty(plexSearch.getAddress())) {
            errors.rejectValue("address", "address.empty");
        }

        if (InetAddresses.isInetAddress(plexSearch.getAddress())) {
            errors.rejectValue("address", "address.notValidAddress");
        }

        if (plexSearch.getPort() == null) {
            errors.rejectValue("port", "port.empty");
        }

        if (StringUtils.isEmpty(plexSearch.getPlexToken())) {
            errors.rejectValue("plexToken", "plexToken.empty");
        }

        if(CollectionUtils.isEmpty(plexSearch.getLibraries())) {
            errors.rejectValue("libraries", "libraries.empty");
            return;
        }

        plexSearch.getLibraries().forEach(plexLibrary -> {
            if(StringUtils.isNotEmpty(plexLibrary.getTitle())) {
                errors.rejectValue("plexLibrary.getTitle()", "plexLibraries[" + position + "].getTitle().empty");
            }

            if(plexLibrary.getKey() == null) {
                errors.rejectValue("plexLibrary.getKey()", "plexLibraries[" + position + "].getKey().empty");
            }

            if(plexLibrary.getSelected() == null) {
                errors.rejectValue("plexLibrary.selected()", "plexLibraries[" + position + "].selected().empty");
            }

            position++;
        });
    }
}

