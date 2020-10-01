/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* jshint esversion: 6 */

import getNotificationTypes from './notification-types.min.js';
import Payload from './payload.min.js';
import hideAllAlertsAndSpinners from './alerts-manager.min.js';

export async function getSoundOptions() {
  let defaultSound;
  const pushOverResponse = await fetch('/notifications/pushOver', {
    method: 'get',
  });
  const pushOverBody = await pushOverResponse.json();
  if (pushOverBody.code && pushOverBody.code === Payload.PUSH_OVER_NOTIFICATION_FOUND && pushOverBody.extras && pushOverBody.extras.sound) {
    defaultSound = pushOverBody.extras.sound;
  }

  const soundsResponse = await fetch('/sounds', {
    method: 'get',
  });
  const body = await soundsResponse.json();
  if (body.status && body.status === 1) {
    Object.keys(body.sounds).forEach((key) => {
      const option = document.createElement('option');
      option.value = key;
      option.text = body.sounds.key;
      if (defaultSound === key) {
        option.defaultSelected = true;
      }
      document.getElementById('pushOverSound').appendChild(option);
    });
  } else {
    const option = document.createElement('option');
    option.value = 'pushover';
    option.text = 'Pushover (default)';
    document.getElementById('pushOverSound').appendChild(option);
  }
}

export async function testPushOverNotifications() {
  hideAllAlertsAndSpinners();
  document.getElementById('pushOverSpinner').style.display = 'block';

  const response = await fetch('/notifications/test/5', {
    method: 'put',
  });
  const put = await response.json();
  if (put.code && put.code === Payload.NOTIFICATION_TEST_SUCCEEDED) {
    hideAllAlertsAndSpinners();
    document.getElementById('pushOverTestSuccess').style.display = 'block';
  } else {
    hideAllAlertsAndSpinners();
    document.getElementById('pushOverTestError').style.display = 'block';
  }
}

export async function savePushOverNotifications() {
  hideAllAlertsAndSpinners();

  const body = {};
  body.token = document.getElementById('pushOverToken').value;
  body.user = document.getElementById('pushOverUser').value;
  body.priority = document.getElementById('pushOverPriority').value;
  body.sound = document.getElementById('pushOverSound').value;
  body.retry = document.getElementById('pushOverRetry').value;
  body.expire = document.getElementById('pushOverExpire').value;
  body.enabled = document.getElementById('pushOverEnabled').value;
  body.notificationTypes = getNotificationTypes(document.getElementById('pushOverTmdbApiConnectionNotification').checked,
    document.getElementById('pushOverPlexServerConnectionNotification').checked,
    document.getElementById('pushOverPlexMetadataUpdateNotification').checked,
    document.getElementById('pushOverPlexLibraryUpdateNotification').checked,
    document.getElementById('pushOverGapsMissingCollectionsNotification').checked);

  const response = await fetch('/notifications/pushOver', {
    method: 'put',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  });
  const put = await response.json();
  if (put.code && put.code === Payload.PUSH_OVER_NOTIFICATION_UPDATE_SUCCEEDED) {
    hideAllAlertsAndSpinners();
    document.getElementById('pushOverSaveSuccess').style.display = 'block';
  } else {
    hideAllAlertsAndSpinners();
    document.getElementById('pushOverSaveError').style.display = 'block';
  }
}
