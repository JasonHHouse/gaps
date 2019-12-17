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
let moviesTable;

$(document).ready(function () {
    moviesTable = $('#movies').DataTable();
});

document.addEventListener('DOMContentLoaded', function () {
    backButton = $('#cancel');
    copyToClipboard = $('#copyToClipboard');
    searchResults = $('#searchResults');
    searchPosition = $('#searchPosition');
    progressContainer = $('#progressContainer');
    searchTitle = $('#searchTitle');
    searchDescription = $('#searchDescription');
    /*
        backButton.click(function () {
            $('#warningModal').modal('open');
        });*/

    setCopyToClipboardEnabled(false);
    copyToClipboard.click(function () {
        CopyToClipboard('searchResults');
        M.toast({html: 'Copied to Clipboard'});
    });

    $('#agree').click(function () {
        //Cancel Search
        $.ajax({
            type: "PUT",
            url: "cancelSearch",
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
    searchResults.html("");
    searchTitle.text("Searching for Movies");
    searchDescription.text("Gaps is looking through your Plex libraries. This could take a while so just sit tight and we'll find all the missing movies for you.");

    const plexSearch = JSON.parse($('#plexSearch').val());

    let plexMovieUrls = [];

    plexSearch.libraries.forEach(function (library) {
        let data = {
            'X-Plex-Token': plexSearch.plexToken
        };

        let encoded = encodeQueryData(data);
        let plexMovieUrl = `http://${plexSearch.address}:${plexSearch.port}/library/sections/${library.key}/all/?${encoded}`;
        plexMovieUrls.push(plexMovieUrl);
    });

    /*for (const library of plexSearch.libraries) {
        let data = {
            'X-Plex-Token': plexSearch.plexToken
        };

        let plexMovieUrl = "http://" + plexSearch.address + ":" + plexSearch.port + "/library/sections/" + library.key + "/all/?" + encodeQueryData(data);
        plexMovieUrls.push(plexMovieUrl);
    }*/

    const gaps = {
        movieDbApiKey: plexSearch.movieDbApiKey,
        writeToFile: true,
        searchFromPlex: true,
        movieUrls: plexMovieUrls
    };

    $.ajax({
        type: "POST",
        url: "submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function () {
            searchTitle.text(`${movieCounter} movies to add to complete your collections`);
            searchDescription.text("Below is everything Gaps found that is missing from your movie collections.");
            progressContainer.hide();
            searchPosition.html('');
            backButton.text('restart');
            setCopyToClipboardEnabled(true);
            disconnect();
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
            searchPosition.html('');
            progressContainer.hide();
            backButton.text('restart');
            setCopyToClipboardEnabled(false);
        }
    });

    showSearchStatus();
}
/*
function buildMovieDiv(movie) {
    return '<div>' + buildMovie(movie) + '</div>';
}

function buildMovie(movie) {
    return `${movie.name} (${movie.year}) from '${movie.collection}'`;
}*/

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/newMovieFound', function (status) {
            const obj = JSON.parse(status.body);
            showSearchStatus(obj);

            if(obj.nextMovie) {
                moviesTable.row.add({
                    "title": obj.nextMovie.name,
                    "year": obj.nextMovie.year,
                    "collection": obj.nextMovie.collection,
                }).draw();
            }
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
    if (!obj) {
        searchResults.html("");
    } else {
        let percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
        searchPosition.html(`<h5>${obj.searchedMovieCount} of ${obj.totalMovieCount} movies searched. ${percentage}% complete.</h5>`);

        // if(obj.nextMovie) {
        //     movieCounter++;
        //     searchResults.append(buildMovieDiv(obj.nextMovie));
        // }
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
