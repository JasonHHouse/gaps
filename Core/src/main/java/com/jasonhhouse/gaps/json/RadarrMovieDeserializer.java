/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jasonhhouse.gaps.movie.RadarrMovie;
import java.io.IOException;

public class RadarrMovieDeserializer extends StdDeserializer<RadarrMovie> {

    public RadarrMovieDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RadarrMovie deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode radarrNode = jsonParser.getCodec().readTree(jsonParser);
        RadarrMovie.Builder builder = new RadarrMovie.Builder(radarrNode.get("title").textValue(), radarrNode.get("year").intValue());
        builder.setTmdbId(radarrNode.get("tmdbId").intValue());
        builder.setImdbId(radarrNode.get("imdbId").textValue());

        if (radarrNode.has("collection")) {
            builder.setCollectionTitle(radarrNode.get("collection").get("name").textValue());
            builder.setCollectionId(radarrNode.get("collection").get("tmdbId").intValue());
        }

        if (radarrNode.has("images")) {
            for (JsonNode imageNode : radarrNode.get("images")) {
                if ("poster".equalsIgnoreCase(imageNode.get("coverType").textValue())) {
                    builder.setPosterUrl(radarrNode.get("url").textValue());
                }
            }
        }

        return builder.build();
    }
}
