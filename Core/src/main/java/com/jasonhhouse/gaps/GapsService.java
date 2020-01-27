/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Blackboard service interface for storing the PlexSearch object controllers
 */
public interface GapsService {

    /**
     * @return Returns the PlexSearch instance as a singleton
     */
    PlexSearch getPlexSearch();

    /**
     * Updates PlexLibrary's to add them if not added and set them selected or unselected if added
     *
     * @param selectedLibraries The libraries to update. they must come in as a single string added together of the plex server ID and the library key.
     */
    void updateLibrarySelections(@NotNull List<String> selectedLibraries);

    /**
     * Updates the plex search object itself to the singleton object
     *
     * @param plexSearch The object to copy into the plex search singleton
     */
    void updatePlexSearch(PlexSearch plexSearch);
}
