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

import { spyOnAddEventListener } from '../common.spec.js';

describe('Verify Log In Page', () => {
  it('Default page', () => {
    cy.visit('/login', { onBeforeLoad: spyOnAddEventListener });

    cy.get(':nth-child(1) > label')
      .should('have.text', 'Username');

    cy.get(':nth-child(2) > label')
      .should('have.text', 'Password');

    cy.get('.btn')
      .should('have.text', 'Log In');

    cy.get('#username')
      .clear()
      .type('badUser')
      .should('have.value', 'badUser');

    cy.get('#password')
      .clear()
      .type('password')
      .should('have.value', 'password');

    cy.get('.btn')
      .click();

    cy.get('.card-img-top')
      .should('be.visible');
  });
});
