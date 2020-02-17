package com.jasonhhouse.gaps

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.jasonhhouse.gaps.json.MovieDeserializer
import com.jasonhhouse.gaps.json.MovieSerializer

@JsonSerialize(using = MovieSerializer::class)
@JsonDeserialize(using = MovieDeserializer::class)
data class Movie(val name : String, val year : Int) : Comparable<Movie> {
    @JsonProperty(POSTER)
    var posterUrl : String = ""
    var collection : String = ""
    var collectionId : Int = -1
    var tvdbId : Int = -1;
    var imdbId : String = ""
    var language : String = ""
    var overview : String = ""

    override fun compareTo(other: Movie): Int {
        return name.compareTo(other.name)
    }

    companion object {
        const val TVDB_ID = "tvdbId"
        const val IMDB_ID = "imdbId"
        const val NAME = "name"
        const val YEAR = "year"
        const val POSTER = "poster_url"
        const val COLLECTION_ID = "collectionId"
        const val COLLECTION = "collection"
        const val LANGUAGE = "language"
        const val OVERVIEW = "overview"

    }
}