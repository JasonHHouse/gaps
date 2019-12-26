package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jasonhhouse.gaps.Movie;
import java.io.IOException;

public class MovieDeserializer extends StdDeserializer<Movie> {
    public MovieDeserializer() {
        this(null);
    }

    protected MovieDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Movie deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int tvdbId = (Integer) node.get(Movie.TVDB_ID).numberValue();
        String imdbId = node.get(Movie.IMDB_ID).asText();
        String name = node.get(Movie.NAME).asText();
        int year = (Integer) node.get(Movie.YEAR).numberValue();
        int collectionId = (Integer) node.get(Movie.COLLECTION_ID).numberValue();
        String collection = node.get(Movie.COLLECTION).asText();

        String posterUrl = null;
        if (node.has(Movie.POSTER)) {
            posterUrl = node.get(Movie.POSTER).asText();
        }

        Movie.Builder builder = new Movie.Builder(name, year)
                .setTvdbId(tvdbId)
                .setImdbId(imdbId)
                .setCollectionId(collectionId)
                .setCollection(collection)
                .setPosterUrl(posterUrl);

        return builder.build();
    }
}
