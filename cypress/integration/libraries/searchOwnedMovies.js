/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, expect, before, describe */
/* eslint no-undef: "error" */

import { nuke, redLibraryBefore, spyOnAddEventListener } from '../common.js';

describe('Find owned movies', () => {
  before(nuke);
  before(redLibraryBefore);

  it('Find Saw Movies', () => {
    cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy=Saw]')
      .click();

    cy.get('[data-cy=searchForMovies]')
      .click();

    cy.get('label > input')
      .clear()
      .type('Saw');

    cy.get('#movies_info')
      .should('have.text', 'Showing 1 to 1 of 1 entries');
  });

  it('Refresh Saw Movies', () => {
    cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy="Best Movies"]')
      .click();

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy=Saw]')
      .click();

    cy.get('label > input')
      .clear()
      .type('Saw');

    cy.get('#movies_info')
      .should('have.text', 'Showing 1 to 1 of 1 entries');

    cy.get('.card-img')
      .should('be.visible')
      .and(($img) => {
        // "naturalWidth" and "naturalHeight" are set when the image loads
        expect($img[0].naturalWidth).to.be.greaterThan(0);
      });
  });

  it('Research Saw Movies', () => {
    cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy=Saw]')
      .click();

    cy.get('label > input')
      .clear()
      .type('Saw');

    cy.get('#movies_info')
      .should('have.text', 'Showing 1 to 1 of 1 entries');

    cy.get('#movieContainer > .top-margin')
      .click();

    cy.get('#movies_info')
      .should('have.text', 'Showing 1 to 1 of 1 entries');

    cy.get('.card-img')
      .should('be.visible')
      .and(($img) => {
        // "naturalWidth" and "naturalHeight" are set when the image loads
        expect($img[0].naturalWidth).to.be.greaterThan(0);
      });
  });

  it('Movies with Metadata Empty', () => {
    cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=dropdownMenu]')
      .click();

    cy.get('[data-cy="Movies with new Metadata"]')
      .click();

    cy.get('[data-cy=searchForMovies]')
      .should('be.visible');
  });
});
