/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

let libraryTitle, noMovieContainer, movieContainer, movieSearchingContainer;
let plexServers;
let plexServer;
let moviesTable;
let libraryKey;


jQuery(function ($) {
    Handlebars.registerHelper('json', function (context) {
        return JSON.stringify(context);
    });

    libraryTitle = $('#libraryTitle');
    noMovieContainer = $('#noMovieContainer');
    movieContainer = $('#movieContainer');
    movieSearchingContainer = $('#movieSearchingContainer');
    plexServers = JSON.parse($('#plexServers').val());
    plexServer = JSON.parse($('#plexServer').val());
    libraryKey = $('#libraryKey').val();

    moviesTable = $('#movies').DataTable({
        initComplete: function () {
            getMoviesForTable(`/libraries/${plexServer.machineIdentifier}/${libraryKey}`);
        },
        deferRender: true,
        search: true,
        columns: [
            {
                data: "card",
                render: function (data, type, row) {
                    if (type === 'display') {
                        row.address = plexServer.address;
                        row.port = plexServer.port;
                        row.plexToken = plexServer.plexToken;

                        const plexServerCard = $("#movieCard").html();
                        const theTemplate = Handlebars.compile(plexServerCard);
                        return theTemplate(row);
                    }
                    return "";
                }
            },
            {
                data: "title",
                searchable: true,
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
                searchable: true,
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
                searchable: true,
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.language) {
                        return row.language;
                    }
                    return "";
                }
            },
            {
                data: "summary",
                searchable: true,
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.overview) {
                        return row.overview;
                    }
                    return "";
                }
            },
        ]
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
                noMovieContainer.css({'display': 'none'});
                moviesTable.rows.add(JSON.parse(result.movies)).draw();
            } else {
                movieContainer.css({'display': 'none'});
                noMovieContainer.show(100);
            }
        }, error: function () {
            movieContainer.css({'display': 'none'});
            noMovieContainer.show(100);
            //Show error + error
        }
    });
}

function searchForMovies() {
    movieSearchingContainer.show();
    noMovieContainer.css({'display': 'none'});

    $.ajax({
        type: "GET",
        url: `/plex/movies/${plexServer.machineIdentifier}/${libraryKey}`,
        contentType: "application/json",
        success: function (data) {
            movieSearchingContainer.css({'display': 'none'});
            moviesTable.rows.add(data).draw();
            movieContainer.show(100);
        }
    });
}