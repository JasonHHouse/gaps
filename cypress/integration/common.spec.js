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

export const BASE_URL = '';
// export const BASE_URL = '/gaps';

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

  cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

  cy.populateTmdb('723b4c763114904392ca441909aa0375');

  cy.get('#saveTmdbKey')
    .click();

  cy.get('#plexTab')
    .click();

  cy.populatePlexConfiguration(atob('MTkyLjE2OC4xLjg='), atob('MzI0MDA='), atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='));

  cy.get('#addPlexServer', { timeout: 15000 })
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

  // Define card here
  cy.get('[data-cy=Joker]')
    .should('have.text', 'Joker');

  cy.get('[data-cy="Best Movies"]')
    .should('have.text', 'Best Movies');

  cy.get('[data-cy="Movies"]')
    .should('have.text', 'Movies');

  cy.get('[data-cy="Movies with new Metadata"]')
    .should('have.text', 'Movies with new Metadata');

  cy.get('[data-cy=Saw]')
    .should('have.text', 'Saw');
}

export function jokerLibraryBefore() {
  cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

  cy.populateTmdb('723b4c763114904392ca441909aa0375');

  cy.get('#saveTmdbKey')
    .click();

  cy.get('#plexTab')
    .click();

  cy.populatePlexConfiguration(atob('MTkyLjE2OC4xLjg='), atob('MzI0MDA='), atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='));

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
