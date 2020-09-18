/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.notifications;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.properties.EmailProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static com.jasonhhouse.gaps.notifications.NotificationStatus.SEND_MESSAGE;

public final class EmailNotificationAgent extends AbstractNotificationAgent<EmailProperties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationAgent.class);

    public EmailNotificationAgent(FileIoService fileIoService) {
        super(fileIoService);
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String getName() {
        return "Email Notification Agent";
    }

    @Override
    public boolean sendMessage(NotificationType notificationType, String level, String title, String message) {
        LOGGER.info(SEND_MESSAGE, level, title, message);

        if (sendPrepMessage(notificationType)) {
            return false;
        }

        JavaMailSenderImpl mailSender = getJavaMailSender();

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(t.getMailFrom());
            simpleMailMessage.setTo(t.getMailTo());
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            LOGGER.error("Error with Sending Email Notification.", e);
            return false;
        }
    }

    @NotNull
    @Override
    public EmailProperties getNotificationProperties() {
        return fileIoService.readProperties().getEmailProperties();
    }

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(t.getMailServer());
        mailSender.setPort(t.getMailPort());

        mailSender.setUsername(t.getUsername());
        mailSender.setPassword(t.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", t.getMailTransportProtocol());
        props.put("mail.smtp.auth", t.getMailSmtpAuth());
        props.put("mail.smtp.starttls.enable", t.getMailSmtpTlsEnabled());
        props.put("mail.debug", "false");

        return mailSender;
    }
}
