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

/* global Handlebars, SockJS, Stomp */
/* eslint no-undef: "error" */

import hideAllAlertsAndSpinners from '../modules/alerts-manager.min.js';
import saveSchedule from '../modules/schedule.min.js';
import saveMovieStatus from '../modules/movieStatus.min.js';
import Payload from '../modules/payload.min.js';
import { getContextPath } from '../modules/common.min.js';
import { saveTelegramNotifications, testTelegramNotifications } from '../modules/telegram-notifications.min.js';
import { saveSlackNotifications, testSlackNotifications } from '../modules/slack-notifications.min.js';
import { savePushBulletNotifications, testPushBulletNotifications } from '../modules/push-bullet-notifications.min.js';
import { saveGotifyNotifications, testGotifyNotifications } from '../modules/gotify-notifications.min.js';
import { saveEmailNotifications, testEmailNotifications } from '../modules/email-notifications.min.js';
import { saveDiscordNotifications, testDiscordNotifications } from '../modules/discord-notifications.min.js';
import {
  getSoundOptions,
  savePushOverNotifications,
  testPushOverNotifications,
} from '../modules/push-over-notifications.min.js';
import { openPlexLibraryConfigurationModel, savePlexLibraryConfiguration } from '../modules/plex-configuration.min.js';

function testTmdbKey() {
  hideAllAlertsAndSpinners();

  if (!$('#tmdbConfiguration')[0].checkValidity()) {
    return false;
  }

  document.getElementById('tmdbSpinner').style.display = 'block';

  $.ajax({
    type: 'PUT',
    url: getContextPath(`/configuration/test/tmdbKey/${$('#movieDbApiKey').val()}`),
    contentType: 'application/json',
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.TMDB_KEY_VALID) {
        document.getElementById('tmdbTestSuccess').style.display = 'block';
      } else {
        document.getElementById('tmdbTestError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('tmdbTestError').style.display = 'block';
    },
  });
  return true;
}

function saveTmdbKey() {
  hideAllAlertsAndSpinners();

  if (!$('#tmdbConfiguration')[0].checkValidity()) {
    return false;
  }

  document.getElementById('tmdbSpinner').style.display = 'block';

  $.ajax({
    type: 'POST',
    url: getContextPath(`/configuration/save/tmdbKey/${$('#movieDbApiKey').val()}`),
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.TMDB_KEY_SAVE_SUCCESSFUL) {
        document.getElementById('tmdbSaveSuccess').style.display = 'block';
      } else {
        document.getElementById('tmdbSaveError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('tmdbSaveError').style.display = 'block';
    },
  });
  return true;
}

function testPlexServer() {
  hideAllAlertsAndSpinners();

  if (!$('#plexConfiguration')[0].checkValidity()) {
    return false;
  }

  document.getElementById('plexSpinner').style.display = 'block';

  $.ajax({
    type: 'PUT',
    url: getContextPath('/configuration/test/plex'),
    data: {
      plexToken: $('#plexToken').val(),
      address: $('#address').val(),
      port: $('#port').val(),
    },
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.PLEX_CONNECTION_SUCCEEDED) {
        document.getElementById('plexTestSuccess').style.display = 'block';
      } else {
        document.getElementById('plexTestError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('plexTestError').style.display = 'block';
    },
  });
  return true;
}

function addPlexServer() {
  hideAllAlertsAndSpinners();

  if (!$('#plexConfiguration')[0].checkValidity()) {
    return false;
  }

  document.getElementById('plexSpinner').style.display = 'block';

  $.ajax({
    type: 'POST',
    url: getContextPath('/configuration/add/plex'),
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    data: {
      plexToken: $('#plexToken').val(),
      address: $('#address').val(),
      port: $('#port').val(),
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('plexSaveError').style.display = 'block';
    },
  });
  return true;
}

function testExistingPlexServer(machineIdentifier) {
  hideAllAlertsAndSpinners();
  document.getElementById('plexSpinner').style.display = 'block';

  $.ajax({
    type: 'PUT',
    url: getContextPath(`/configuration/test/plex/${machineIdentifier}`),
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.success) {
        document.getElementById('plexTestSuccess').style.display = 'block';
      } else {
        document.getElementById('plexTestError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('plexTestError').style.display = 'block';
    },
  });
}

