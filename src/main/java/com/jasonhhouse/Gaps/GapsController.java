package com.jasonhhouse.Gaps;

import java.util.Set;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class GapsController {

    private final Logger logger = LoggerFactory.getLogger(GapsController.class);


    private final GapsSearch gapsSearch;

    @Autowired
    GapsController(GapsSearch gapsSearch) {
        this.gapsSearch = gapsSearch;
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public DeferredResult<ResponseEntity<Set<Movie>>> submit(@RequestBody Gaps gaps) {
        logger.info("submit()");

        final DeferredResult<ResponseEntity<Set<Movie>>> deferredResult = new DeferredResult<>();
        runInOtherThread(gaps, deferredResult);
        return deferredResult;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> fetchStatus() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("searchedMovieCount", gapsSearch.getSearchedMovieCount());
        jsonObject.put("totalMovieCount", gapsSearch.getTotalMovieCount());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
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

        Set<Movie> recommendMovies = gapsSearch.run(properties);

        ResponseEntity<Set<Movie>> responseEntity = new ResponseEntity<>(recommendMovies, HttpStatus.OK);
        deferredResult.setResult(responseEntity);
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> test() {
        logger.info("test()");
        return new ResponseEntity<Gaps>(new Gaps(), HttpStatus.OK);
    }
}
