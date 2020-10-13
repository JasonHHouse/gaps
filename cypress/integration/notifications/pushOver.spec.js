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

import faker from 'faker';
import { CYPRESS_VALUES, nuke } from '../common.spec.js';

function checkElements(token, user, priority, sound, retry, expire, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#pushOverToken')
    .should('have.value', token);

  cy.get('#pushOverUser')
    .should('have.value', user);

  cy.get('#pushOverPriority')
    .should('have.value', priority);

  cy.get('#pushOverSound')
    .should('have.value', sound);

  cy.get('#pushOverRetry')
    .should('have.value', retry);

  cy.get('#pushOverExpire')
    .should('have.value', expire);

  cy.get('#pushOverTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#pushOverPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#pushOverPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#pushOverPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#pushOverGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#pushOverEnabled')
    .should('have.value', enabled);
}

describe('Check PushOver Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty PushOver Notification Agent Settings', () => {
    cy.request('/notifications/pushOver')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(132);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.token).to.eq('');
        expect(body.extras.user).to.eq('');
        expect(body.extras.priority).to.eq(0);
        expect(body.extras.sound).to.eq('');
        expect(body.extras.retry).to.eq(0);
        expect(body.extras.expire).to.eq(0);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements('', '', 0, 'pushover', '0', '0', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');
      });
  });

  it('Set PushOver Notification Agent Settings', () => {
    const pushOver = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      token: faker.random.alphaNumeric(),
      user: faker.internet.userName(),
      priority: faker.random.number(2),
      sound: 'updown',
      retry: faker.random.number(100),
      expire: faker.random.number(10000),
    };

    cy.request('PUT', '/notifications/pushOver', pushOver)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(130);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/pushOver')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(132);
        expect(body.extras.token).to.eq(pushOver.token);
        expect(body.extras.user).to.eq(pushOver.user);
        expect(body.extras.priority).to.eq(pushOver.priority);
        expect(body.extras.sound).to.eq(pushOver.sound);
        expect(body.extras.retry).to.eq(pushOver.retry);
        expect(body.extras.expire).to.eq(pushOver.expire);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(pushOver.token, pushOver.user, pushOver.priority, pushOver.sound, pushOver.retry, pushOver.expire, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, pushOver.enabled.toString());
      });
  });

  it('Set TMDB Only PushOver Notification Agent Settings', () => {
    const pushOver = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      token: faker.random.alphaNumeric(),
      user: faker.internet.userName(),
      priority: faker.random.number(2),
      sound: 'updown',
      retry: faker.random.number(100),
      expire: faker.random.number(10000),
    };

    cy.request('PUT', '/notifications/pushOver', pushOver)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(130);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/pushOver')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(132);
        expect(body.extras.token).to.eq(pushOver.token);
        expect(body.extras.user).to.eq(pushOver.user);
        expect(body.extras.priority).to.eq(pushOver.priority);
        expect(body.extras.sound).to.eq(pushOver.sound);
        expect(body.extras.retry).to.eq(pushOver.retry);
        expect(body.extras.expire).to.eq(pushOver.expire);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(pushOver.token, pushOver.user, pushOver.priority, pushOver.sound, pushOver.retry, pushOver.expire, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, pushOver.enabled.toString());
      });
  });

  it('Check saving PushOver Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#pushOverShowHide')
      .click();

    checkElements('', '', 0, 'pushover', '0', '0', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#pushOverToken')
      .clear()
      .type('pushOverToken')
      .should('have.value', 'pushOverToken');

    cy.get('#pushOverUser')
      .clear()
      .type('pushOverUser')
      .should('have.value', 'pushOverUser');

    cy.get('#pushOverPriority')
      .select('0')
      .should('have.value', '0');

    cy.get('#pushOverSound')
      .select('cashregister')
      .should('have.value', 'cashregister');

    cy.get('#pushOverRetry')
      .clear()
      .type('1')
      .should('have.value', '1');

    cy.get('#pushOverExpire')
      .clear()
      .type('2')
      .should('have.value', '2');

    cy.get('#pushOverTmdbApiConnectionNotification')
      .click();

    cy.get('#pushOverPlexServerConnectionNotification')
      .click();

    cy.get('#pushOverPlexMetadataUpdateNotification')
      .click();

    cy.get('#pushOverPlexLibraryUpdateNotification')
      .click();

    cy.get('#pushOverGapsMissingCollectionsNotification')
      .click();

    cy.get('#pushOverEnabled')
      .select('Enabled');

    cy.get('#savePushOver')
      .click();

    cy.get('#pushOverTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushOverSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing PushOver Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#pushOverShowHide')
      .click();

    checkElements('', '', 0, 'pushover', '0', '0', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#pushOverToken')
      .clear();

    cy.get('#pushOverUser')
      .clear();

    cy.get('#pushOverRetry')
      .clear();

    cy.get('#pushOverExpire')
      .clear();

    cy.get('#pushOverTmdbApiConnectionNotification')
      .click();

    cy.get('#pushOverPlexServerConnectionNotification')
      .click();

    cy.get('#pushOverPlexMetadataUpdateNotification')
      .click();

    cy.get('#pushOverPlexLibraryUpdateNotification')
      .click();

    cy.get('#pushOverGapsMissingCollectionsNotification')
      .click();

    cy.get('#pushOverEnabled')
      .select('Enabled');

    cy.get('#savePushOver')
      .click();

    cy.get('#pushOverTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushOverSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testPushOver')
      .click();

    cy.get('#pushOverTestError', { timeout: 5000 })
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushOverTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushOverSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
