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

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.PlexSearch;
import com.jasonhhouse.gaps.PlexServer;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexQueryImpl.class);

    @Override
    public @NotNull List<PlexLibrary> getLibraries(@NotNull PlexSearch plexSearch) {
        LOGGER.info("queryPlexLibraries()");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexSearch.getAddress())
                .port(plexSearch.getPort())
                .addPathSegment("library")
                .addPathSegment("sections")
                .addQueryParameter("X-Plex-Token", plexSearch.getPlexToken())
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

                        PlexLibrary plexLibrary = new PlexLibrary(key, title, false);
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

        return plexLibraries;
    }

    @Override
    public @NotNull PlexServer getPlexServer(@NotNull PlexSearch plexSearch) {
        LOGGER.info("queryPlexLibraries()");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(plexSearch.getAddress())
                .port(plexSearch.getPort())
                .addQueryParameter("X-Plex-Token", plexSearch.getPlexToken())
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

                return new PlexServer(friendlyName, machineIdentifier);
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
