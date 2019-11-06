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

let stompClient;
let backButton;
let copyToClipboard;
let searchResults;
let searchPosition;
let progressContainer;
let searchTitle;
let searchDescription;
let movieCounter;

document.addEventListener('DOMContentLoaded', function () {
    const elements = document.querySelectorAll('.modal');
    M.Modal.init(elements);

    backButton = $('#cancel');
    copyToClipboard = $('#copyToClipboard');
    searchResults = $('#searchResults');
    searchPosition = $('#searchPosition');
    progressContainer = $('#progressContainer');
    searchTitle = $('#searchTitle');
    searchDescription = $('#searchDescription');

    backButton.click(function () {
        $('#warningModal').modal('open');
    });

    setCopyToClipboardEnabled(false);
    copyToClipboard.click(function () {
        CopyToClipboard('searchResults');
        M.toast({ html: 'Copied to Clipboard' });
    });

    $('#agree').click(function () {
        //Cancel Search
        $.ajax({
            type: "PUT",
            url: "https://" + location.hostname + ":" + location.port + "/cancelSearch",
            contentType: "application/json",
        });

        //Navigate Home
        location.assign("index.html");
    });

    connect();
    search();
});

window.onbeforeunload = function () {
    disconnect();
};

function setCopyToClipboardEnabled(bool) {
    if (bool) {
        copyToClipboard.removeClass('disabled');
    } else {
        copyToClipboard.addClass('disabled');
    }
}

function search() {

    //reset movie counter;
    movieCounter = 0;

    progressContainer.show();
    searchTitle.text("Searching for Movies...");
    searchDescription.text("Gaps is looking through your Plex libraries. This could take a while so just sit tight and we'll find all the missing movies for you.");

    const libraries = JSON.parse(Cookies.get('libraries'));
    const address = Cookies.get('address');
    const port = Cookies.get('port');
    const plexToken = Cookies.get('plex_token');
    const movieDbApiKey = Cookies.get('movie_db_api_key');

    let plexMovieUrls = [];

    for (const library of libraries) {
        let data = {
            'X-Plex-Token': plexToken
        };

        let plexMovieUrl = "http://" + address + ":" + port + "/library/sections/" + library.key + "/all/?" + encodeQueryData(data);
        plexMovieUrls.push(plexMovieUrl);
    }

    const gaps = {
        movieDbApiKey: movieDbApiKey,
        writeToFile: true,
        searchFromPlex: true,
        movieUrls: plexMovieUrls
    };

    $.ajax({
        type: "POST",
        url: "https://" + location.hostname + ":" + location.port + "/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function (movies) {
            searchResults.html(buildMoviesDiv(movies));

            movieCounter = movies.length;

            searchTitle.text(`${movieCounter} movies to add to complete your collections`);
            searchDescription.text("Below is everything Gaps found that is missing from your movie collections.");
            progressContainer.hide();
            backButton.text('restart');
            setCopyToClipboardEnabled(true);
        },
        error: function (err) {
            disconnect();
            let message = "Unknown error. Check docker Gaps log file.";
            if (err) {
                message = JSON.parse(err.responseText).message;
                console.error(message);
            }

            searchTitle.text("An error occurred...");
            searchDescription.text("");
            progressContainer.hide();
            backButton.text('restart');
            setCopyToClipboardEnabled(false);
        }
    });

    showSearchStatus();
}

function buildMoviesDiv(movies) {
    let result = '';

    for (let movie of movies) {
        result += buildMovieDiv(movie);
    }

    return result;
}

function buildMovieDiv(movie) {
    return '<div>' + buildMovie(movie) + '</div>';
}

function buildMovie(movie) {
    return `${movie.name} (${movie.year}) from '${movie.collection}'`;
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/currentSearchResults', function (status) {
            let obj = JSON.parse(status.body);
            showSearchStatus(obj);
            shouldDisconnect(obj)
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function shouldDisconnect(obj) {
    if (obj && obj.searchedMovieCount && obj.totalMovieCount && obj.totalMovieCount === obj.searchedMovieCount) {
        disconnect();

        //Cancel Search
        $.ajax({
            type: "PUT",
            url: "http://" + location.hostname + ":" + location.port + "/cancelSearch",
            contentType: "application/json",
        });
    }
}

function showSearchStatus(obj) {
    if (!obj || (!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0)) {
        searchResults.html("");
    } else {
        let percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
        searchPosition.html(`<h5>${obj.searchedMovieCount} of ${obj.totalMovieCount} movies searched. ${percentage}% complete.</h5>`);

        movieCounter = obj.moviesFound.length;

        searchResults.html(buildMoviesDiv(obj.moviesFound));
    }
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data) {
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    }
    return ret.join('&');
}

function CopyToClipboard(containerId) {
    let range;
    if (document.selection) {
        range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerId));
        range.select().createTextRange();
        document.execCommand("copy");

    } else if (window.getSelection) {
        range = document.createRange();
        range.selectNode(document.getElementById(containerId));
        window.getSelection().addRange(range);
        document.execCommand("copy");
    }
}