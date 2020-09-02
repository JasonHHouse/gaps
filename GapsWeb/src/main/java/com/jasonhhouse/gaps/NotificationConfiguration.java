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

import com.jasonhhouse.gaps.notifications.EmailNotificationAgent;
import com.jasonhhouse.gaps.notifications.GotifyNotificationAgent;
import com.jasonhhouse.gaps.notifications.PushBulletNotificationAgent;
import com.jasonhhouse.gaps.notifications.PushOverNotificationAgent;
import com.jasonhhouse.gaps.notifications.SlackNotificationAgent;
import com.jasonhhouse.gaps.notifications.TelegramNotificationAgent;
import com.jasonhhouse.gaps.service.FileIoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    private final FileIoService fileIoService;

    public NotificationConfiguration(FileIoService fileIoService) {
        this.fileIoService = fileIoService;
    }

    @Bean
    public TelegramNotificationAgent getTelegramElement() {
        return new TelegramNotificationAgent(fileIoService);
    }

    @Bean
    public PushBulletNotificationAgent getPushBulletElement() {
        return new PushBulletNotificationAgent(fileIoService);
    }

    @Bean
    public SlackNotificationAgent getSlackElement() {
        return new SlackNotificationAgent(fileIoService);
    }

    @Bean
    public EmailNotificationAgent getEmailElement() {
        return new EmailNotificationAgent(fileIoService);
    }

    @Bean
    public GotifyNotificationAgent getGotifyElement() {
        return new GotifyNotificationAgent(fileIoService);
    }

    @Bean
    public PushOverNotificationAgent getPushOverElement() {
        return new PushOverNotificationAgent(fileIoService);
    }
}
