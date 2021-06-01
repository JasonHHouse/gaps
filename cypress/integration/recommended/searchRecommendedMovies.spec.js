/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, describe, expect, it, beforeEach */
/* eslint no-undef: "error" */

import {
  BASE_URL, nuke, redLibraryBefore, spyOnAddEventListener,
} from '../common.spec.js';

function searchSawLibrary(cy) {
  cy.visit(`${BASE_URL}/libraries`, { onBeforeLoad: spyOnAddEventListener });

  cy.get('[data-cy=dropdownMenu]')
    .click();

  cy.get('[data-cy=Saw]')
    .click();

  cy.get('[data-cy=searchForMovies]')
    .click();

  cy.get('label > input')
    .clear()
    .type('Saw');

  cy.get('[data-cy=Horror]')
    .should('have.text', 'Horror');

  cy.get('[data-cy=Crime]')
    .should('have.text', 'Crime');

  cy.get('#movies_info')
    .should('have.text', 'Showing 1 to 1 of 1 entries');

  cy.visit(`${BASE_URL}/recommended`, { onBeforeLoad: spyOnAddEventListener });
}

describe('Search for Recommended', () => {
  beforeEach(nuke);
  beforeEach(redLibraryBefore);

  it('Clean configuration page load', () => {
    searchSawLibrary(cy);

    cy.get('[data-cy=libraryTitle]')
      .then(($libraryTitle) => {
        if ($libraryTitle.text() !== 'Joker - Saw') {
          cy.get('[data-cy=dropdownMenu]')
            .click();

          cy.get('[data-cy=Saw]')
            .click();
        }
      });

    cy.get('#noMovieContainer > .card > .card-img-top')
      .should('not.be.visible');
  });

  it('Search Movies', () => {
    searchSawLibrary(cy);

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy=Saw]')
      .click();

    cy.get('[data-cy=searchForMovies]')
      .click();
    
    cy.get('[data-cy="backdropPath-tt0432348"]')
      .should('be.visible')
      .and(($img) => {
        // "naturalWidth" and "naturalHeight" are set when the image loads
        expect($img[0].naturalWidth).to.be.greaterThan(0);
      });

    cy.get('[data-cy=posterUrl-tt0432348]')
      .should('be.visible')
      .and(($img) => {
        // "naturalWidth" and "naturalHeight" are set when the image loads
        expect($img[0].naturalWidth).to.be.greaterThan(0);
      });

    cy.scrollTo(0, 1000);

    cy.get('#movies_info', { timeout: 5000 })
      .should('have.text', 'Showing 1 to 7 of 7 entries');

    cy.get('[data-cy=tt0432348-176]')
      .should('have.text', 'Saw');

    cy.get('[data-cy=tt0432348-215]')
      .should('have.text', 'Saw II');
  });

  it('Research Movies', () => {
    searchSawLibrary(cy);

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy=Saw]')
      .click();

    cy.get('[data-cy=searchForMovies]')
      .click();

    cy.get('#movies_info', { timeout: 5000 })
      .should('have.text', 'Showing 1 to 7 of 7 entries');

    cy.get('[data-cy=tt0432348-176]')
      .should('have.text', 'Saw');

    cy.get('[data-cy=tt0432348-215]')
      .should('have.text', 'Saw II');

    cy.get('#movieContainer > [onclick="searchForMovies()"]')
      .click();

    cy.get('#movies_info', { timeout: 5000 })
      .should('have.text', 'Showing 1 to 7 of 7 entries');

    cy.get('[data-cy=tt0432348-176]')
      .should('have.text', 'Saw');

    cy.get('[data-cy=tt0432348-215]')
      .should('have.text', 'Saw II');
  });
});
