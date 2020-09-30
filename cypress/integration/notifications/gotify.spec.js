/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, describe, it, beforeEach, expect */
/* eslint no-undef: "error" */

import { CYPRESS_VALUES, nuke } from '../common.js';
import faker from "faker";

function checkElements(address, token, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#gotifyAddress')
    .should('have.value', address);

  cy.get('#gotifyToken')
    .should('have.value', token);

  cy.get('#gotifyTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#gotifyPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#gotifyPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#gotifyPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#gotifyGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#gotifyEnabled')
    .should('have.value', enabled);
}

describe('Check Gotify Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty Gotify Notification Agent Settings', () => {
    cy.request('/notifications/gotify')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(112);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.address).to.eq('');
        expect(body.extras.token).to.eq('');
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#gotifyAddress')
          .should('have.value', '');

        cy.get('#gotifyToken')
          .should('have.value', '');

        cy.get('#gotifyTmdbApiConnectionNotification')
          .should('not.be.checked');

        cy.get('#gotifyPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#gotifyPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#gotifyPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#gotifyGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#gotifyEnabled')
          .should('have.value', 'false');
      });
  });

  it('Set Gotify Notification Agent Settings', () => {
    const gotify = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      address: faker.random.alphaNumeric(),
      token: faker.random.alphaNumeric(),
    };

    cy.request('PUT', '/notifications/gotify', gotify)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(110);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/gotify')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(112);
        expect(body.extras.address).to.eq(gotify.address);
        expect(body.extras.token).to.eq(gotify.token);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#gotifyAddress')
          .should('have.value', gotify.address);

        cy.get('#gotifyToken')
          .should('have.value', gotify.token);

        cy.get('#gotifyTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#gotifyPlexServerConnectionNotification')
          .should('be.checked');

        cy.get('#gotifyPlexMetadataUpdateNotification')
          .should('be.checked');

        cy.get('#gotifyPlexLibraryUpdateNotification')
          .should('be.checked');

        cy.get('#gotifyGapsMissingCollectionsNotification')
          .should('be.checked');

        cy.get('#gotifyEnabled')
          .should('have.value', gotify.enabled.toString());
      });
  });

  it('Set TMDB Only Gotify Notification Agent Settings', () => {
    const gotify = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      address: faker.random.alphaNumeric(),
      token: faker.random.alphaNumeric(),
    };

    cy.request('PUT', '/notifications/gotify', gotify)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(110);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/gotify')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(112);
        expect(body.extras.address).to.eq(gotify.address);
        expect(body.extras.token).to.eq(gotify.token);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#gotifyAddress')
          .should('have.value', gotify.address);

        cy.get('#gotifyToken')
          .should('have.value', gotify.token);

        cy.get('#gotifyTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#gotifyPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#gotifyPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#gotifyPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#gotifyGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#gotifyEnabled')
          .should('have.value', gotify.enabled.toString());
      });
  });

  it('Check saving Gotify Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#gotifyShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    const address = faker.random.alphaNumeric();
    const token = faker.random.alphaNumeric();

    cy.get('#gotifyAddress')
      .clear()
      .type(address)
      .should('have.value', address);

    cy.get('#gotifyToken')
      .clear()
      .type(token)
      .should('have.value', token);

    cy.get('#gotifyTmdbApiConnectionNotification')
      .click();

    cy.get('#gotifyPlexServerConnectionNotification')
      .click();

    cy.get('#gotifyPlexMetadataUpdateNotification')
      .click();

    cy.get('#gotifyPlexLibraryUpdateNotification')
      .click();

    cy.get('#gotifyGapsMissingCollectionsNotification')
      .click();

    cy.get('#gotifyEnabled')
      .select('Enabled');

    cy.get('#saveGotify')
      .click();

    cy.get('#gotifyTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifyTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#gotifySaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check saving and test Gotify Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#gotifyShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#gotifyAddress')
      .clear()
      .type(atob('aHR0cDovLzE5Mi4xNjguMS44OjgwNzA='))
      .should('have.value', atob('aHR0cDovLzE5Mi4xNjguMS44OjgwNzA='));

    cy.get('#gotifyToken')
      .clear()
      .type(atob('QVJkbS55SHRQUTlhaW5i'))
      .should('have.value', atob('QVJkbS55SHRQUTlhaW5i'));

    cy.get('#gotifyTmdbApiConnectionNotification')
      .click();

    cy.get('#gotifyPlexServerConnectionNotification')
      .click();

    cy.get('#gotifyPlexMetadataUpdateNotification')
      .click();

    cy.get('#gotifyPlexLibraryUpdateNotification')
      .click();

    cy.get('#gotifyGapsMissingCollectionsNotification')
      .click();

    cy.get('#gotifyEnabled')
      .select('Enabled');

    cy.get('#saveGotify')
      .click();

    cy.get('#gotifyTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifyTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#gotifySaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testGotify')
      .click();

    cy.wait(2000);

    cy.get('#gotifyTestSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#gotifyTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing Gotify Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#gotifyShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#gotifyAddress')
      .clear();

    cy.get('#gotifyToken')
      .clear();

    cy.get('#gotifyTmdbApiConnectionNotification')
      .click();

    cy.get('#gotifyPlexServerConnectionNotification')
      .click();

    cy.get('#gotifyPlexMetadataUpdateNotification')
      .click();

    cy.get('#gotifyPlexLibraryUpdateNotification')
      .click();

    cy.get('#gotifyGapsMissingCollectionsNotification')
      .click();

    cy.get('#gotifyEnabled')
      .select('Enabled');

    cy.get('#saveGotify')
      .click();

    cy.get('#gotifyTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifyTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#gotifySaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testGotify')
      .click();

    cy.wait(2000);

    cy.get('#gotifyTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifyTestError')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#gotifySaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#gotifySpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
