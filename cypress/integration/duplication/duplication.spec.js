/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, describe, beforeEach, expect */
/* eslint no-undef: "error" */

import {
  BASE_URL, nuke, redLibraryBefore, spyOnAddEventListener,
} from '../common.spec.js';

function checkForDuplicates(ownedMovies, recommendedMovies) {
  cy.log(`recommendedMovies.length: ${recommendedMovies.length}`);
  cy.log(`ownedMovies.length: ${ownedMovies.length}`);

  Object.values(recommendedMovies).forEach((recommendedMovie) => {
    Object.values(ownedMovies).forEach((ownedMovie) => {
      if (recommendedMovie.tmdbId === ownedMovie.tmdbId
        || recommendedMovie.imdbId === ownedMovie.imdbId) {
        cy.log(`Recommended Movie: ${recommendedMovie}`);
        cy.log(`Owned Movie: ${ownedMovie}`);
      }

      if ((recommendedMovie.tmdbId !== undefined && ownedMovie.tmdbId !== undefined)) {
        expect(recommendedMovie.tmdbId).to.not.eq(ownedMovie.tmdbId);
      }

      if ((recommendedMovie.imdbId !== undefined && ownedMovie.imdbId !== undefined)) {
        expect(recommendedMovie.imdbId).to.not.eq(ownedMovie.imdbId);
      }
    });
  });
}

function searchBestMovieLibrary(cy) {
  cy.visit(`${BASE_URL}/libraries`, { onBeforeLoad: spyOnAddEventListener });

  cy.get('[data-cy=dropdownMenu]')
    .click();

  cy.get('[data-cy="Best Movies"]')
    .click();

  cy.get('[data-cy=searchForMovies]')
    .click();

  // Wait for timeout from clearing data
  cy.wait(5000);

  cy.visit(`${BASE_URL}/recommended`, { onBeforeLoad: spyOnAddEventListener });

  cy.get('[data-cy=dropdownMenu]')
    .click();

  cy.get('[data-cy="Best Movies"]')
    .click();

  cy.get('[data-cy=searchForMovies]')
    .click();
}

function searchMovieWithNewMetadataLibrary(cy) {
  cy.visit(`${BASE_URL}/libraries`, { onBeforeLoad: spyOnAddEventListener });

  cy.get('[data-cy=dropdownMenu]')
    .click();

  cy.get('[data-cy="Movies with new Metadata"]')
    .click();

  cy.get('[data-cy=searchForMovies]')
    .click();

  // Wait for timeout to load page
  cy.wait(5000);

  cy.get('label > input')
    .clear()
    .type('God');

  cy.get('#movies_info')
    .contains('Showing 1 to 1 of 1 entries');

  cy.visit(`${BASE_URL}/recommended`, { onBeforeLoad: spyOnAddEventListener });
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
  beforeEach(nuke);
  beforeEach(redLibraryBefore);

  it('Check Best Movies and Recommended for Duplicates', () => {
    searchBestMovieLibrary(cy);

    waitUtilSearchingIsDone();

    let ownedMovies;
    cy.request('/plex/movies/c51c432ae94e316d52570550f915ecbcd71bede8/1')
      .then((resp) => {
        ownedMovies = resp.body;
        cy.log(`ownedMovies.length: ${ownedMovies.length}`);
      }).request('/recommended/c51c432ae94e316d52570550f915ecbcd71bede8/1')
      .then((resp) => {
        const recommendedMovies = resp.body.extras;
        checkForDuplicates(ownedMovies, recommendedMovies);
      });
  });

  it('Check Movies with new Metatdata and Recommended for Duplicates', () => {
    searchMovieWithNewMetadataLibrary(cy);

    waitUtilSearchingIsDone();

    let ownedMovies;
    cy.request('/plex/movies/c51c432ae94e316d52570550f915ecbcd71bede8/4')
      .then((resp) => {
        ownedMovies = resp.body;
      }).request('/recommended/c51c432ae94e316d52570550f915ecbcd71bede8/4')
      .then((resp) => {
        const recommendedMovies = resp.body.extras;
        checkForDuplicates(ownedMovies, recommendedMovies);
      });
  });
});
