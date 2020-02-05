/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

$(document).ready(function () {
    moviesTable = $('#movies').DataTable();
});

/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

let libraryTitle, noMovieContainer, movieContainer, movieSearchContainer;
let plexServers;
let plexServer;
let moviesTable;
let libraryKey;

let stompClient;
let backButton;
let copyToClipboard;
let searchResults;
let progressContainer;
let searchTitle;
let searchDescription;
let movieCounter;

jQuery(function ($) {
    Handlebars.registerHelper('json', function (context) {
        return JSON.stringify(context);
    });

    libraryTitle = $('#libraryTitle');
    noMovieContainer = $('#noMovieContainer');
    movieContainer = $('#movieContainer');
    plexServers = JSON.parse($('#plexServers').val());
    plexServer = JSON.parse($('#plexServer').val());
    libraryKey = $('#libraryKey').val();

    backButton = $('#cancel');
    copyToClipboard = $('#copyToClipboard');
    searchResults = [];
    movieSearchContainer = $('#movieSearchContainer');
    progressContainer = $('#progressContainer');
    searchTitle = $('#searchTitle');
    searchDescription = $('#searchDescription');

    moviesTable = $('#movies').DataTable({
        "initComplete": function (settings, json) {
            getMoviesForTable(`/libraries/${plexServer.machineIdentifier}/${libraryKey}`);
        },
        ordering: false,
        columns: [
            {
                data: "card",
                render: function (data, type, row) {
                    if (type === 'display') {
                        const plexServerCard = $("#movieCard").html();
                        const theTemplate = Handlebars.compile(plexServerCard);
                        return theTemplate(row);
                    }
                    return "";
                }
            },
            {
                data: "title",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.name) {
                        return row.name;
                    }
                    return "";
                }
            },
            {
                data: "year",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.year) {
                        return row.year;
                    }
                    return "";
                }
            },
            {
                data: "language",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.language) {
                        return row.language;
                    }
                    return "";
                }
            },
            {
                data: "collection",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.collection) {
                        return row.collection;
                    }
                    return "";
                }
            },
        ],
        select: {
            style: 'os',
            selector: 'td:not(:last-child)' // no row selection on last column
        },
        rowCallback: function (row, data) {
            // Set the checked state of the checkbox in the table
            $('input.editor-active', row).prop('checked', data.active == 1);
        }
    });


    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {

        stompClient.subscribe('/finishedSearching', function (successful) {
            progressContainer.hide();
            backButton.text('Restart');
            disconnect();
            if (successful) {
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
                movieCounter++;
                moviesTable.row.add(obj.nextMovie).draw();
                searchResults.push(`${obj.nextMovie.name} (${obj.nextMovie.year}) in collection '${obj.nextMovie.collection}'`)
            }
        });
    });

});

function switchPlexLibrary(machineIdentifier, key) {
    libraryKey = key;
    plexServer = plexServers[machineIdentifier];
    const plexLibrary = plexServer.plexLibraries.find(plexServer => plexServer.key === parseInt(key));
    libraryTitle.text(`${plexServer.friendlyName} - ${plexLibrary.title}`);

    moviesTable.data().clear();
    moviesTable.rows().invalidate().draw();

    getMoviesForTable(`/libraries/${machineIdentifier}/${libraryKey}`);
}

function getMoviesForTable(url) {
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            if (result.success) {
                movieContainer.show(100);
                noMovieContainer.css({'display':'none'});
                moviesTable.rows.add(JSON.parse(result.movies)).draw();
            } else {
                movieContainer.css({'display':'none'});
                noMovieContainer.show(100);
            }
        }, error: function () {
            movieContainer.css({'display':'none'});
            noMovieContainer.show(100);
            //Show error + error
        }
    });
}

function cancel() {
    stompClient.send("/cancelSearching");

    //Navigate Home
    location.assign("/");
}

function viewRss() {
    location.assign("rssCheck");
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

function searchForMovies() {
    movieContainer.show(100);
    movieSearchContainer.show(100);
    noMovieContainer.css({'display':'none'});

    //reset movie counter;
    movieCounter = 0;
    progressContainer.show();
    searchTitle.text("Searching for Movies");
    searchDescription.text("Gaps is looking through your Plex libraries. This could take a while so just sit tight, and we'll find all the missing movies for you.");

    $.ajax({
        type: "PUT",
        url: `/search/start/${plexServer.machineIdentifier}/${libraryKey}`,
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
