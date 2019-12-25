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
    searchResults = [];
    progressContainer = $('#progressContainer');
    searchTitle = $('#searchTitle');
    searchDescription = $('#searchDescription');

    setCopyToClipboardEnabled(false);
    copyToClipboard.click(function () {
        copy(searchResults);
        $('#copiedToClipboard').show();
    });

    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe( '/finishedSearching', function (successful) {
            progressContainer.hide();
            backButton.text('Restart');
            disconnect();
            if(successful) {
                searchTitle.text(`Search Complete`);
                searchDescription.text(`${movieCounter} movies to add to complete your collections. Below is everything Gaps found that is missing from your movie collections.`);
                setCopyToClipboardEnabled(true);
            } else {
                searchTitle.text("Search Failed");
                searchDescription.text("Unknown error. Check docker Gaps log file.");
                setCopyToClipboardEnabled(false);
            }
        });

        stompClient.subscribe('/newMovieFound', function (status) {
            const obj = JSON.parse(status.body);
            showSearchStatus(obj);

            function buildUrl(nextMovie) {
                if (nextMovie.tvdbId) {
                    return `https://www.themoviedb.org/movie/${nextMovie.tvdbId}`;
                }

                if (nextMovie.imdbId) {
                    return `https://www.imdb.com/title/${nextMovie.imdbId}/`
                }

                return undefined;
            }

            if (obj.nextMovie) {
                const url = buildUrl(obj.nextMovie);
                let link;
                if (url) {
                    link = `<a target="_blank" href="${url}">Details</a>`;
                } else {
                    link = "No URL found";
                }

                moviesTable.row.add([
                    obj.nextMovie.name,
                    obj.nextMovie.year,
                    obj.nextMovie.collection,
                    link
                ]).draw();

                searchResults.push(`${obj.nextMovie.name} (${obj.nextMovie.year}) in collection '${obj.nextMovie.collection}'`)
            }
        });
    });

    search();
});

function cancel() {
    stompClient.send("/cancelSearching");

    //Navigate Home
    location.assign("/");
}

function viewRss(){
    location.assign("rss");
}

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
    searchTitle.text("Searching for Movies");
    searchDescription.text("Gaps is looking through your Plex libraries. This could take a while so just sit tight, and we'll find all the missing movies for you.");

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

    const gaps = {
        movieDbApiKey: plexSearch.movieDbApiKey,
        writeToFile: true,
        searchFromPlex: true,
        movieUrls: plexMovieUrls
    };

    $.ajax({
        type: "POST",
        url: "startSearching",
        data: JSON.stringify(gaps),
        contentType: "application/json"
    });

    showSearchStatus();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showSearchStatus(obj) {
    if (!obj) {
        searchDescription.html("");
    } else {
        let percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
        searchDescription.html(`${obj.searchedMovieCount} of ${obj.totalMovieCount} movies searched. ${percentage}% complete.`);
    }
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data) {
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    }
    return ret.join('&');
}

function copy(arr) {
    const stringified = arr.join('\r\n');
    $('<input>').val(stringified).appendTo('body').select();
    document.execCommand('copy');
}
