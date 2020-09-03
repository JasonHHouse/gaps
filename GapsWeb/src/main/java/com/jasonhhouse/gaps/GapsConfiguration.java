/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@ConstructorBinding
@ConfigurationProperties("info.app")
public class GapsConfiguration {

    private final String name;
    private final String description;
    private final String version;
    private final Boolean loginEnabled;
    private final Boolean sslEnabled;
    private final String storageFolder;
    private final Properties properties;

    public GapsConfiguration(String name, String description, String version, Boolean loginEnabled, Boolean sslEnabled, String storageFolder, Properties properties) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.loginEnabled = loginEnabled;
        this.sslEnabled = sslEnabled;
        this.storageFolder = storageFolder;
        this.properties = properties;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public Boolean getLoginEnabled() {
        return loginEnabled;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "YamlConfig{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", loginEnabled=" + loginEnabled +
                ", sslEnabled=" + sslEnabled +
                ", storagePath='" + storageFolder + '\'' +
                '}';
    }

    @ConstructorBinding
    public static final class Properties {
        private final String rssFeed;
        private final String gapsProperties;
        private final String movieIds;
        private final String ownedMovies;
        private final String recommendedMovies;

        public Properties(String rssFeed, String gapsProperties, String movieIds, String ownedMovies, String recommendedMovies) {
            this.rssFeed = rssFeed;
            this.gapsProperties = gapsProperties;
            this.movieIds = movieIds;
            this.ownedMovies = ownedMovies;
            this.recommendedMovies = recommendedMovies;
        }

        public String getRssFeed() {
            return rssFeed;
        }

        public String getGapsProperties() {
            return gapsProperties;
        }

        public String getMovieIds() {
            return movieIds;
        }

        public String getOwnedMovies() {
            return ownedMovies;
        }

        public String getRecommendedMovies() {
            return recommendedMovies;
        }
    }
}
