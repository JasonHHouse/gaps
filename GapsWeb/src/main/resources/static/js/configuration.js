/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

'use strict';

import {Payload} from './modules/payload.min.js';
import {saveTelegramNotifications, testTelegramNotifications} from './modules/telegram-notifications.min.js'
import {saveSlackNotifications, testSlackNotifications} from './modules/slack-notifications.min.js'
import {hideAllAlertsAndSpinners} from "./modules/alerts-manager.min.js";
import {savePushBulletNotifications, testPushBulletNotifications} from "./modules/push-bullet-notifications.min.js";
import {saveGotifyNotifications, testGotifyNotifications} from "./modules/gotify-notifications.min.js";
import {saveEmailNotifications, testEmailNotifications} from "./modules/email-notifications.min.js";
import {saveSchedule} from "./modules/schedule.min.js";
import {getSoundOptions, savePushOverNotifications, testPushOverNotifications} from "./modules/push-over-notifications.min.js";

let plexSpinner, plexSaveSuccess, plexSaveError, plexTestSuccess, plexTestError, plexDeleteSuccess, plexDeleteError,
    plexDuplicateError;
let tmdbSpinner, tmdbSaveSuccess, tmdbSaveError, tmdbTestSuccess, tmdbTestError;
let scheduleSpinner, scheduleSaveSuccess, scheduleSaveError;
let deleteAllError, deleteAllSuccess;

