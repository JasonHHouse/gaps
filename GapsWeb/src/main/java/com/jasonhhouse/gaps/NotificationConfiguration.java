package com.jasonhhouse.gaps;

import com.jasonhhouse.gaps.notifications.NotificationAgent;
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
    public NotificationAgent getElement() {
        return new TelegramNotificationAgent(ioService);
    }

}
