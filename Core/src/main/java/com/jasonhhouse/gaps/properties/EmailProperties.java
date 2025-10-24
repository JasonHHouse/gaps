/*
 * Copyright 2025 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonhhouse.gaps.NotificationType;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class EmailProperties extends AbstractNotificationProperties {

    @NotNull
    private final String username;

    @NotNull
    private final String password;

    @NotNull
    private final String mailTo;

    @NotNull
    private final String mailFrom;

    @NotNull
    private final String mailServer;

    @NotNull
    private final Integer mailPort;

    @NotNull
    private final String mailTransportProtocol;

    @NotNull
    private final String mailSmtpAuth;

    @NotNull
    private final Boolean mailSmtpTlsEnabled;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public EmailProperties(@JsonProperty(value = "enabled", required = true) @NotNull Boolean enabled,
                           @JsonProperty(value = "notificationTypes", required = true) @NotNull List<NotificationType> notificationTypes,
                           @JsonProperty(value = "username") @Nullable String username,
                           @JsonProperty(value = "password") @Nullable String password,
                           @JsonProperty(value = "mailTo") @Nullable String mailTo,
                           @JsonProperty(value = "mailFrom") @Nullable String mailFrom,
                           @JsonProperty(value = "mailServer") @Nullable String mailServer,
                           @JsonProperty(value = "mailPort") @Nullable Integer mailPort,
                           @JsonProperty(value = "mailTransportProtocol") @Nullable String mailTransportProtocol,
                           @JsonProperty(value = "mailSmtpAuth") @Nullable String mailSmtpAuth,
                           @JsonProperty(value = "mailSmtpTlsEnabled") @Nullable Boolean mailSmtpTlsEnabled) {
        super(enabled, notificationTypes);
        this.username = username == null ? "" : username;
        this.password = password == null ? "" : password;
        this.mailTo = mailTo == null ? "" : mailTo;
        this.mailFrom = mailFrom == null ? "" : mailFrom;
        this.mailServer = mailServer == null ? "" : mailServer;
        this.mailPort = mailPort == null ? 0 : mailPort;
        this.mailTransportProtocol = mailTransportProtocol == null ? "" : mailTransportProtocol;
        this.mailSmtpAuth = mailSmtpAuth == null ? "" : mailSmtpAuth;
        this.mailSmtpTlsEnabled = mailSmtpTlsEnabled != null && mailSmtpTlsEnabled;
    }

    static EmailProperties getDefault() {
        return new EmailProperties(false, Collections.emptyList(), "", "", "", "", "", 0, "", "", false);
    }

    @NotNull
    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    @NotNull
    public String getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    @NotNull
    public Boolean getMailSmtpTlsEnabled() {
        return mailSmtpTlsEnabled;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getMailTo() {
        return mailTo;
    }

    @NotNull
    public String getMailFrom() {
        return mailFrom;
    }

    @NotNull
    public String getMailServer() {
        return mailServer;
    }

    @NotNull
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
                ", mailPort=" + mailPort +
                ", mailTransportProtocol='" + mailTransportProtocol + '\'' +
                ", mailSmtpAuth='" + mailSmtpAuth + '\'' +
                ", mailSmtpTlsEnabled=" + mailSmtpTlsEnabled +
                ", enabled=" + enabled +
                ", notificationTypes=" + notificationTypes +
                '}';
    }
}
