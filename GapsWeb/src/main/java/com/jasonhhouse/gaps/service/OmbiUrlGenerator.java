package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.properties.OmbiProperties;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OmbiUrlGenerator {

    public @NotNull HttpUrl generateStatusUrl(@NotNull OmbiProperties ombiProperties) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(ombiProperties.getAddress())
                .port(ombiProperties.getPort())
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("Status")
                .build();
    }

    public @NotNull HttpUrl generateMovieRequestsUrl(@NotNull OmbiProperties ombiProperties) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(ombiProperties.getAddress())
                .port(ombiProperties.getPort())
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("Request")
                .addPathSegment("movie")
                .build();
    }
}
