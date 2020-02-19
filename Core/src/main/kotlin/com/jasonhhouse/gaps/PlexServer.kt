package com.jasonhhouse.gaps

data class PlexServer(val friendlyName : String,
                      val machineIdentifier : String,
                      val plexToken : String,
                      val address : String,
                      val port : Int) {
    val plexLibraries : MutableSet<PlexLibrary> = HashSet()
}