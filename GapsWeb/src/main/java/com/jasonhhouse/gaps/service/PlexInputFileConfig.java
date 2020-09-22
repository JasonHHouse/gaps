/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.service;

import java.io.File;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;

public class PlexInputFileConfig implements InputFileConfig {

    private final @NotNull String machineIdentifier;
    private final @NotNull Integer key;

    public PlexInputFileConfig(@NotNull String machineIdentifier, @NotNull Integer key) {
        this.machineIdentifier = machineIdentifier;
        this.key = key;
    }

    @Override
    public @NotNull File getJsonFile() {
        return Paths.get(machineIdentifier, key.toString(), "movies.json").toFile();
    }

    public @NotNull String getMachineIdentifier() {
        return machineIdentifier;
    }

    public @NotNull Integer getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "PlexInputFileConfig{" +
                "machineIdentifier='" + machineIdentifier + '\'' +
                ", key=" + key +
                '}';
    }
}
