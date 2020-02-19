package com.jasonhhouse.gaps

data class PlexSearch (var movieDbApiKey : String?) {
    val plexServers : MutableSet<PlexServer> = HashSet()
    companion object {
        const val MOVIE_DB_API_KEY = "movieDbApiKey"
    }

    fun addPlexServer(plexServer: PlexServer) {
        plexServers.add(plexServer)
    }
}