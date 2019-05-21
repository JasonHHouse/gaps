package com.jasonhhouse.Gaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class GapsController {

    private final Logger logger = LoggerFactory.getLogger(GapsController.class);

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public DeferredResult<ResponseEntity<Set<Movie>>> submit(@RequestBody Gaps gaps) {
        logger.info("submit()");

        final DeferredResult<ResponseEntity<Set<Movie>>> deferredResult = new DeferredResult<>();
        runInOtherThread(gaps, deferredResult);
        return deferredResult;
    }

    private void runInOtherThread(Gaps gaps, DeferredResult<ResponseEntity<Set<Movie>>> deferredResult) {
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

        GapsSearch gapsSearch = new GapsSearch(properties);
        Set<Movie> recommendMovies = gapsSearch.run();

        ResponseEntity<Set<Movie>> responseEntity = new ResponseEntity<>(recommendMovies, HttpStatus.OK);
        deferredResult.setResult(responseEntity);
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> test() {
        logger.info("test()");
        return new ResponseEntity<Gaps>(new Gaps(), HttpStatus.OK);
    }
}
