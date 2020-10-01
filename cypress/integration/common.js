/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, expect */
/* eslint no-undef: "error" */

export const CYPRESS_VALUES = {
  notBeChecked: 'not.be.checked',
  beChecked: 'be.checked',
  notBeVisible: 'not.be.visible',
  beVisible: 'be.visible',
};

export function spyOnAddEventListener(win) {
  const winObject = Object.assign(win);
  // win = window object in our application
  const addListener = winObject.EventTarget.prototype.addEventListener;
  winObject.EventTarget.prototype.addEventListener = function eventListener(name) {
    if (name === 'change') {
      // restore the original event listener
      winObject.EventTarget.prototype.addEventListener = addListener;
    }
    return addListener.apply(this, arguments);
  };
}

export function searchPlexForMoviesFromSaw(cy) {
  cy.get('#dropdownMenuLink')
    .click();

  cy.get('[data-key="2"]')
    .first()
    .click();

  cy.get('.card-body > .btn')
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
}

export function searchPlexForMoviesFromBestMovies(cy) {
  cy.get('#dropdownMenuLink')
    .click();

  cy.get('[data-key="5"]')
    .first()
    .click();

  cy.get('.card-body > .btn')
    .click();

  // Wait for timeout from clearing data
  cy.wait(5000);
}

export function searchPlexForMoviesFromMovies(cy) {
  cy.get('#dropdownMenuLink')
    .click();

  cy.get('[data-key="1"]')
    .first()
    .click();

  cy.get('.card-body > .btn')
    .click();

  cy.get('label > input')
    .clear()
    .type('Gods');

  cy.get('#movies_info')
    .should('have.text', 'Showing 1 to 1 of 1 entries (filtered from 21 total entries)');
}

export function nuke() {
  cy.request('PUT', '/nuke')
    .then((response) => {
      expect(response.body).to.have.property('code', 30);
      expect(response.body).to.have.property('reason', 'Nuke successful. All files deleted.');
    });

  // Wait for timeout from clearing data
  cy.wait(1000);
}

export function redLibraryBefore() {
  nuke();

  cy.visit('/configuration', { onBeforeLoad: spyOnAddEventListener });

  cy.get('#movieDbApiKey')
    .clear()
    .type('723b4c763114904392ca441909aa0375')
    .should('have.value', '723b4c763114904392ca441909aa0375');

  cy.get('#saveTmdbKey')
    .click();

  cy.get('#plexTab')
    .click();

  cy.get('#address')
    .clear()
    .type(atob('MTkyLjE2OC4xLjk='))
    .should('have.value', atob('MTkyLjE2OC4xLjk='));

  cy.get('#port')
    .clear()
    .type(atob('MzI0MDA='))
    .should('have.value', atob('MzI0MDA='));

  cy.get('#plexToken')
    .clear()
    .type(atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='))
    .should('have.value', atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='));

  cy.get('#addPlexServer')
    .click();

  // Wait for timeout from plex
  cy.wait(10000);

  cy.get('#plexSpinner')
    .should('not.be.visible');

  cy.get('#plexTestError')
    .should('not.be.visible');

  cy.get('#plexTestSuccess')
    .should('not.be.visible');

  cy.get('#plexSaveError')
    .should('not.be.visible');

  cy.get('#plexSaveSuccess')
    .should('be.visible');

  cy.get('#plexDeleteError')
    .should('not.be.visible');

  cy.get('#plexDeleteSuccess')
    .should('not.be.visible');

  cy.get('#plexDuplicateError')
    .should('not.be.visible');

  // Define card here
  cy.get('.card-header')
    .should('have.text', 'Red');

  cy.get('.list-group > :nth-child(1)')
    .should('have.text', 'Best Movies');

  cy.get('.list-group > :nth-child(2)')
    .should('have.text', 'Movies with new Metadata');

  cy.get('.list-group > :nth-child(3)')
    .should('have.text', 'Saw');
}

export function jokerLibraryBefore() {
  cy.visit('/configuration', { onBeforeLoad: spyOnAddEventListener });

  cy.get('#movieDbApiKey')
    .clear()
    .type('723b4c763114904392ca441909aa0375')
    .should('have.value', '723b4c763114904392ca441909aa0375');

  cy.get('#saveTmdbKey')
    .click();

  cy.get('#plexTab')
    .click();

  cy.get('#address')
    .clear()
    .type('192.168.1.8')
    .should('have.value', '192.168.1.8');

  cy.get('#port')
    .clear()
    .type(atob('MzI0MDA='))
    .should('have.value', atob('MzI0MDA='));

  cy.get('#plexToken')
    .clear()
    .type(atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='))
    .should('have.value', atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='));

  cy.get('#addPlexServer')
    .click();

  cy.get('#plexSpinner')
    .should('not.be.visible');

  cy.get('#plexTestError')
    .should('not.be.visible');

  cy.get('#plexTestSuccess')
    .should('not.be.visible');

  cy.get('#plexSaveError')
    .should('not.be.visible');

  cy.get('#plexSaveSuccess')
    .should('be.visible');

  cy.get('#plexDeleteError')
    .should('not.be.visible');

  cy.get('#plexDeleteSuccess')
    .should('not.be.visible');

  cy.get('#plexDuplicateError')
    .should('not.be.visible');
}
