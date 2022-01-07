/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import getNotificationTypes from './notification-types.min.js';
import Payload from './payload.min.js';
import hideAllAlertsAndSpinners from './alerts-manager.min.js';
import { getContextPath } from './common.min.js';

export async function testEmailNotifications() {
  hideAllAlertsAndSpinners();
  document.getElementById('emailSpinner').style.display = 'block';

  const response = await fetch(getContextPath('/notifications/test/4'), {
    method: 'put',
  });
  const put = await response.json();
  if (put.code && put.code === Payload.NOTIFICATION_TEST_SUCCEEDED) {
    hideAllAlertsAndSpinners();
    document.getElementById('emailTestSuccess').style.display = 'block';
  } else {
    hideAllAlertsAndSpinners();
    document.getElementById('emailTestError').style.display = 'block';
  }
}

export async function saveEmailNotifications() {
  hideAllAlertsAndSpinners();

  const body = {};
  body.username = document.getElementById('emailUsername').value;
  body.password = document.getElementById('emailPassword').value;
  body.mailTo = document.getElementById('emailMailTo').value;
  body.mailFrom = document.getElementById('emailMailFrom').value;
  body.mailServer = document.getElementById('emailServer').value;
  body.mailPort = document.getElementById('emailPort').value;
  body.mailTransportProtocol = document.getElementById('emailTransportProtocol').value;
  body.mailSmtpAuth = document.getElementById('emailSmtpAuth').value;
  body.mailSmtpTlsEnabled = document.getElementById('emailSmtpTlsEnabled').value;

  body.enabled = document.getElementById('emailEnabled').value;
  body.notificationTypes = getNotificationTypes(
    document.getElementById('emailTmdbApiConnectionNotification').checked,
    document.getElementById('emailPlexServerConnectionNotification').checked,
    document.getElementById('emailPlexMetadataUpdateNotification').checked,
    document.getElementById('emailPlexLibraryUpdateNotification').checked,
    document.getElementById('emailGapsMissingCollectionsNotification').checked,
  );

  const response = await fetch(getContextPath('/notifications/email'), {
    method: 'put',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  });
  const put = await response.json();
  if (put.code && put.code === Payload.EMAIL_NOTIFICATION_UPDATE_SUCCEEDED) {
    hideAllAlertsAndSpinners();
    document.getElementById('emailSaveSuccess').style.display = 'block';
  } else {
    hideAllAlertsAndSpinners();
    document.getElementById('emailSaveError').style.display = 'block';
  }
}