function removePlexServer(machineIdentifier) {
  hideAllAlertsAndSpinners();
  document.getElementById('plexSpinner').style.display = 'block';

  $.ajax({
    type: 'DELETE',
    url: getContextPath(`/configuration/delete/plex/${machineIdentifier}`),
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.success) {
        $(`#${machineIdentifier}`).remove();
        document.getElementById('plexDeleteSuccess').style.display = 'block';
      } else {
        document.getElementById('plexDeleteError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('plexDeleteError').style.display = 'block';
    },
  });
}

function setDeleteAllEnabledOrDisabled() {
  $('#deleteAll').prop('disabled', !$('#confirmDeleteAll').is(':checked'));
}

function nuke() {
  $.ajax({
    type: 'PUT',
    url: getContextPath('/nuke'),
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.NUKE_SUCCESSFUL) {
        document.getElementById('deleteAllSuccess').style.display = 'block';
        $('#movieDbApiKey').val('');
        $('#plexServers').html('');
      } else {
        document.getElementById('deleteAllError').style.display = 'block';
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      document.getElementById('deleteAllError').style.display = 'block';
    },
  });
}

window.addEventListener('load', () => {
  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  const forms = document.getElementsByClassName('needs-validation');
  // Loop over them and prevent submission
  Array.prototype.filter.call(forms, (form) => {
    form.addEventListener('click', (event) => {
      if (form.checkValidity() === false) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add('was-validated');
    }, false);
  });
}, false);

document.addEventListener('DOMContentLoaded', () => {
  Handlebars.registerHelper({
    isEnabled(value) {
      return value.enabled && !value.defaultLibrary;
    },
    isDefaultLibrary(value) {
      return !value.enabled && value.defaultLibrary;
    },
    isBoth(value) {
      return value.enabled && value.defaultLibrary;
    },
    isNone(value) {
      return !value.enabled && !value.defaultLibrary;
    },
  });

  const socket = new SockJS(getContextPath('/gs-guide-websocket'));
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    stompClient.subscribe('/configuration/plex/complete', (message) => {
      const payload = JSON.parse(message.body);

      hideAllAlertsAndSpinners();

      if (payload && payload.code === Payload.PLEX_LIBRARIES_FOUND) {
        // Success
        document.getElementById('plexSaveSuccess').style.display = 'block';

        $('#plexToken').val('');
        $('#address').val('');
        $('#port').val(atob('MzI0MDA='));

        const plexServerCard = $('#plexServerCard').html();
        const theTemplate = Handlebars.compile(plexServerCard);
        const theCompiledHtml = theTemplate(payload.extras);
        $('#plexServers').append(theCompiledHtml);
      } else {
        // Failed
        document.getElementById('plexSaveError').style.display = 'block';
      }
    });
    stompClient.subscribe('/configuration/plex/duplicate', () => {
      hideAllAlertsAndSpinners();
      document.getElementById('plexDuplicateError').style.display = 'block';
    });
  });

  // Exposing function for onClick()
  window.testTmdbKey = testTmdbKey;
  window.saveTmdbKey = saveTmdbKey;
  window.testPlexServer = testPlexServer;
  window.addPlexServer = addPlexServer;
  window.testExistingPlexServer = testExistingPlexServer;
  window.removePlexServer = removePlexServer;
  window.setDeleteAllEnabledOrDisabled = setDeleteAllEnabledOrDisabled;
  window.nuke = nuke;

  // Exposing function for onClick() from module
  window.saveTelegram = saveTelegramNotifications;
  window.testTelegram = testTelegramNotifications;
  window.saveSlack = saveSlackNotifications;
  window.testSlack = testSlackNotifications;
  window.saveDiscord = saveDiscordNotifications;
  window.testDiscord = testDiscordNotifications;
  window.savePushBullet = savePushBulletNotifications;
  window.testPushBullet = testPushBulletNotifications;
  window.testGotify = testGotifyNotifications;
  window.saveGotify = saveGotifyNotifications;
  window.testEmail = testEmailNotifications;
  window.saveEmail = saveEmailNotifications;
  window.testPushOver = testPushOverNotifications;
  window.savePushOver = savePushOverNotifications;
  window.saveSchedule = saveSchedule;
  window.saveMovieStatus = saveMovieStatus;
  window.openPlexLibraryConfigurationModel = openPlexLibraryConfigurationModel;
  window.savePlexLibraryConfiguration = savePlexLibraryConfiguration;

  getSoundOptions();
});

$(() => {
  $('[data-hide]').on('click', function () {
    // $("." + $(this).attr("data-hide")).hide();
    // -or-, see below
    $(this).closest(`.${$(this).attr('data-hide')}`).hide();
  });
});
