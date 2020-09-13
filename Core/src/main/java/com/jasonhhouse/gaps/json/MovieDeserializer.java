/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jasonhhouse.gaps.BasicMovie;
import com.jasonhhouse.gaps.MovieFromCollection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDeserializer extends StdDeserializer<BasicMovie> {
    public MovieDeserializer() {
        this(null);
    }

    protected MovieDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BasicMovie deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int tvdbId = (Integer) node.get(BasicMovie.TVDB_ID).numberValue();
        String imdbId = node.get(BasicMovie.IMDB_ID).textValue();
        String name = node.get(BasicMovie.NAME).textValue();
        int year = (Integer) node.get(BasicMovie.YEAR).numberValue();


        int collectionId = -1;
        if (node.has(BasicMovie.COLLECTION_ID)) {
            collectionId = (Integer) node.get(BasicMovie.COLLECTION_ID).numberValue();
        }

        String collection = null;
        if (node.has(BasicMovie.COLLECTION)) {
            collection = node.get(BasicMovie.COLLECTION).textValue();
        }

        String posterUrl = null;
        if (node.has(BasicMovie.POSTER)) {
            posterUrl = node.get(BasicMovie.POSTER).textValue();
        }

        String language = null;
        if (node.has(BasicMovie.LANGUAGE)) {
            language = node.get(BasicMovie.LANGUAGE).textValue();
        }

        String overview = null;
        if (node.has(BasicMovie.OVERVIEW)) {
            overview = node.get(BasicMovie.OVERVIEW).textValue();
        }

        List<MovieFromCollection> moviesInCollection = new ArrayList<>();
        if (node.has(BasicMovie.MOVIES_IN_COLLECTION)) {
            ArrayNode arrayNode = (ArrayNode) node.get(BasicMovie.MOVIES_IN_COLLECTION);
            if (arrayNode.isArray()) {
                for (JsonNode jsonNode : arrayNode) {
                    moviesInCollection.add(new MovieFromCollection(jsonNode.get("title").textValue(), jsonNode.get("id").textValue(), jsonNode.get("owned").booleanValue()));
                }
            }
        }

        BasicMovie.Builder builder = new BasicMovie.Builder(name, year)
                .setTvdbId(tvdbId)
                .setImdbId(imdbId)
                .setCollectionId(collectionId)
                .setCollection(collection)
                .setPosterUrl(posterUrl)
                .setLanguage(language)
                .setOverview(overview)
                .setMoviesInCollection(moviesInCollection);

        return builder.build();
    }
}
