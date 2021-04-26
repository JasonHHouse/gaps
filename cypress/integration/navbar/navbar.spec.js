/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, describe */
/* eslint no-undef: "error" */

import { BASE_URL, spyOnAddEventListener } from '../common.spec.js';

function checkNavIcons(cy, isLibDefault = true, isMissingDefault = true, isRssDefault = true, isSettingsDefault = true, isUpdatesDefault = true, isAboutDefault = true) {
  cy.get('[data-cy=librariesIcon]') // ToDo Update Icon data-cy below
    .should('have.class', isLibDefault ? 'default' : 'activePage');

  cy.get('[data-cy=recommendedIcon]')
    .should('have.class', isMissingDefault ? 'default' : 'activePage');

  cy.get('[data-cy=rssCheckIcon]')
    .should('have.class', isRssDefault ? 'default' : 'activePage');

  cy.get('[data-cy=configurationIcon]')
    .should('have.class', isSettingsDefault ? 'default' : 'activePage');

  cy.get('[data-cy=updatesIcon]')
    .should('have.class', isUpdatesDefault ? 'default' : 'activePage');

  cy.get('[data-cy=aboutIcon]')
    .should('have.class', isAboutDefault ? 'default' : 'activePage');
}

describe('Verify Navbars', () => {
  it('Index page', () => {
    cy.visit(`${BASE_URL}/home`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy);
  });

  it('About page', () => {
    cy.visit(`${BASE_URL}/about`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, undefined, undefined, undefined, undefined, undefined, false);
  });

  it('Settings page', () => {
    cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, undefined, undefined, undefined, false, undefined, undefined);
  });

  it('Libraries page', () => {
    cy.visit(`${BASE_URL}/libraries`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, false, undefined, undefined, undefined, undefined, undefined);
  });

  it('Recommended page', () => {
    cy.visit(`${BASE_URL}/recommended`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, undefined, false, undefined, undefined, undefined, undefined);
  });

  it('RSS page', () => {
    cy.visit(`${BASE_URL}/rssCheck`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, undefined, undefined, false, undefined, undefined, undefined);
  });

  it('Updates page', () => {
    cy.visit(`${BASE_URL}/updates`, { onBeforeLoad: spyOnAddEventListener });

    checkNavIcons(cy, undefined, undefined, undefined, undefined, false, undefined);
  });
});
