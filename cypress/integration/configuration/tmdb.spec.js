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

import faker from 'faker';
import { spyOnAddEventListener } from '../common.spec.js';

describe('TMDB Configuration Tests', () => {
  before(() => {
    cy.visit('/configuration', { onBeforeLoad: spyOnAddEventListener });
  });
  it('Test invalid TMDB Key', () => {
    const invalidKey = faker.random.alphaNumeric(16);

    cy.populateTmdb(invalidKey);

    cy.get('#testTmdbKey')
      .click();

    cy.get('#tmdbTestError', { timeout: 5000 })
      .should('be.visible');

    cy.get('#tmdbTestSuccess')
      .should('not.be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('not.be.visible');
  });

  it('Test valid TMDB Key', () => {
    cy.populateTmdb('723b4c763114904392ca441909aa0375');

    cy.get('#testTmdbKey')
      .click();

    cy.get('#tmdbTestError')
      .should('not.be.visible');

    cy.get('#tmdbTestSuccess')
      .should('be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('not.be.visible');
  });

  it('Save invalid TMDB Key', () => {
    const invalidKey = faker.random.alphaNumeric(16);

    cy.populateTmdb(invalidKey);

    cy.get('#saveTmdbKey')
      .click();

    cy.get('#tmdbTestError')
      .should('not.be.visible');

    cy.get('#tmdbTestSuccess')
      .should('not.be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('be.visible');

    cy.visit('/configuration', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=movieDbApiKey]')
      .should('have.value', invalidKey);
  });

  it('Save valid TMDB Key', () => {
    cy.populateTmdb('723b4c763114904392ca441909aa0375');

    cy.get('#saveTmdbKey')
      .click();

    cy.get('#tmdbTestError')
      .should('not.be.visible');

    cy.get('#tmdbTestSuccess')
      .should('not.be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('be.visible');

    cy.visit('/configuration', { onBeforeLoad: spyOnAddEventListener });

    cy.get('[data-cy=movieDbApiKey]')
      .should('have.value', '723b4c763114904392ca441909aa0375');
  });

  it('Attempt to save empty TMDB Key', () => {
    cy.populateTmdb();

    cy.get('#saveTmdbKey')
      .click();

    cy.get('#emptyTmdbKeyLabel')
      .should('be.visible');

    cy.get('#tmdbTestError')
      .should('not.be.visible');

    cy.get('#tmdbTestSuccess')
      .should('not.be.visible');

    cy.get('#tmdbSaveError')
      .should('not.be.visible');

    cy.get('#tmdbSaveSuccess')
      .should('not.be.visible');
  });

  it('Attempt to test empty TMDB Key', () => {
    cy.populateTmdb('');

    cy.get('#testTmdbKey')
      .click();

    cy.get('#emptyTmdbKeyLabel')
      .should('be.visible');

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
