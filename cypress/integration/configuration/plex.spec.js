/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, describe, before */
/* eslint no-undef: "error" */

import faker from "@faker-js/faker";
import { BASE_URL, spyOnAddEventListener } from '../common.spec.js';

describe('Plex Configuration Tests', () => {
  before(() => {
    cy.visit(`${BASE_URL}/configuration`, { onBeforeLoad: spyOnAddEventListener });

    cy.get('#plexTab')
      .click();

    // Remove existing server before each test
    cy.get('body').then(($body) => {
      if ($body.find("[onclick=\"removePlexServer(this.getAttribute('data-machineIdentifier'))\"]").length > 0) {
        cy.get("[onclick=\"removePlexServer(this.getAttribute('data-machineIdentifier'))\"]")
          .each(($el) => {
            cy.wrap($el).click();
          });
      }
    });
  });

  it('Test invalid new Plex Server', () => {
    const ip = faker.internet.ip();
    const port = faker.random.number(64000);
    const token = faker.random.alphaNumeric(32);

    cy.populatePlexConfiguration(ip, port, token);

    cy.get('#testPlexServer')
      .click();

    cy.get('#plexSpinner', { timeout: 15000 })
      .should('not.be.visible');

    cy.get('#plexTestError')
      .should('be.visible');

    cy.get('#plexTestSuccess')
      .should('not.be.visible');

    cy.get('#plexSaveError')
      .should('not.be.visible');

    cy.get('#plexSaveSuccess')
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('not.be.visible');

    cy.get('#plexDuplicateError')
      .should('not.be.visible');
  });

  it('Test valid new Plex Server', () => {
    cy.populatePlexConfiguration(atob('MTkyLjE2OC4xLjg='), atob('MzI0MDA='), atob('bVF3NHVhd3hUeVlFbXFOVXJ2Qno='));

    cy.get('#testPlexServer')
      .click();

    cy.get('#plexSpinner')
      .should('not.be.visible');

    cy.get('#plexTestError')
      .should('not.be.visible');

    cy.get('#plexTestSuccess')
      .should('be.visible');

    cy.get('#plexSaveError')
      .should('not.be.visible');

    cy.get('#plexSaveSuccess')
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('not.be.visible');

    cy.get('#plexDuplicateError')
      .should('not.be.visible');
  });

  it('Save invalid Plex Server', () => {
    const ip = faker.internet.ip();
    const port = faker.random.number(64000);
    const token = faker.random.alphaNumeric(32);

    cy.populatePlexConfiguration(ip, port, token);

    cy.get('#addPlexServer')
      .click();

    cy.get('#plexSpinner', { timeout: 15000 })
      .should('not.be.visible');

    cy.get('#plexTestError')
      .should('not.be.visible');

    cy.get('#plexTestSuccess')
      .should('not.be.visible');

    cy.get('#plexSaveError')
      .should('be.visible');

    cy.get('#plexSaveSuccess')
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('not.be.visible');

    cy.get('#plexDuplicateError')
      .should('not.be.visible');
  });

  it('Save valid Plex Server', () => {
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

    // Define card here
    cy.get('.card-header')
      .should('have.text', 'Joker');

    cy.get('.list-group > :nth-child(1)')
      .should('have.text', 'Best Movies');

    cy.get('.list-group > :nth-child(2)')
      .should('have.text', 'Movies');

    cy.get('.list-group > :nth-child(3)')
      .should('have.text', 'Movies with new Metadata');

    cy.get('.list-group > :nth-child(4)')
      .should('have.text', 'Saw');
  });

  it('Test valid existing Plex Server', () => {
    cy.get('#testExisting')
      .click();

    cy.get('#plexSpinner')
      .should('not.be.visible');

    cy.get('#plexTestError')
      .should('not.be.visible');

    cy.get('#plexTestSuccess')
      .should('be.visible');

    cy.get('#plexSaveError')
      .should('not.be.visible');

    cy.get('#plexSaveSuccess')
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('not.be.visible');

    cy.get('#plexDuplicateError')
      .should('not.be.visible');
  });

  it('Remove valid existing Plex Server', () => {
    cy.get('#removeExisting')
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
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('be.visible');

    cy.get('#plexDuplicateError')
      .should('not.be.visible');
  });

  it('Save duplicate valid Plex Server', () => {
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

    // Define card here
    cy.get('.card-header')
      .should('have.text', 'Joker');

    cy.get('.list-group > :nth-child(1)')
      .should('have.text', 'Best Movies');

    cy.get('.list-group > :nth-child(2)')
      .should('have.text', 'Movies');

    cy.get('.list-group > :nth-child(3)')
      .should('have.text', 'Movies with new Metadata');

    cy.get('.list-group > :nth-child(4)')
      .should('have.text', 'Saw');

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
      .should('not.be.visible');

    cy.get('#plexDeleteError')
      .should('not.be.visible');

    cy.get('#plexDeleteSuccess')
      .should('not.be.visible');

    cy.get('#plexDuplicateError')
      .should('be.visible');
  });
});
