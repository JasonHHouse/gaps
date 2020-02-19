package com.jasonhhouse.gaps

/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Interface to handle connecting to a Plex instance and returning the known movie libraries
 */
interface PlexQuery {

    /**
     * @param plexServer Needs to have the IP Address, port, and plex token to connect
     */
    fun getLibraries(plexServer: PlexServer): Payload

    /**
     * Find the plex server name, key, and libraries based on the given PlexSearch parameters
     *
     * @param plexServer the search parameters
     */
    fun queryPlexServer(plexServer: PlexServer): Payload

    /**
     * Connect to plex via the URL and parse all the movies from the returned XML creating a HashSet of movies the
     * user has.
     */
    fun findAllPlexMovies(previousMovies: Map<MoviePair, Movie>, url: String): List<Movie>
}