window.addEventListener('load', function () {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    Array.prototype.filter.call(forms, function (form) {
        form.addEventListener('click', function (event) {
            if (form.checkValidity() === false) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}, false);

document.addEventListener('DOMContentLoaded', function () {

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
    scheduleSpinner = $('#scheduleSpinner');
    scheduleSaveSuccess = $('#scheduleSaveSuccess');
    scheduleSaveError = $('#scheduleSaveError');

    deleteAllError = $('#deleteAllError');
    deleteAllSuccess = $('#deleteAllSuccess');

    const socket = new SockJS('/gs-guide-websocket');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/configuration/plex/complete`, function (message) {
            const payload = JSON.parse(message.body);

            hideAllAlertsAndSpinners();

            if (payload && payload.code === Payload.PLEX_LIBRARIES_FOUND) {
                //Success
                plexSaveSuccess.show();

                $('#plexToken').val('');
                $('#address').val('');
                $('#port').val(atob('MzI0MDA='));

                const plexServerCard = $("#plexServerCard").html();
                const theTemplate = Handlebars.compile(plexServerCard);
                const theCompiledHtml = theTemplate(payload.extras);
                $('#plexServers').append(theCompiledHtml);
            } else {
                //Failed
                plexSaveError.show();
            }
        });
        stompClient.subscribe(`/configuration/plex/duplicate`, function () {
            hideAllAlertsAndSpinners();
            plexDuplicateError.show();
        });
    });

    //Exposing function for onClick()
    window.testTmdbKey = testTmdbKey;
    window.saveTmdbKey = saveTmdbKey;
    window.testPlexServer = testPlexServer;
    window.addPlexServer = addPlexServer;
    window.testExistingPlexServer = testExistingPlexServer;
    window.removePlexServer = removePlexServer;
    window.setDeleteAllEnabledOrDisabled = setDeleteAllEnabledOrDisabled;
    window.nuke = nuke;

    //Exposing function for onClick() from module
    window.saveTelegram = saveTelegramNotifications;
    window.testTelegram = testTelegramNotifications;
    window.saveSlack = saveSlackNotifications;
    window.testSlack = testSlackNotifications;
    window.savePushBullet = savePushBulletNotifications;
    window.testPushBullet = testPushBulletNotifications;
    window.testGotify = testGotifyNotifications;
    window.saveGotify = saveGotifyNotifications;
    window.testEmail = testEmailNotifications;
    window.saveEmail = saveEmailNotifications;
    window.testPushOver = testPushOverNotifications;
    window.savePushOver = savePushOverNotifications;
    window.saveSchedule = saveSchedule;

    getSoundOptions();
});

function testTmdbKey() {
    hideAllAlertsAndSpinners();

    if (!$('#tmdbConfiguration')[0].checkValidity()) {
        return false;
    }

    tmdbSpinner.show();

    $.ajax({
        type: "PUT",
        url: `/configuration/test/tmdbKey/${$('#movieDbApiKey').val()}`,
        contentType: "application/json",
        dataType: "json",
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.code === Payload.TMDB_KEY_VALID) {
                tmdbTestSuccess.show();
            } else {
                tmdbTestError.show();
            }
        }, error: function () {
            hideAllAlertsAndSpinners();
            tmdbTestError.show();
        }
    });
}


function saveTmdbKey() {
    hideAllAlertsAndSpinners();

    if (!$('#tmdbConfiguration')[0].checkValidity()) {
        return false;
    }

    tmdbSpinner.show();
    $.ajax({
        type: "POST",
        url: `/configuration/save/tmdbKey/${$('#movieDbApiKey').val()}`,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.code === Payload.TMDB_KEY_SAVE_SUCCESSFUL) {
                tmdbSaveSuccess.show();
            } else {
                tmdbSaveError.show();
            }
        }, error: function () {
            hideAllAlertsAndSpinners();
            tmdbSaveError.show();
        }
    });
}

function testPlexServer() {
    hideAllAlertsAndSpinners();

    if (!$('#plexConfiguration')[0].checkValidity()) {
        return false;
    }

    plexSpinner.show();

    $.ajax({
        type: "PUT",
        url: '/configuration/test/plex',
        data: {
            'plexToken': $('#plexToken').val(),
            'address': $('#address').val(),
            'port': $('#port').val()
        },
        dataType: "json",
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.code === Payload.PLEX_CONNECTION_SUCCEEDED) {
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

function addPlexServer() {
    hideAllAlertsAndSpinners();

    if (!$('#plexConfiguration')[0].checkValidity()) {
        return false;
    }

    plexSpinner.show();

    $.ajax({
        type: "POST",
        url: '/configuration/add/plex',
        data: {
            'plexToken': $('#plexToken').val(),
            'address': $('#address').val(),
            'port': $('#port').val()
        },
        error: function () {
            hideAllAlertsAndSpinners();
            plexSaveError.show();
        }
    });
}

function testExistingPlexServer(machineIdentifier) {
    hideAllAlertsAndSpinners();
    plexSpinner.show();

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

function removePlexServer(machineIdentifier) {
    hideAllAlertsAndSpinners();
    plexSpinner.show();

    $.ajax({
        type: "DELETE",
        url: `/configuration/delete/plex/${machineIdentifier}`,
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.success) {
                $('#' + machineIdentifier).remove();
                plexDeleteSuccess.show();
            } else {
                plexDeleteError.show();
            }
        },
        error: function () {
            hideAllAlertsAndSpinners();
            plexDeleteError.show();
        }
    });
}

function setDeleteAllEnabledOrDisabled() {
    $('#deleteAll').prop("disabled", !$('#confirmDeleteAll').is(":checked"));
}

function nuke() {
    $.ajax({
        type: 'PUT',
        url: '/nuke',
        success: function (result) {
            hideAllAlertsAndSpinners();
            if (result && result.code === Payload.NUKE_SUCCESSFUL) {
                deleteAllSuccess.show();
                $('#movieDbApiKey').val('');
                $('#plexServers').html('');
            } else {
                deleteAllError.show();
            }
        },
        error: function () {
            hideAllAlertsAndSpinners();
            deleteAllError.show();
        }
    });
}

$(function () {
    $("[data-hide]").on("click", function () {
        //$("." + $(this).attr("data-hide")).hide();
        // -or-, see below
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
});
