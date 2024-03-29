/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, describe, before, expect */
/* eslint no-undef: "error" */

import { BASE_URL, spyOnAddEventListener } from '../common.spec.js';

describe('Schedule Configuration Tests', () => {
  before(() => {
    cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

    cy.get('#scheduleTab')
      .click();
  });

  it('Attempt to save empty monthly schedule', () => {
    cy.get('#setSchedule')
      .select('Monthly');

    cy.get('#saveSchedule')
      .click();

    cy.get('#scheduleSaveSuccess')
      .should('be.visible');

    cy.get('#scheduleSaveError')
      .should('not.be.visible');

    cy.request(`${BASE_URL}/schedule`)
      .then((resp) => {
        const result = resp.body;
        expect(result).to.have.property('message', 'Monthly');
      });
  });

  it('Attempt to save empty hourly schedule', () => {
    cy.get('#setSchedule')
      .select('Hourly');

    cy.get('#saveSchedule')
      .click();

    cy.get('#scheduleSaveSuccess')
      .should('be.visible');

    cy.get('#scheduleSaveError')
      .should('not.be.visible');

    cy.request(`${BASE_URL}/schedule`)
      .then((resp) => {
        const result = resp.body;
        expect(result).to.have.property('message', 'Hourly');
      });
  });
});
