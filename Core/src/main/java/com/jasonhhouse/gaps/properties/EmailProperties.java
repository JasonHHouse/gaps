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

public final class EmailProperties extends AbstractNotificationProperties {

    private final String username;
    private final String password;
    private final String mailTo;
    private final String mailFrom;
    private final String mailServer;
    private final Integer mailPort;
    private final String mailTransportProtocol;
    private final String mailSmtpAuth;
    private final Boolean mailSmtpTlsEnabled;

    public EmailProperties(Boolean isEnabled, List<NotificationType> notificationTypes, String username, String password, String mailTo, String mailFrom, String mailServer, Integer mailPort, String mailTransportProtocol, String mailSmtpAuth, Boolean mailSmtpTlsEnabled) {
        super(isEnabled, notificationTypes);
        this.username = username;
        this.password = password;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.mailServer = mailServer;
        this.mailPort = mailPort;
        this.mailTransportProtocol = mailTransportProtocol;
        this.mailSmtpAuth = mailSmtpAuth;
        this.mailSmtpTlsEnabled = mailSmtpTlsEnabled;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public String getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public Boolean getMailSmtpTlsEnabled() {
        return mailSmtpTlsEnabled;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailServer() {
        return mailServer;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    @Override
    public String toString() {
        return "EmailProperties{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mailTo='" + mailTo + '\'' +
                ", mailFrom='" + mailFrom + '\'' +
                ", mailServer='" + mailServer + '\'' +
                ", mailPort='" + mailPort + '\'' +
                ", mailTransportProtocol='" + mailTransportProtocol + '\'' +
                ", mailSmtpAuth='" + mailSmtpAuth + '\'' +
                ", mailSmtpTlsEnabled=" + mailSmtpTlsEnabled +
                '}';
    }
}
