/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

export function testTelegram() {
    'use strict';




    getUserAsync('yourUsernameHere').then(data => console.log(data));

    $.ajax({
        type: "PUT",
        url: `/configuration/test/plex/${machineIdentifier}`,
        dataType: "json",
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.success) {
                plexTestSuccess.show();
            } else {
                plexTestError.show();
            }
        }, error: function () {
            hideAllAlertsAndSpinners();
            plexTestError.show();
        }
    });
}

async function testTelegramNotifications() {
    let response = await fetch('/notifications/test/0', {
        method: 'put',
    });
    let data = await response.json()
    return data;
}

async function saveTelegramNotifications(name) {
    const body = {};
    body.botId = document.getElementById('telegramBotId');
    body.chatId = document.getElementById('telegramChatId');
    body.enabled = document.getElementById('telegramEnabled')

    let response = await fetch(`/notifications/telegram`, {
        method: 'put',
        body: {}
    });
    let data = await response.json()
    return data;
}