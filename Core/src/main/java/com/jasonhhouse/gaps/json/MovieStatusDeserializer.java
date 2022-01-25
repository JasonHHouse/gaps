package com.jasonhhouse.gaps.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jasonhhouse.gaps.MovieStatus;
import java.io.IOException;

public class MovieStatusDeserializer extends StdDeserializer<MovieStatus> {
    public MovieStatusDeserializer() {
        this(null);
    }

    protected MovieStatusDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MovieStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get(MovieStatus.ID_LABEL).numberValue().intValue();
        return MovieStatus.getMovieStatus(id);
    }
}
