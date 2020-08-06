package com.jasonhhouse.gaps.notifications;

import com.jasonhhouse.gaps.NotificationType;
import com.jasonhhouse.gaps.service.IoService;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailNotificationAgent implements NotificationAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationAgent.class);

    private final String username;
    private final String password;
    private final String mailTo;
    private final String mailFrom;
    private final JavaMailSenderImpl mailSender;
    private final IoService ioService;
    //private final List<NotificationType> notificationTypes;

    public EmailNotificationAgent(IoService ioService) {
        this.ioService = ioService;
        username = "jh5975";
        password = "";
        mailTo = "jh5975@gmail.com";
        mailFrom = "jh5975@gmail.com";
        mailSender = getJavaMailSender();
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
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean sendMessage(NotificationType notificationType, String level, String title, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailFrom);
            simpleMailMessage.setTo(mailTo);
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            LOGGER.error("Error with Sending Email Notification.", e);
            return false;
        }
    }

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
