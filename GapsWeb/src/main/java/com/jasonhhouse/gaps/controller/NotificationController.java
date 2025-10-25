/*
 *
 *  Copyright 2025 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.gaps.controller;

import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.properties.DiscordProperties;
import com.jasonhhouse.gaps.properties.EmailProperties;
import com.jasonhhouse.gaps.properties.GotifyProperties;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.properties.PushBulletProperties;
import com.jasonhhouse.gaps.properties.PushOverProperties;
import com.jasonhhouse.gaps.properties.SlackProperties;
import com.jasonhhouse.gaps.properties.TelegramProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import com.jasonhhouse.gaps.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/notifications")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final FileIoService fileIoService;

    public NotificationController(NotificationService notificationService, FileIoService fileIoService) {
        this.notificationService = notificationService;
        this.fileIoService = fileIoService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/email")
    @ResponseBody
    public ResponseEntity<Payload> putEmail(@RequestBody EmailProperties emailProperties) {
        LOGGER.info("putEmail( {} )", emailProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setEmailProperties(emailProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Email Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.EMAIL_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.EMAIL_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.EMAIL_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/email")
    @ResponseBody
    public ResponseEntity<Payload> getEmail() {
        LOGGER.info("getEmail()");

        try {
            return ResponseEntity.ok().body(Payload.EMAIL_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getEmailProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.EMAIL_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.EMAIL_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/discord")
    @ResponseBody
    public ResponseEntity<Payload> putDiscord(@RequestBody DiscordProperties discordProperties) {
        LOGGER.info("putDiscord( {} )", discordProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setDiscordProperties(discordProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Discord Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.DISCORD_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.DISCORD_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.DISCORD_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/discord")
    @ResponseBody
    public ResponseEntity<Payload> getDiscord() {
        LOGGER.info("getDiscord()");

        try {
            return ResponseEntity.ok().body(Payload.DISCORD_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getDiscordProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.DISCORD_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.DISCORD_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/gotify")
    @ResponseBody
    public ResponseEntity<Payload> putGotify(@RequestBody GotifyProperties gotifyProperties) {
        LOGGER.info("putGotify( {} )", gotifyProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setGotifyProperties(gotifyProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Gotify Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.GOTIFY_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.GOTIFY_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.GOTIFY_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/gotify")
    @ResponseBody
    public ResponseEntity<Payload> getGotify() {
        LOGGER.info("getGotify()");

        try {
            return ResponseEntity.ok().body(Payload.GOTIFY_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getGotifyProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.GOTIFY_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.GOTIFY_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/pushbullet")
    @ResponseBody
    public ResponseEntity<Payload> putPushBullet(@RequestBody PushBulletProperties pushBulletProperties) {
        LOGGER.info("putPushBullet( {} )", pushBulletProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setPushBulletProperties(pushBulletProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("PushBullet Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.PUSH_BULLET_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.PUSH_BULLET_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.PUSH_BULLET_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/pushbullet")
    @ResponseBody
    public ResponseEntity<Payload> getPushBullet() {
        LOGGER.info("getPushBullet()");

        try {
            return ResponseEntity.ok().body(Payload.PUSH_BULLET_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getPushBulletProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.PUSH_BULLET_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.PUSH_BULLET_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/slack")
    @ResponseBody
    public ResponseEntity<Payload> putSlack(@RequestBody SlackProperties slackProperties) {
        LOGGER.info("putSlack( {} )", slackProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setSlackProperties(slackProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Slack Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.SLACK_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.SLACK_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.SLACK_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/slack")
    @ResponseBody
    public ResponseEntity<Payload> getSlack() {
        LOGGER.info("getSlack()");

        try {
            return ResponseEntity.ok().body(Payload.SLACK_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getSlackProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.SLACK_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.SLACK_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/telegram")
    @ResponseBody
    public ResponseEntity<Payload> putTelegram(@RequestBody TelegramProperties telegramProperties) {
        LOGGER.info("putTelegram( {} )", telegramProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setTelegramProperties(telegramProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Telegram Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.TELEGRAM_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.TELEGRAM_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.TELEGRAM_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/telegram")
    @ResponseBody
    public ResponseEntity<Payload> getTelegram() {
        LOGGER.info("getTelegram()");

        try {
            return ResponseEntity.ok().body(Payload.TELEGRAM_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getTelegramProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.TELEGRAM_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.TELEGRAM_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/pushOver")
    @ResponseBody
    public ResponseEntity<Payload> putPushOver(@RequestBody PushOverProperties pushOverProperties) {
        LOGGER.info("putPushOver( {} )", pushOverProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setPushOverProperties(pushOverProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info("Telegram Properties Updated Successfully");
            return ResponseEntity.ok().body(Payload.PUSH_OVER_NOTIFICATION_UPDATE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.PUSH_OVER_NOTIFICATION_UPDATE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.PUSH_OVER_NOTIFICATION_UPDATE_FAILED.setExtras(e.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/pushOver")
    @ResponseBody
    public ResponseEntity<Payload> getPushOver() {
        LOGGER.info("getPushOver()");

        try {
            return ResponseEntity.ok().body(Payload.PUSH_OVER_NOTIFICATION_FOUND.setExtras(fileIoService.readProperties().getPushOverProperties()));
        } catch (Exception e) {
            LOGGER.error(Payload.PUSH_OVER_NOTIFICATION_NOT_FOUND.getReason(), e);
            return ResponseEntity.ok().body(Payload.PUSH_OVER_NOTIFICATION_NOT_FOUND.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/test/all")
    @ResponseBody
    public ResponseEntity<Payload> putTestAll() {
        LOGGER.info("putTestAll()");

        try {
            boolean result = notificationService.test();

            if (result) {
                LOGGER.info("Notification Test All Succeeded");
                return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_SUCCEEDED);
            } else {
                LOGGER.error("Notification Test All Failed");
                return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED);
            }
        } catch (Exception e) {
            LOGGER.error("Notification Test All Failed", e);
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/test/{id}")
    @ResponseBody
    public ResponseEntity<Payload> putTest(@PathVariable("id") final Integer id) {
        LOGGER.info("putTest( {} )", id);

        try {
            boolean result = notificationService.test(id);
            if (result) {
                LOGGER.info("Notification Test with ID {} Succeeded", id);
                return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_SUCCEEDED);
            } else {
                LOGGER.error("Notification Test Failed with ID {}", id);
                return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Notification Test Failed with ID %s", id), e);
            return ResponseEntity.ok().body(Payload.NOTIFICATION_TEST_FAILED.setExtras(e.getMessage()));
        }
    }
}
