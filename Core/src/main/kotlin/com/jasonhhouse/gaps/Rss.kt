package com.jasonhhouse.gaps

import com.fasterxml.jackson.annotation.JsonProperty

data class Rss(@JsonProperty("imdb_id") var imdbId: String = "",
               @JsonProperty("release_date") var releaseData: Int = -1,
               @JsonProperty("tvdb_id") var tvdbId: Int = -1,
               @JsonProperty("title") var title: String = "",
               @JsonProperty("poster_path") var posterPath: String = "") {
    constructor() : this("", -1, -1, "", "")
}