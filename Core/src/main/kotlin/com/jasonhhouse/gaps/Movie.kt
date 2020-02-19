/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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