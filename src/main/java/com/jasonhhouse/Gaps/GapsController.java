package com.jasonhhouse.Gaps;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GapsController {

    private final Logger logger = LoggerFactory.getLogger(GapsController.class);

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseEntity<?> submit(@RequestBody Gaps gaps) {
        logger.info("submit()");
        logger.info(gaps.toString());
        return new ResponseEntity<List<Movie>>(new ArrayList<>(), HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> test() {
        logger.info("test()");
        return new ResponseEntity<Gaps>(new Gaps(), HttpStatus.OK);
    }
}
