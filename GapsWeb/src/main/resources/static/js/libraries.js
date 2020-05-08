/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import {getOwnedMoviesForTable} from '/js/modules/common.min.js';

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
        deferRender: true,
        ordering: false,
        columns: [
            {data: 'imdbId'},
            {data: 'name'},
            {data: 'year'},
            {data: 'language'},
            {data: 'overview'}
        ],
        columnDefs: [
            {
                targets: [0],
                type: 'html',
                searchable: false,
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
                targets: [1, 2, 3, 4],
                visible: false
            }
        ]
    });

    getOwnedMoviesForTable(`/libraries/${plexServer.machineIdentifier}/${libraryKey}`, movieContainer, noMovieContainer, moviesTable);

    //Exposing function for onClick()
    window.searchForMovies = searchForMovies;
    window.switchPlexLibrary = switchPlexLibrary;
});

function switchPlexLibrary(machineIdentifier, key) {
    libraryKey = key;
    plexServer = plexServers[machineIdentifier];
    const plexLibrary = plexServer.plexLibraries.find(plexServer => plexServer.key === parseInt(key));
    libraryTitle.text(`${plexServer.friendlyName} - ${plexLibrary.title}`);

    moviesTable.data().clear();
    moviesTable.rows().invalidate().draw();

    getOwnedMoviesForTable(`/libraries/${machineIdentifier}/${libraryKey}`, movieContainer, noMovieContainer, moviesTable);
}

function searchForMovies() {
    movieSearchingContainer.show();
    noMovieContainer.css({'display': 'none'});
    moviesTable.data().clear();

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