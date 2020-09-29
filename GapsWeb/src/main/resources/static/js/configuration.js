/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global Handlebars, SockJS, Stomp */
/* eslint no-undef: "error" */

import hideAllAlertsAndSpinners from './modules/alerts-manager.min.js';
import saveSchedule from './modules/schedule.min.js';
import Payload from './modules/payload.min.js';
import { saveTelegramNotifications, testTelegramNotifications } from './modules/telegram-notifications.min.js';
import { saveSlackNotifications, testSlackNotifications } from './modules/slack-notifications.min.js';
import { savePushBulletNotifications, testPushBulletNotifications } from './modules/push-bullet-notifications.min.js';
import { saveGotifyNotifications, testGotifyNotifications } from './modules/gotify-notifications.min.js';
import { saveEmailNotifications, testEmailNotifications } from './modules/email-notifications.min.js';
import { saveDiscordNotifications, testDiscordNotifications } from './modules/discord-notifications.min.js';
import { getSoundOptions, savePushOverNotifications, testPushOverNotifications } from './modules/push-over-notifications.min.js';
import { openPlexLibraryConfigurationModel, savePlexLibraryConfiguration } from './modules/plex-configuration.min.js';

let plexSpinner;
let plexSaveSuccess;
let plexSaveError;
let plexTestSuccess;
let plexTestError;
let plexDeleteSuccess;
let plexDeleteError;
let plexDuplicateError;
let tmdbSpinner;
let tmdbSaveSuccess;
let tmdbSaveError;
let tmdbTestSuccess;
let tmdbTestError;
let deleteAllError;
let deleteAllSuccess;

function testTmdbKey() {
  hideAllAlertsAndSpinners();

  if (!$('#tmdbConfiguration')[0].checkValidity()) {
    return false;
  }

  tmdbSpinner.show();

  $.ajax({
    type: 'PUT',
    url: `/configuration/test/tmdbKey/${$('#movieDbApiKey').val()}`,
    contentType: 'application/json',
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.TMDB_KEY_VALID) {
        tmdbTestSuccess.show();
      } else {
        tmdbTestError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      tmdbTestError.show();
    },
  });
  return true;
}

function saveTmdbKey() {
  hideAllAlertsAndSpinners();

  if (!$('#tmdbConfiguration')[0].checkValidity()) {
    return false;
  }

  tmdbSpinner.show();
  $.ajax({
    type: 'POST',
    url: `/configuration/save/tmdbKey/${$('#movieDbApiKey').val()}`,
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.TMDB_KEY_SAVE_SUCCESSFUL) {
        tmdbSaveSuccess.show();
      } else {
        tmdbSaveError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      tmdbSaveError.show();
    },
  });
  return true;
}

function testPlexServer() {
  hideAllAlertsAndSpinners();

  if (!$('#plexConfiguration')[0].checkValidity()) {
    return false;
  }

  plexSpinner.show();

  $.ajax({
    type: 'PUT',
    url: '/configuration/test/plex',
    data: {
      plexToken: $('#plexToken').val(),
      address: $('#address').val(),
      port: $('#port').val(),
    },
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.PLEX_CONNECTION_SUCCEEDED) {
        plexTestSuccess.show();
      } else {
        plexTestError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      plexTestError.show();
    },
  });
  return true;
}

function addPlexServer() {
  hideAllAlertsAndSpinners();

  if (!$('#plexConfiguration')[0].checkValidity()) {
    return false;
  }

  plexSpinner.show();

  $.ajax({
    type: 'POST',
    url: '/configuration/add/plex',
    data: {
      plexToken: $('#plexToken').val(),
      address: $('#address').val(),
      port: $('#port').val(),
    },
    error() {
      hideAllAlertsAndSpinners();
      plexSaveError.show();
    },
  });
  return true;
}

function testExistingPlexServer(machineIdentifier) {
  hideAllAlertsAndSpinners();
  plexSpinner.show();

  $.ajax({
    type: 'PUT',
    url: `/configuration/test/plex/${machineIdentifier}`,
    dataType: 'json',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.success) {
        plexTestSuccess.show();
      } else {
        plexTestError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      plexTestError.show();
    },
  });
}

function removePlexServer(machineIdentifier) {
  hideAllAlertsAndSpinners();
  plexSpinner.show();

  $.ajax({
    type: 'DELETE',
    url: `/configuration/delete/plex/${machineIdentifier}`,
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.success) {
        $(`#${machineIdentifier}`).remove();
        plexDeleteSuccess.show();
      } else {
        plexDeleteError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      plexDeleteError.show();
    },
  });
}

function setDeleteAllEnabledOrDisabled() {
  $('#deleteAll').prop('disabled', !$('#confirmDeleteAll').is(':checked'));
}

function nuke() {
  $.ajax({
    type: 'PUT',
    url: '/nuke',
    success(result) {
      hideAllAlertsAndSpinners();
      if (result && result.code === Payload.NUKE_SUCCESSFUL) {
        deleteAllSuccess.show();
        $('#movieDbApiKey').val('');
        $('#plexServers').html('');
      } else {
        deleteAllError.show();
      }
    },
    error() {
      hideAllAlertsAndSpinners();
      deleteAllError.show();
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

  plexSpinner = $('#plexSpinner');
  plexSaveSuccess = $('#plexSaveSuccess');
  plexSaveError = $('#plexSaveError');
  plexTestSuccess = $('#plexTestSuccess');
  plexTestError = $('#plexTestError');
  plexDeleteSuccess = $('#plexDeleteSuccess');
  plexDeleteError = $('#plexDeleteError');
  tmdbSpinner = $('#tmdbSpinner');
  tmdbSaveSuccess = $('#tmdbSaveSuccess');
  tmdbSaveError = $('#tmdbSaveError');
  tmdbTestSuccess = $('#tmdbTestSuccess');
  tmdbTestError = $('#tmdbTestError');
  plexDuplicateError = $('#plexDuplicateError');

  deleteAllError = $('#deleteAllError');
  deleteAllSuccess = $('#deleteAllSuccess');

  const socket = new SockJS('/gs-guide-websocket');
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    stompClient.subscribe('/configuration/plex/complete', (message) => {
      const payload = JSON.parse(message.body);

      hideAllAlertsAndSpinners();

      if (payload && payload.code === Payload.PLEX_LIBRARIES_FOUND) {
        // Success
        plexSaveSuccess.show();

        $('#plexToken').val('');
        $('#address').val('');
        $('#port').val(atob('MzI0MDA='));

        const plexServerCard = $('#plexServerCard').html();
        const theTemplate = Handlebars.compile(plexServerCard);
        const theCompiledHtml = theTemplate(payload.extras);
        $('#plexServers').append(theCompiledHtml);
      } else {
        // Failed
        plexSaveError.show();
      }
    });
    stompClient.subscribe('/configuration/plex/duplicate', () => {
      hideAllAlertsAndSpinners();
      plexDuplicateError.show();
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
