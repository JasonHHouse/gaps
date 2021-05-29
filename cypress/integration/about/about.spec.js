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

describe('Verify About Page', () => {
  it('Default page', () => {
    cy.visit(`${BASE_URL}/about`, { onBeforeLoad: spyOnAddEventListener });

    cy.get('h3.top-margin')
      .should('have.text', 'About');

    cy.get('.container > :nth-child(3)')
      .should('have.text', 'v0.9.1');

    cy.get('.container > :nth-child(6)')
      .should('have.text', 'Software');

    cy.get(':nth-child(8)')
      .should('have.text', 'License');

    cy.get(':nth-child(10)')
      .should('have.text', 'Support');
  });
});
