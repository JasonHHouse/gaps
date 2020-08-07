/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.properties;

import com.jasonhhouse.gaps.NotificationType;
import java.util.List;

public class GotifyProperties extends AbstractNotificationProperties {

    private final String address;
    private final String token;

    public GotifyProperties(Boolean isEnabled, List<NotificationType> notificationTypes, String address, String token) {
        super(isEnabled, notificationTypes);
        this.address = address;
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "GotifyProperties{" +
                "address='" + address + '\'' +
                ", token='" + token + '\'' +
                ", isEnabled=" + isEnabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
