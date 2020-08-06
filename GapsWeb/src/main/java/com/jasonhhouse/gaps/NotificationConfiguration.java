package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.notifications.EmailNotificationAgent;
import com.jasonhhouse.gaps.notifications.NotificationAgent;
import com.jasonhhouse.gaps.notifications.PushBulletNotificationAgent;
import com.jasonhhouse.gaps.notifications.SlackNotificationAgent;
import com.jasonhhouse.gaps.notifications.TelegramNotificationAgent;
import com.jasonhhouse.gaps.service.IoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    private final IoService ioService;

    public NotificationConfiguration(IoService ioService) {
        this.ioService = ioService;
    }

    @Bean
    public NotificationAgent getTelegramElement() {
        return new TelegramNotificationAgent(ioService);
    }

    @Bean
    public NotificationAgent getPushBulletElement() {
        return new PushBulletNotificationAgent(ioService);
    }

    @Bean
    public NotificationAgent getSlackElement() {
        return new SlackNotificationAgent(ioService);
    }

    @Bean
    public NotificationAgent getEmailElement() {
        return new EmailNotificationAgent(ioService);
    }
}
