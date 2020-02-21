package com.jasonhhouse.gaps

data class PlexServer(var friendlyName: String = "",
                      var machineIdentifier: String = "",
                      val plexToken: String,
                      val address: String,
                      val port: Int) {
    val plexLibraries: MutableSet<PlexLibrary> = HashSet()
}