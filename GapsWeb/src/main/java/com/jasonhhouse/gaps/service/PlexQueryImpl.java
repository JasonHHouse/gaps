package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.PlexLibrary;
import com.jasonhhouse.gaps.PlexQuery;
import com.jasonhhouse.gaps.PlexSearch;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
                String expression = "/MediaContainer/Directory";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

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

}
