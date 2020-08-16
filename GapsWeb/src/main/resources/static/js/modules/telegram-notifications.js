/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import {getNotificationTypes} from '/js/modules/notification-types.min.js';
import {hideAllAlertsAndSpinners} from "/js/modules/alerts-manager.min.js";

export async function testTelegramNotifications() {
    'use strict';
    hideAllAlertsAndSpinners();
    document.getElementById('telegramSpinner').style.display = 'block';

    let response = await fetch('/notifications/test/0', {
        method: 'put',
    });
    await response.json();
    if (response.ok) {
        hideAllAlertsAndSpinners();
        document.getElementById('telegramTestSuccess').style.display = 'block';
    } else {
        hideAllAlertsAndSpinners();
        document.getElementById('telegramTestError').style.display = 'block';
    }
}

export async function saveTelegramNotifications() {
    'use strict';
    hideAllAlertsAndSpinners();

    const body = {};
    body.botId = document.getElementById('telegramBotId').value;
    body.chatId = document.getElementById('telegramChatId').value;
    body.enabled = document.getElementById('telegramEnabled').value;
    body.notificationTypes = getNotificationTypes(document.getElementById('telegramTmdbApiConnectionNotification').checked,
        document.getElementById('telegramPlexServerConnectionNotification').checked,
        document.getElementById('telegramPlexMetadataUpdateNotification').checked,
        document.getElementById('telegramPlexLibraryUpdateNotification').checked,
        document.getElementById('telegramGapsMissingCollectionsNotification').checked);

    let response = await fetch(`/notifications/telegram`, {
        method: 'put',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    });
    await response.json();
    if (response.ok) {
        hideAllAlertsAndSpinners();
        document.getElementById('telegramSaveSuccess').style.display = 'block';
    } else {
        hideAllAlertsAndSpinners();
        document.getElementById('telegramSaveError').style.display = 'block';
    }
}