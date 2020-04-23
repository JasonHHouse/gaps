package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Pair;
import com.jasonhhouse.gaps.UrlGenerator;
import com.jasonhhouse.plex.MediaContainer;
import com.jasonhhouse.plex.Video;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MissLabeled {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissLabeled.class);

    private final UrlGenerator urlGenerator;

    @Autowired
    public MissLabeled(@Qualifier("real") UrlGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    public @NotNull MediaContainer findAllPlexVideos(@NotNull String url) {
        LOGGER.info("findAllPlexVideos()");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        if (StringUtils.isEmpty(url)) {
            LOGGER.info("No URL added to findAllPlexVideos().");
            return new MediaContainer();
        }

        MediaContainer mediaContainer;
        try {
            HttpUrl httpUrl = urlGenerator.generatePlexUrl(url);

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
                    String reason = "Body returned empty from Plex";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
                }

                InputStream inputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                JAXBContext jaxbContext = JAXBContext.newInstance(MediaContainer.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                mediaContainer = (MediaContainer) jaxbUnmarshaller.unmarshal(inputStream);

            } catch (IOException e) {
                String reason = "Error connecting to Plex to get Movie list: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (JAXBException e) {
                String reason = "Error parsing XML from Plex: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            String reason = "Error with plex Url: " + url;
            LOGGER.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        return mediaContainer;
    }

    public List<Pair<String, Double>> findMatchPercentage(MediaContainer mediaContainer) {
        List<Pair<String, Double>> pairs = new ArrayList<>();
        for (Video video : mediaContainer.getVideos()) {
            String file = video.getMedia().get(0).getParts().get(0).getFile();
            if (file.contains("(")) {
                file = file.substring(0, file.lastIndexOf('(') - 1);
            } else {
                file = file.substring(0, file.lastIndexOf('.') - 1);
            }
            file = file.substring(file.lastIndexOf('/') + 1);
            String title = video.getTitle();
            title = title.replaceAll(":", "");
            LOGGER.info("title:" + title);
            LOGGER.info("file:" + file);
            Double matchPercentage = similarity(title, file);
            if (matchPercentage < .75) {
                Pair<String, Double> pair = new Pair<>(title, matchPercentage);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private Double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength;
    }

}
