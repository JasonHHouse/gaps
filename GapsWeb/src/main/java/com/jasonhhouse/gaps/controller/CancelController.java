package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.GapsSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "cancelSearch")
public class CancelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelController.class);

    private final GapsSearch gapsSearch;

    @Autowired
    CancelController(GapsSearch gapsSearch) {
        this.gapsSearch = gapsSearch;
    }

    /**
     * Forces a stop to searching. Used commonly if navigated away from page.
     *
     * @return success of search canceled
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> cancelSearch() {
        LOGGER.info("cancelSearch()");
        gapsSearch.cancelSearch();
        return new ResponseEntity<>("Canceled Search", HttpStatus.OK);
    }
}
