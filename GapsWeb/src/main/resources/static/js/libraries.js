/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global Handlebars */
/* eslint no-undef: "error" */

import { getOwnedMoviesForTable } from './modules/common.min.js';

let libraryTitle;
let noMovieContainer;
let movieContainer;
let movieSearchingContainer;
let plexServers;
let moviesTable;

function switchPlexLibrary(machineIdentifier, key) {
  const plexServer = plexServers[machineIdentifier];
  const plexLibrary = plexServer.plexLibraries.find((tempPlexLibrary) => tempPlexLibrary.key === parseInt(key, 10));
  libraryTitle.text(`${plexServer.friendlyName} - ${plexLibrary.title}`);
  libraryTitle.setAttribute('data-machineIdentifier', machineIdentifier);
  libraryTitle.setAttribute('data-key', key);

  moviesTable.data().clear();
  moviesTable.rows().invalidate().draw();

  getOwnedMoviesForTable(`/libraries/${machineIdentifier}/${key}`, movieContainer, noMovieContainer, moviesTable);
}

function searchForMovies() {
  movieSearchingContainer.show();
  noMovieContainer.css({ display: 'none' });
  moviesTable.data().clear();
  moviesTable.rows().invalidate().draw();

  const machineIdentifier = libraryTitle.getAttribute('data-machineIdentifier');
  const key = libraryTitle.getAttribute('data-key');

  $.ajax({
    type: 'GET',
    url: `/plex/movies/${machineIdentifier}/${key}`,
    contentType: 'application/json',
    success(data) {
      movieSearchingContainer.css({ display: 'none' });
      moviesTable.rows.add(data).draw();
      movieContainer.show(100);
    },
  });
}

jQuery(($) => {
  libraryTitle = $('#libraryTitle');
  noMovieContainer = $('#noMovieContainer');
  movieContainer = $('#movieContainer');
  movieSearchingContainer = $('#movieSearchingContainer');
  plexServers = JSON.parse($('#plexServers').val());
  const plexServer = JSON.parse($('#plexServer').val());
  const key = JSON.parse($('#libraryKey').val());

  moviesTable = $('#movies').DataTable({
    deferRender: true,
    ordering: false,
    columns: [
      { data: 'imdbId' },
      { data: 'name' },
      { data: 'year' },
      { data: 'language' },
      { data: 'overview' },
    ],
    columnDefs: [
      {
        targets: [0],
        type: 'html',
        searchable: false,
        render(data, type, row) {
          if (type === 'display') {
            const plexServerData = Object.assign(row);
            plexServerData.address = plexServer.address;
            plexServerData.port = plexServer.port;
            plexServerData.plexToken = plexServer.plexToken;

            const plexServerCard = $('#movieCard').html();
            const theTemplate = Handlebars.compile(plexServerCard);
            return theTemplate(plexServerData);
          }
          return '';
        },
      },
      {
        targets: [1, 2, 3, 4],
        visible: false,
      },
    ],
  });

  getOwnedMoviesForTable(`/libraries/${plexServer.machineIdentifier}/${key}`, movieContainer, noMovieContainer, moviesTable);

  // Exposing function for onClick()
  window.searchForMovies = searchForMovies;
  window.switchPlexLibrary = switchPlexLibrary;
});
