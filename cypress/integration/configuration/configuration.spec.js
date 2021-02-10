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

import { BASE_URL, nuke, spyOnAddEventListener } from '../common.spec.js';

describe('Configuration Tests', () => {
  it('Clean configuration page load', () => {
    nuke();

    cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

    cy.get('.active > .nav-link')
      .should('have.attr', 'href', `${BASE_URL}/configuration`)
      .parent()
      .should('have.attr', 'aria-current', 'page');

    cy.get('.navbar-nav > :nth-child(1) > .nav-link')
      .should('have.attr', 'href', `${BASE_URL}/libraries`)
      .parent()
      .should('not.have.attr', 'aria-current', 'page');

    cy.get('.navbar-nav > :nth-child(2) > .nav-link')
      .should('have.attr', 'href', `${BASE_URL}/recommended`)
      .parent()
      .should('not.have.attr', 'aria-current', 'page');

    cy.get('.navbar-nav > :nth-child(4) > .nav-link')
      .should('have.attr', 'href', `${BASE_URL}/rssCheck`)
      .parent()
      .should('not.have.attr', 'aria-current', 'page');

    cy.get('.navbar-nav > :nth-child(7) > .nav-link')
      .should('have.attr', 'href', `${BASE_URL}/about`)
      .parent()
      .should('not.have.attr', 'aria-current', 'page');

    cy.get('#tmdbTab')
      .should('have.class', 'active');

    cy.get('#plexTab')
      .should('not.have.class', 'active');

    cy.get('#scheduleTab')
      .should('not.have.class', 'active');

    cy.get('#folderTab')
      .should('have.class', 'disabled');

    cy.get('#tmdbTestError')
      .should('not.be.visible');

    cy.get('#tmdbTestSuccess')
      .should('not.be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('not.be.visible');
  });
});
