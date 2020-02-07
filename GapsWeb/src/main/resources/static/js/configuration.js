/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

let plexSpinner, plexSaveSuccess, plexSaveError, plexTestSuccess, plexTestError, plexDeleteSuccess, plexDeleteError,
    plexDuplicateError;
let tmdbSpinner, tmdbSaveSuccess, tmdbSaveError, tmdbTestSuccess, tmdbTestError;

window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    Array.prototype.filter.call(forms, function(form) {
        form.addEventListener('click', function(event) {
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

    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/configuration/plex/complete`, function (message) {
            const payload = JSON.parse(message.body);

            plexSpinner.hide();

            if(payload && payload.code === 13) {
                //Success
                plexSaveSuccess.show();

                $('#plexToken').val('');
                $('#address').val('');
                $('#port').val('32400');

                const plexServerCard = $("#plexServerCard").html();
                const theTemplate = Handlebars.compile(plexServerCard);
                const theCompiledHtml = theTemplate(payload.extras);
                $('#plexServers').append(theCompiledHtml);
            } else {
                //Failed
                plexSaveError.show();
            }

          /*  if (payload) {
                if (response.failed) {
                    plexSaveError.show();
                } else {
                    plexSaveSuccess.show();

                    $('#plexToken').val('');
                    $('#address').val('');
                    $('#port').val('32400');

                    const plexServerCard = $("#plexServerCard").html();
                    const theTemplate = Handlebars.compile(plexServerCard);
                    const theCompiledHtml = theTemplate(payload);
                    $('#plexServers').append(theCompiledHtml);
                }
            } else {
                plexSaveError.show();
            }*/
        });
        stompClient.subscribe(`/configuration/plex/duplicate`, function () {
            plexSpinner.hide();
            plexDuplicateError.show();
        });
    });
});

function testTmdbKey() {
    hideAllAlerts();

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
            tmdbSpinner.hide();
            if (result && result.code === 20) {
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
    hideAllAlerts();

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
            tmdbSpinner.hide();
            if (result && result.code === 23) {
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
    hideAllAlerts();

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
            plexSpinner.hide();
            if (result && result.code === 10) {
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
    hideAllAlerts();

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
            plexSpinner.hide();
            plexSaveError.show();
        }
    });
}

function testExistingPlexServer(machineIdentifier) {
    hideAllAlerts();
    plexSpinner.show();

    $.ajax({
        type: "PUT",
        url: `/configuration/test/plex/${machineIdentifier}`,
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

function removePlexServer(machineIdentifier) {
    hideAllAlerts();
    plexSpinner.show();

    $.ajax({
        type: "DELETE",
        url: `/configuration/delete/plex/${machineIdentifier}`,
        success: function (result) {
            plexSpinner.hide();
            if (result && result.success) {
                $('#' + machineIdentifier).remove();
                plexDeleteSuccess.show();
            } else {
                plexDeleteError.show();
            }
        },
        error: function () {
            plexSpinner.hide();
            plexDeleteError.show();
        }
    });
}

function hideAllAlerts() {
    //Spinners
    plexSpinner.hide();
    tmdbSpinner.hide();

    //Plex Alerts
    plexTestSuccess.hide();
    plexTestError.hide();
    plexSaveSuccess.hide();
    plexSaveError.hide();
    plexDeleteSuccess.hide();
    plexDeleteError.hide();

    //TMDB Alerts
    tmdbTestSuccess.hide();
    tmdbTestError.hide();
    tmdbSaveSuccess.hide();
    tmdbSaveError.hide();
}

$(function () {
    $("[data-hide]").on("click", function () {
        //$("." + $(this).attr("data-hide")).hide();
        // -or-, see below
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
});