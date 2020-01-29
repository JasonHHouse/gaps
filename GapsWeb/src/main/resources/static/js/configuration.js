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
            const plexServer = JSON.parse(message.body);

            const plexServerCard = $("#plexServerCard").html();
            const theTemplate = Handlebars.compile(plexServerCard);
            const theCompiledHtml = theTemplate(plexServer);
            $('#plexServers').append(theCompiledHtml);
        });
    });
});

function addPlexServer() {
    $.ajax({
        type: "POST",
        url: '/configuration/plex',
        data: {
            'plexToken': $('#plexToken').val(),
            'address': $('#address').val(),
            'port': $('#port').val()
        },
        dataType: 'application/json'
    });
}

function testTmdbKey() {
    const movieDbApiKey = $('#movieDbApiKey').val();

    $.ajax({
        type: "POST",
        url: `/configuration/testTmdbKey/${movieDbApiKey}`,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            if (result && result.success) {
                $('#tmdbSuccess').show();
            } else {
                $('#tmdbError').show();
            }
        }, error: function () {
            $('#tmdbError').show();
        }
    });
}

function updateTmdbKey() {

}

$(function(){
    $("[data-hide]").on("click", function(){
        //$("." + $(this).attr("data-hide")).hide();
        // -or-, see below
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
});