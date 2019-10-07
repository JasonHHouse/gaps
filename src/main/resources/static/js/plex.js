/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
"use strict";

let keepChecking;
let stompClient;
let modal;

document.addEventListener('DOMContentLoaded', function () {
    M.AutoInit();

    const elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        onCloseEnd: function () {
            $.ajax({
                type: "PUT",
                url: "http://" + $('#address').val() + ":" + $('#port').val() + "/cancelSearch",
                contentType: "application/json",
                timeout: 0
            });
        }
    });

    M.Modal.getInstance($('#searchModal'));

    console.debug(document.cookie);
});

function onSubmitGapsSearch() {
    if (validateInput()) {
        search();
    }
}


function search() {
    connect();

    keepChecking = true;
    let searchModelTitle = $('#searchModelTitle');
    let progressContainer = $('#progressContainer');
    let searchingBody = $('#searchingBody');
    let modelButton = $('#modelButton');

    progressContainer.hide();
    modelButton.text('cancel');
    searchModelTitle.text("Searching");
    searchingBody.empty();

    const gaps = {
        movieDbApiKey: $('#movie_db_api_key').val(),
        writeToFile: true,
        movieDbListId: $('#movie_db_list_id').val(),
        searchFromPlex: true,
        connectTimeout: $('#connect_timeout').val(),
        writeTimeout: $('#write_timeout').val(),
        readTimeout: $('#read_timeout').val(),
        movieUrls: $('#plex_movie_urls').val().split("\n")
    }

    $.ajax({
        type: "POST",
        url: "http://" + $('#address').val() + ":" + $('#port').val() + "/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function (movies) {
            keepChecking = false;
            let movieHtml = "";
            movies.forEach(function (movie) {
                movieHtml += buildMovieDiv(movie);
            });

            progressContainer.hide();
            searchingBody.html(buildMovies(movieHtml));
            searchModelTitle.text(movies.length + ' movies to add to complete your collections');
            modelButton.text('close');

            disconnect();
        },
        error: function (err) {
            let message = "Unknown error. Check docker Gaps log file.";
            if (err) {
                message = JSON.parse(err.responseText).message;
            }

            progressContainer.hide();
            searchingBody.html(message);
            searchModelTitle.text("An error occurred...");
            modelButton.text('close');

            keepChecking = false;

            disconnect();
        }
    });

    $('#searchModal').modal('open');

    polling();
}

function buildMovies(html) {
    return '<ul class="collection">' + html + '</ul>';
}

function buildMovieDiv(movie) {
    return '<li class="collection-item">' + buildMovie(movie) + '</li>';
}

function buildMovie(movie) {
    return movie.name + " (" + movie.year + ") from '" + movie.collection + "'";
}

function polling() {
    if (keepChecking) {
        $.ajax({
            type: "GET",
            url: "http://" + $('#address').val() + ":" + $('#port').val() + "/status",
            contentType: "application/json",
            success: function (data) {

            }
        })
    }
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/searchResults', function (status) {
            showSearchStatus(JSON.parse(status.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showSearchStatus(obj) {
    if (keepChecking) {
        if (!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0) {
            $('#searchingBody').text("Searching for movies...");
        } else {
            $('#progressContainer').show();
            const percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
            $('#searchingBody').text(obj.searchedMovieCount + ' of ' + obj.totalMovieCount + " movies searched. " + percentage + "% complete. "  + obj.moviesFound);
            $('#progressBar').css("width", percentage + "%");
        }
    }
}
