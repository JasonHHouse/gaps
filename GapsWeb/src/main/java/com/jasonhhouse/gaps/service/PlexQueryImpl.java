/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.Pair;
import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.UrlGenerator;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.plex.libs.MediaContainer;
import com.jasonhhouse.plex.libs.PlexLibrary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class PlexQueryImpl implements PlexQuery {

    public static final String ID_IDX_START = "://";
    public static final String ID_IDX_END = "?";
    private static final long TIMEOUT = 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexQueryImpl.class);

    private final UrlGenerator urlGenerator;

    @Autowired
    public PlexQueryImpl(@Qualifier("real") UrlGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    @Override
    public @NotNull Payload getLibraries(@NotNull PlexServer plexServer) {
        LOGGER.info("queryPlexLibraries()");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addPathSegment("library")
                .addPathSegment("sections")
                .addQueryParameter("X-Plex-Token", plexServer.getPlexToken())
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
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
                MediaContainer mediaContainer = (MediaContainer) jaxbUnmarshaller.unmarshal(inputStream);

                //Remove everything except movie folders
                List<PlexLibrary> plexLibraries = mediaContainer.getPlexLibraries().stream().filter(plexLibrary -> plexLibrary.getType().equalsIgnoreCase("movie")).collect(Collectors.toList());

                LOGGER.info("{} Plex libraries found", plexLibraries.size());
                plexServer.setPlexLibraries(plexLibraries);
                return Payload.PLEX_LIBRARIES_FOUND.setExtras("size():" + plexLibraries.size());
            } catch (IOException e) {
                String reason = String.format("Error connecting to Plex to get library list: %s", url);
                LOGGER.error(reason, e);
                return Payload.PLEX_CONNECTION_FAILED.setExtras("url:" + url);
            } catch (JAXBException e) {
                String reason = "Error parsing XML from Plex: " + url;
                LOGGER.error(reason, e);
                return Payload.PARSING_PLEX_FAILED.setExtras("url:" + url);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with plex Url: " + url;
            LOGGER.error(reason, e);
            return Payload.PLEX_URL_ERROR.setExtras("url:" + url);
        }

    }

    @Override
    public @NotNull Payload queryPlexServer(@NotNull PlexServer plexServer) throws ResponseStatusException {
        LOGGER.info("queryPlexLibraries( {}} )", plexServer);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addQueryParameter("X-Plex-Token", plexServer.getPlexToken())
                .build();

        //ToDo
        //Need to control time out here, using gaps object
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                NodeList dtmNodeList = parseXml(response, url, "/MediaContainer");
                Node node = dtmNodeList.item(0);

                if (node == null) {
                    String reason = "No Plex server found at url: " + url;
                    LOGGER.error(reason);
                    throw new IllegalStateException(reason);
                }

                NamedNodeMap map = node.getAttributes();
                Node friendlyNameNode = map.getNamedItem("friendlyName");
                if (friendlyNameNode == null) {
                    String reason = "Error finding 'friendlyName' inside /";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                }

                String friendlyName = friendlyNameNode.getNodeValue().trim();
                LOGGER.info("friendlyName:{}", friendlyName);

                Node machineIdentifierNode = map.getNamedItem("machineIdentifier");
                if (machineIdentifierNode == null) {
                    String reason = "Error finding 'machineIdentifier' inside /";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                }

                String machineIdentifier = machineIdentifierNode.getNodeValue().trim();
                LOGGER.info("machineIdentifier:{}", machineIdentifier);

                plexServer.setFriendlyName(friendlyName);
                plexServer.setMachineIdentifier(machineIdentifier);

                return Payload.PLEX_CONNECTION_SUCCEEDED.setExtras("url:" + url);
            } catch (IOException e) {
                String reason = String.format("Error connecting to Plex to get library list: %s", url);
                LOGGER.error(reason, e);
                return Payload.PLEX_CONNECTION_FAILED.setExtras("url:" + url);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = String.format("Error parsing XML from Plex: %s", url);
                LOGGER.error(reason, e);
                return Payload.PARSING_PLEX_FAILED.setExtras("url:" + url);
            }
        } catch (IllegalArgumentException e) {
            String reason = String.format("Error with plex Url: %s", url);
            LOGGER.error(reason, e);
            return Payload.PLEX_URL_ERROR.setExtras("url:" + url);
        }
    }

    @Override
    public void findAllMovieIds(@NotNull List<BasicMovie> basicMovies, @NotNull PlexServer plexServer, @NotNull PlexLibrary plexLibrary) {
        LOGGER.info("findAllMovieIds( {}, {} )", plexServer, plexLibrary);

        if (plexLibrary.getScanner().equals("Plex Movie Scanner")) {
            LOGGER.info("PlexLibrary {} uses old scanner", plexLibrary.getTitle());
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        for (BasicMovie basicMovie : basicMovies) {
            if (basicMovie.getRatingKey() == -1) {
                LOGGER.info("No key found for the movie {}", basicMovie.getName());
                continue;
            }

            HttpUrl httpUrl = urlGenerator.generatePlexMetadataUrl(plexServer, plexLibrary, basicMovie.getRatingKey());

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
                    LOGGER.error("Body returned empty from Plex for the movie {}", basicMovie.getName());
                    continue;
                }

                InputStream fileIS = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                DocumentBuilder builder = getDocumentBuilder();
                Document xmlDocument = builder.parse(fileIS);
                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/MediaContainer/Video/Guid";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

                if (nodeList.getLength() == 0) {
                    LOGGER.warn("No guids found in url: {}", httpUrl);
                    continue;
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    Node nodeTitle = node.getAttributes().getNamedItem("id");

                    if (nodeTitle == null) {
                        LOGGER.error("Missing id from Guid element in Plex");
                        continue;
                    }

                    //Files can't have : so need to remove to find matches correctly
                    String urlId = nodeTitle.getNodeValue();
                    String id = urlId.replaceAll("[A-Za-z]+://", "");
                    if (urlId.contains("imdb")) {
                        basicMovie.setImdbId(id);
                    } else if (urlId.contains("tmdb")) {
                        basicMovie.setTmdbId(Integer.parseInt(id));
                    } else {
                        LOGGER.warn("Can't find ID to match {}", urlId);
                    }
                }

            } catch (IOException e) {
                String reason = String.format("Error connecting to Plex to get Movie list: %s", httpUrl);
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = String.format("Error parsing XML from Plex: %s", httpUrl);
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }

        }

    }

    @Override
    public @NotNull PlexServer getPlexServerFromMachineIdentifier(@NotNull PlexProperties plexProperties, @NotNull String machineIdentifier) throws IllegalArgumentException {
        LOGGER.info("generatePlexUrl( {} )", machineIdentifier);
        for (PlexServer plexServer : plexProperties.getPlexServers()) {
            if (plexServer.getMachineIdentifier().equals(machineIdentifier)) {
                return plexServer;
            }
        }

        throw new IllegalArgumentException(String.format("No PlexServer matching machineIdentifier %s found", machineIdentifier));
    }

    @Override
    public @NotNull PlexLibrary getPlexLibraryFromKey(@NotNull PlexServer plexServer, @NotNull Integer key) throws IllegalArgumentException {
        for (PlexLibrary plexLibrary : plexServer.getPlexLibraries()) {
            if (plexLibrary.getKey().equals(key)) {
                return plexLibrary;
            }
        }

        throw new IllegalArgumentException(String.format("No PlexLibrary matching key %s found", key));
    }

    @Override
    public @NotNull List<BasicMovie> findAllPlexMovies(@NotNull Map<Pair<String, Integer>, BasicMovie> previousMovies, @NotNull HttpUrl url) {
        LOGGER.info("findAllPlexMovies()");

        List<BasicMovie> ownedBasicMovies = new ArrayList<>();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body() != null ? response.body().string() : null;

                if (StringUtils.isBlank(body)) {
                    String reason = "Body returned empty from Plex";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
                }

                InputStream fileIS = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                DocumentBuilder builder = getDocumentBuilder();
                Document xmlDocument = builder.parse(fileIS);
                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/MediaContainer/Video";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

                if (nodeList.getLength() == 0) {
                    LOGGER.warn("No movies found in url: {}", url);
                    return ownedBasicMovies;
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    Node nodeTitle = node.getAttributes().getNamedItem("title");

                    if (nodeTitle == null) {
                        String reason = "Missing title from Video element in Plex";
                        LOGGER.error(reason);
                        throw new NullPointerException(reason);
                    }

                    //Files can't have : so need to remove to find matches correctly
                    String title = nodeTitle.getNodeValue().replaceAll(":", "");
                    if (node.getAttributes().getNamedItem("year") == null) {
                        LOGGER.warn("Year not found for {}", title);
                        continue;
                    }
                    int year = Integer.parseInt(node.getAttributes().getNamedItem("year").getNodeValue());

                    Integer tmdbId = -1;
                    String imdbId = "";
                    if (node.getAttributes().getNamedItem("guid") != null) {
                        String guid = node.getAttributes().getNamedItem("guid").getNodeValue();
                        if (guid.contains("com.plexapp.agents.themoviedb")) {
                            guid = guid.replaceAll("[A-Za-z\\.]+://", "");
                            tmdbId = Integer.valueOf(guid.substring(0, guid.indexOf('?')));
                        } else if (guid.contains("com.plexapp.agents.imdb")) {
                            guid = guid.replaceAll("[A-Za-z\\.]+://", "");
                            imdbId = guid.substring(0, guid.indexOf('?'));
                        }
                    }

                    String thumbnail = "";
                    if (node.getAttributes().getNamedItem("thumb") != null) {
                        thumbnail = node.getAttributes().getNamedItem("thumb").getNodeValue();
                    }

                    String summary = "";
                    if (node.getAttributes().getNamedItem("summary") != null) {
                        summary = node.getAttributes().getNamedItem("summary").getNodeValue();
                    }

                    String key = "";
                    if (node.getAttributes().getNamedItem("key") != null) {
                        key = node.getAttributes().getNamedItem("key").getNodeValue();
                    }

                    Integer ratingKey = -1;
                    if (node.getAttributes().getNamedItem("ratingKey") != null) {
                        ratingKey = Integer.valueOf(node.getAttributes().getNamedItem("ratingKey").getNodeValue());
                    }

                    List<String> genres = new ArrayList<>();
                    if (node.hasChildNodes()) {
                        for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                            Node childNode = node.getChildNodes().item(j);
                            if (childNode.getNodeName().equalsIgnoreCase("genre")) {
                                genres.add(childNode.getAttributes().getNamedItem("tag").getNodeValue());
                            }
                        }
                    }

                    BasicMovie basicMovie = getOrCreateOwnedMovie(previousMovies, title, year, tmdbId, imdbId, thumbnail, summary, ratingKey, key, genres);
                    ownedBasicMovies.add(basicMovie);
                }
                LOGGER.info("{} movies found in plex", ownedBasicMovies.size());

            } catch (IOException e) {
                String reason = String.format("Error connecting to Plex to get Movie list: %s", url);
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = String.format("Error parsing XML from Plex: %s", url);
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            String reason = String.format("Error with plex Url: %s", url);
            LOGGER.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        return ownedBasicMovies;
    }

    private BasicMovie getOrCreateOwnedMovie(Map<Pair<String, Integer>, BasicMovie> previousMovies, @NotNull String title, int year,
                                             @NotNull Integer tmdbId, @NotNull String imdbId, @NotNull String thumbnail,
                                             @NotNull String summary, @NotNull Integer ratingKey, @NotNull String key,
                                             @NotNull List<String> genres) {
        Pair<String, Integer> moviePair = new Pair<>(title, year);
        if (previousMovies.containsKey(moviePair)) {
            BasicMovie previousBasicMovie = previousMovies.get(moviePair);
            return new BasicMovie.Builder(title, year)
                    .setPosterUrl(thumbnail)
                    .setOverview(summary)
                    .setKey(key)
                    .setRatingKey(ratingKey)
                    .setImdbId(previousBasicMovie.getImdbId())
                    .setCollectionTitle(previousBasicMovie.getCollectionTitle())
                    .setLanguage(previousBasicMovie.getLanguage())
                    .setTmdbId(previousBasicMovie.getTmdbId())
                    .setCollectionId(previousBasicMovie.getCollectionId())
                    .setGenres(genres)
                    .build();
        } else {
            return new BasicMovie.Builder(title, year)
                    .setPosterUrl(thumbnail)
                    .setOverview(summary)
                    .setKey(key)
                    .setRatingKey(ratingKey)
                    .setTmdbId(tmdbId)
                    .setImdbId(imdbId)
                    .setGenres(genres)
                    .build();
        }
    }

    private <T> T parseXml(@NotNull Response response, @NotNull HttpUrl url, @NotNull String expression) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            LOGGER.warn("Empty response body");
            throw new IOException("Empty response body");
        }
        String body = responseBody.string();

        if (StringUtils.isBlank(body)) {
            String reason = String.format("Body returned null from Plex. Url: %s", url);
            LOGGER.error(reason);
            throw new IllegalStateException(reason);
        }

        InputStream fileIS = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        DocumentBuilder builder = getDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (T) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        builderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return builderFactory.newDocumentBuilder();
    }

}
