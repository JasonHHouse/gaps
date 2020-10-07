/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, describe, it, expect */
/* eslint no-undef: "error" */

import { redLibraryBefore, spyOnAddEventListener } from '../common.js';

function searchSawLibrary(cy) {
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

  cy.visit('/recommended', { onBeforeLoad: spyOnAddEventListener });
}

describe('Recommended API', () => {
  it('Get Bad recommended', () => {
    cy.request('/recommended/a/2')
      .then((resp) => {
        cy.log(resp.body);
        const result = resp.body;
        expect(result.code).to.eq(41);
      });
  });

  it('Get library Red - Saw', () => {
    redLibraryBefore();
    searchSawLibrary(cy);

    cy.request('/libraries/c51c432ae94e316d52570550f915ecbcd71bede8/5')
      .then((resp) => {
        cy.log(resp.body);
        const result = resp.body;
        expect(result.code).to.eq(40);
      });
  });
});
