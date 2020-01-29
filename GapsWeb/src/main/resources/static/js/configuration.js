/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

document.addEventListener('DOMContentLoaded', function () {

    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/configuration/plex/complete`, function (message) {
            const plexSpinner = $('#plexSpinner');
            const plexSaveSuccess = $('#plexSaveSuccess');
            const plexSaveError = $('#plexSaveError');

            const body = message.body;

            plexSpinner.hide();
            if (body) {
                const response = JSON.parse(body);
                if (response.failed) {
                    plexSaveError.show();
                } else {
                    plexSaveSuccess.show();

                    const plexServerCard = $("#plexServerCard").html();
                    const theTemplate = Handlebars.compile(plexServerCard);
                    const theCompiledHtml = theTemplate(response);
                    $('#plexServers').append(theCompiledHtml);
                }
            } else {
                plexSaveError.show();
            }
        });
    });
});

function testTmdbKey() {
    const tmdbSpinner = $('#tmdbSpinner');
    const tmdbSaveSuccess = $('#tmdbSaveSuccess');
    const tmdbSaveError = $('#tmdbSaveError');
    const tmdbTestSuccess = $('#tmdbTestSuccess');
    const tmdbTestError = $('#tmdbTestError');

    tmdbTestSuccess.hide();
    tmdbTestError.hide();
    tmdbSaveSuccess.hide();
    tmdbSaveError.hide();
    tmdbSpinner.show();

    $.ajax({
        type: "PUT",
        url: `/configuration/test/tmdbKey/${$('#movieDbApiKey').val()}`,
        contentType: "application/json",
        dataType: "json",
        success: function (result) {
            tmdbSpinner.hide();
            if (result && result.success) {
                tmdbTestSuccess.show();
            } else {
                tmdbTestError.show();
            }
        }, error: function () {
            tmdbSpinner.hide();
            tmdbTestError.show();
        }
    });
}

function saveTmdbKey() {
    const tmdbSpinner = $('#tmdbSpinner');
    const tmdbSaveSuccess = $('#tmdbSaveSuccess');
    const tmdbSaveError = $('#tmdbSaveError');
    const tmdbTestSuccess = $('#tmdbTestSuccess');
    const tmdbTestError = $('#tmdbTestError');

    tmdbTestSuccess.hide();
    tmdbTestError.hide();
    tmdbSaveSuccess.hide();
    tmdbSaveError.hide();
    tmdbSpinner.show();

    $.ajax({
        type: "POST",
        url: `/configuration/save/tmdbKey/${$('#movieDbApiKey').val()}`,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            tmdbSpinner.hide();
            if (result && result.success) {
                tmdbSaveSuccess.show();
            } else {
                tmdbSaveError.show();
            }
        }, error: function () {
            tmdbSpinner.hide();
            tmdbSaveError.show();
        }
    });
}

function testPlexServer() {
    const plexSpinner = $('#plexSpinner');
    const plexSaveSuccess = $('#plexSaveSuccess');
    const plexSaveError = $('#plexSaveError');
    const plexTestSuccess = $('#plexTestSuccess');
    const plexTestError = $('#plexTestError');

    plexTestSuccess.hide();
    plexTestError.hide();
    plexSaveSuccess.hide();
    plexSaveError.hide();
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
            plexSpinner.hide();
            if (result && result.success) {
                plexTestSuccess.show();
            } else {
                plexTestError.show();
            }
        }, error: function () {
            plexSpinner.hide();
            plexTestError.show();
        }
    });
}

function addPlexServer() {
    const plexSpinner = $('#plexSpinner');
    const plexSaveSuccess = $('#plexSaveSuccess');
    const plexSaveError = $('#plexSaveError');
    const plexTestSuccess = $('#plexTestSuccess');
    const plexTestError = $('#plexTestError');

    plexTestSuccess.hide();
    plexTestError.hide();
    plexSaveSuccess.hide();
    plexSaveError.hide();
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
            plexSpinner.hide();
            plexSaveError.show();
        }
    });
}

function testExistingPlexServer(machineIdentifier) {
    console.log("testExistingPlexServer( " + machineIdentifier + " )");
}

function removePlexServer(machineIdentifier) {
    console.log("removePlexServer( " + machineIdentifier + " )");
}

$(function () {
    $("[data-hide]").on("click", function () {
        //$("." + $(this).attr("data-hide")).hide();
        // -or-, see below
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
});