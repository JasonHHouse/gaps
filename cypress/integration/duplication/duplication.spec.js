/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, describe, before, expect */
/* eslint no-undef: "error" */

import { redLibraryBefore, searchPlexForMoviesFromBestMovies, spyOnAddEventListener } from '../common.js';

function checkForDuplicates(ownedMovies, recommendedMovies) {
  cy.log(`recommendedMovies.length: ${recommendedMovies.length}`);
  cy.log(`ownedMovies.length: ${ownedMovies.length}`);

  Object.values(recommendedMovies).forEach((recommendedMovie) => {
    Object.values(ownedMovies).forEach((ownedMovie) => {
      if (recommendedMovie.tvdbId === ownedMovie.tvdbId
        || recommendedMovie.imdbId === ownedMovie.imdbId) {
        cy.log(`Recommended Movie: ${recommendedMovie}`);
        cy.log(`Owned Movie: ${ownedMovie}`);
      }

      if ((recommendedMovie.tvdbId !== undefined && ownedMovie.tvdbId !== undefined)) {
        expect(recommendedMovie.tvdbId).to.not.eq(ownedMovie.tvdbId);
      }

      if ((recommendedMovie.imdbId !== undefined && ownedMovie.imdbId !== undefined)) {
        expect(recommendedMovie.imdbId).to.not.eq(ownedMovie.imdbId);
      }
    });
  });
}

function searchBestMovieLibrary(cy) {
  cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

  searchPlexForMoviesFromBestMovies(cy);

  cy.visit('/recommended', { onBeforeLoad: spyOnAddEventListener });
}

function waitUtilSearchingIsDone() {
  cy.request('/searchStatus')
    .then((resp) => {
      if (resp.status === 200 && resp.body.isSearching === false) {
        return;
      }
      // else recurse
      waitUtilSearchingIsDone();
    });
}

describe('Search for Duplicates', () => {
  before(redLibraryBefore);

  it('Check Best Movies and Recommended for Duplicates', () => {
    searchBestMovieLibrary(cy);

    cy.get('#dropdownMenuLink')
      .click();

    cy.get('[data-key="5"]')
      .first()
      .click();

    cy.get('.card-body > .btn')
      .click();

    waitUtilSearchingIsDone();

    let ownedMovies;
    cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/5')
      .then((resp) => {
        ownedMovies = resp.body;
        cy.log(`ownedMovies.length: ${ownedMovies.length}`);
      }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/5')
      .then((resp) => {
        const recommendedMovies = resp.body.extras;
        checkForDuplicates(ownedMovies, recommendedMovies);
      });
  });

  it('Check Saw and Recommended for Duplicates', () => {
    waitUtilSearchingIsDone();

    let ownedMovies;
    cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
      .then((resp) => {
        ownedMovies = resp.body;
        cy.log(`ownedMovies.length: ${ownedMovies.length}`);
      }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
      .then((resp) => {
        const recommendedMovies = resp.body.extras;
        checkForDuplicates(ownedMovies, recommendedMovies);
      });
  });

  it('Check Movies and Recommended for Duplicates', () => {
    waitUtilSearchingIsDone();

    let ownedMovies;
    cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
      .then((resp) => {
        ownedMovies = resp.body;
      }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
      .then((resp) => {
        const recommendedMovies = resp.body.extras;
        checkForDuplicates(ownedMovies, recommendedMovies);
      });
  });
});
