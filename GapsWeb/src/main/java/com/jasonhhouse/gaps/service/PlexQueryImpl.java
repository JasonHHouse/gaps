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

import com.jasonhhouse.gaps.Movie;
import com.jasonhhouse.gaps.MoviePair;
import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.UrlGenerator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexQueryImpl.class);

    private final UrlGenerator urlGenerator;

    @Autowired
    public PlexQueryImpl(@Qualifier("real") UrlGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    @Override
    public void getLibraries(@NotNull PlexServer plexServer) {
        LOGGER.info("queryPlexLibraries()");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addPathSegment("library")
                .addPathSegment("sections")
                .addQueryParameter("X-Plex-Token", plexServer.getPlexToken())
                .build();

        //ToDo
        //Need to control time out here, using gaps object
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        List<PlexLibrary> plexLibraries = new ArrayList<>();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                NodeList nodeList = parseXml(response, url, "/MediaContainer/Directory");

                if (nodeList.getLength() == 0) {
                    String reason = "No libraries found in url: " + url;
                    LOGGER.warn(reason);
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    NamedNodeMap map = node.getAttributes();
                    Node namedItem = map.getNamedItem("type");
                    if (namedItem == null) {
                        String reason = "Error finding 'type' inside /MediaContainer/Directory";
                        LOGGER.error(reason);
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                    }

                    String type = namedItem.getNodeValue();

                    if (type.equals("movie")) {
                        NamedNodeMap attributes = node.getAttributes();
                        Node titleNode = attributes.getNamedItem("title");
                        Node keyNode = attributes.getNamedItem("key");

                        if (titleNode == null) {
                            String reason = "Error finding 'title' inside /MediaContainer/Directory";
                            LOGGER.error(reason);
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                        }

                        if (keyNode == null) {
                            String reason = "Error finding 'key' inside /MediaContainer/Directory";
                            LOGGER.error(reason);
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                        }

                        String title = titleNode.getNodeValue().replaceAll(":", "");
                        Integer key = Integer.valueOf(keyNode.getNodeValue().trim());

                        PlexLibrary plexLibrary = new PlexLibrary(key, title, plexServer.getMachineIdentifier(), false);
                        plexLibraries.add(plexLibrary);
                    }
                }

            } catch (IOException e) {
                String reason = "Error connecting to Plex to get library list: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = "Error parsing XML from Plex: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with plex Url: " + url;
            LOGGER.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        LOGGER.info(plexLibraries.size() + " Plex libraries found");
        plexServer.getPlexLibraries().addAll(plexLibraries);
    }

    @Override
    public void queryPlexServer(@NotNull PlexServer plexServer) {
        LOGGER.info("queryPlexLibraries()");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexServer.getAddress())
                .port(plexServer.getPort())
                .addQueryParameter("X-Plex-Token", plexServer.getPlexToken())
                .build();

        //ToDo
        //Need to control time out here, using gaps object
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                DTMNodeList dtmNodeList = parseXml(response, url, "/MediaContainer");
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
                LOGGER.info("friendlyName:" + friendlyName);

                Node machineIdentifierNode = map.getNamedItem("machineIdentifier");
                if (machineIdentifierNode == null) {
                    String reason = "Error finding 'machineIdentifier' inside /";
                    LOGGER.error(reason);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
                }

                String machineIdentifier = machineIdentifierNode.getNodeValue().trim();
                LOGGER.info("machineIdentifier:" + machineIdentifier);

                plexServer.setFriendlyName(friendlyName);
                plexServer.setMachineIdentifier(machineIdentifier);
            } catch (IOException e) {
                String reason = "Error connecting to Plex to get library list: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = "Error parsing XML from Plex: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException e) {
            String reason = "Error with plex Url: " + url;
            LOGGER.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }
    }

    @Override
    public List<Movie> findAllPlexMovies(Map<MoviePair, Movie> previousMovies, @NotNull String url) {
        LOGGER.info("findAllPlexMovies()");

        List<Movie> ownedMovies = new ArrayList<>();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        if (StringUtils.isEmpty(url)) {
            LOGGER.info("No URL added to findAllPlexMovies().");
            return ownedMovies;
        }

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

                InputStream fileIS = new ByteArrayInputStream(body.getBytes());
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDocument = builder.parse(fileIS);
                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/MediaContainer/Video";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

                if (nodeList.getLength() == 0) {
                    String reason = "No movies found in url: " + url;
                    LOGGER.warn(reason);
                    return ownedMovies;
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
                        LOGGER.warn("Year not found for " + title);
                        continue;
                    }
                    int year = Integer.parseInt(node.getAttributes().getNamedItem("year").getNodeValue());

                    String guid = "";
                    if (node.getAttributes().getNamedItem("guid") != null) {
                        guid = node.getAttributes().getNamedItem("guid").getNodeValue();
                    }

                    String thumbnail = "";
                    if (node.getAttributes().getNamedItem("thumb") != null) {
                        thumbnail = node.getAttributes().getNamedItem("thumb").getNodeValue();
                    }

                    String summary = "";
                    if (node.getAttributes().getNamedItem("summary") != null) {
                        summary = node.getAttributes().getNamedItem("summary").getNodeValue();
                    }

                    Movie movie;
                    if (guid.contains("com.plexapp.agents.themoviedb")) {
                        //ToDo
                        //Find out what it looks like in TMDB
                        //language = ??
                        guid = guid.substring(guid.indexOf(ID_IDX_START) + ID_IDX_START.length(), guid.indexOf(ID_IDX_END));
                        movie = getOrCreateOwnedMovie(previousMovies, title, year, thumbnail, Integer.parseInt(guid), null, null, -1, null, summary);
                    } else if (guid.contains("com.plexapp.agents.imdb://")) {
                        String language = guid.substring(guid.indexOf("?lang=") + "?lang=".length());
                        language = new Locale(language, "").getDisplayLanguage();
                        guid = guid.substring(guid.indexOf(ID_IDX_START) + ID_IDX_START.length(), guid.indexOf(ID_IDX_END));
                        movie = getOrCreateOwnedMovie(previousMovies, title, year, thumbnail, -1, guid, language, -1, null, summary);
                    } else {
                        LOGGER.warn("Cannot handle guid value of " + guid);
                        movie = getOrCreateOwnedMovie(previousMovies, title, year, thumbnail, -1, null, null, -1, null, summary);
                    }

                    ownedMovies.add(movie);
                }
                LOGGER.debug(ownedMovies.size() + " movies found in plex");

            } catch (IOException e) {
                String reason = "Error connecting to Plex to get Movie list: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason, e);
            } catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
                String reason = "Error parsing XML from Plex: " + url;
                LOGGER.error(reason, e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason, e);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            String reason = "Error with plex Url: " + url;
            LOGGER.error(reason, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason, e);
        }

        return ownedMovies;
    }

    private Movie getOrCreateOwnedMovie(Map<MoviePair, Movie> previousMovies, String title, int year, String thumbnail, int tvdbId, String imdbId, String language, int collection, String collectionName, String summary) {
        MoviePair moviePair = new MoviePair(title, year);
        if (previousMovies.containsKey(moviePair)) {
            return previousMovies.get(moviePair);
        } else {
            return new Movie.Builder(title, year)
                    .setPosterUrl(thumbnail)
                    .setTvdbId(tvdbId)
                    .setImdbId(imdbId)
                    .setLanguage(language)
                    .setCollectionId(collection)
                    .setCollection(collectionName)
                    .setOverview(summary)
                    .build();
        }
    }

    private <T> T parseXml(Response response, HttpUrl url, String expression) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        String body = response.body() != null ? response.body().string() : null;

        if (StringUtils.isBlank(body)) {
            String reason = "Body returned null from Plex. Url: " + url;
            LOGGER.error(reason);
            throw new IllegalStateException(reason);
        }

        InputStream fileIS = new ByteArrayInputStream(body.getBytes());
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (T) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
    }

}
