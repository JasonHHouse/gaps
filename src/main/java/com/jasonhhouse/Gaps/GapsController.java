package com.jasonhhouse.Gaps;

import java.util.Set;
import java.util.concurrent.Future;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class GapsController {

    private final Logger logger = LoggerFactory.getLogger(GapsController.class);

    private final GapsSearch gapsSearch;

    @Autowired
    GapsController(GapsSearch gapsSearch) {
        this.gapsSearch = gapsSearch;
    }

    @RequestMapping(value = "cancelSearch", method = RequestMethod.PUT)
    public ResponseEntity<String> cancelSearch() {
        logger.info("cancelSearch()");
        gapsSearch.cancelSearch();
        return new ResponseEntity<>("Canceled Search", HttpStatus.OK);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public Future<ResponseEntity<Set<Movie>>> submit(@RequestBody Gaps gaps) {
        logger.info("submit()");

        //Error checking
        if (StringUtils.isEmpty(gaps.getMovieDbApiKey())) {
            String reason = "Missing Movie DB Api Key. This field is required for Gaps.";
            logger.error(reason);

            Exception e = new IllegalArgumentException();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        if (BooleanUtils.isNotTrue(gaps.getSearchFromPlex()) && BooleanUtils.isNotTrue(gaps.getSearchFromFolder())) {
            String reason = "Must search from Plex and/or folders. One or both of these fields is required for Gaps.";
            logger.error(reason);

            Exception e = new IllegalArgumentException();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        if (BooleanUtils.isNotFalse(gaps.getSearchFromPlex())) {
            if (CollectionUtils.isEmpty(gaps.getMovieUrls())) {
                String reason = "Missing Plex movie collection urls. This field is required to search from Plex.";
                logger.error(reason);

                Exception e = new IllegalArgumentException();
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
            } else {
                for (String url : gaps.getMovieUrls()) {
                    if (StringUtils.isEmpty(url)) {
                        String reason = "Found empty Plex movie collection url. This field is required to search from Plex.";
                        logger.error(reason);

                        Exception e = new IllegalArgumentException();
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
                    }
                }
            }
        }

        //Fill in default values if missing
        if (gaps.getWriteTimeout() == null) {
            logger.info("Missing write timeout. Setting default to 180 seconds.");
            gaps.setWriteTimeout(180);
        }

        if (gaps.getConnectTimeout() == null) {
            logger.info("Missing connect timeout. Setting default to 180 seconds.");
            gaps.setConnectTimeout(180);
        }

        if (gaps.getReadTimeout() == null) {
            logger.info("Missing read timeout. Setting default to 180 seconds.");
            gaps.setReadTimeout(180);
        }


        return runInOtherThread(gaps);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> fetchStatus() {
        logger.info("fetchStatus()");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("searchedMovieCount", gapsSearch.getSearchedMovieCount());
        jsonObject.put("totalMovieCount", gapsSearch.getTotalMovieCount());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    private Future<ResponseEntity<Set<Movie>>> runInOtherThread(Gaps gaps) {
        Properties properties = new Properties();
        properties.setMovieDbApiKey(gaps.getMovieDbApiKey());
        properties.setWriteToFile(false);

        PlexProperties plexProperties = new PlexProperties();
        plexProperties.setReadTimeout(gaps.getReadTimeout());
        plexProperties.setConnectTimeout(gaps.getConnectTimeout());
        plexProperties.setWriteTimeout(gaps.getWriteTimeout());
        plexProperties.setMovieUrls(gaps.getMovieUrls());
        plexProperties.setSearchFromPlex(true);
        properties.setPlex(plexProperties);

        FolderProperties folderProperties = new FolderProperties();
        folderProperties.setSearchFromFolder(false);
        properties.setFolder(folderProperties);

        return gapsSearch.run(properties);
    }

}
