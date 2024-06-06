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

import faker from "@faker-js/faker";
import { BASE_URL, CYPRESS_VALUES, nuke } from '../common.spec.js';

function checkElements(channelTag, token, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#pushBulletChannelTag')
    .should('have.value', channelTag);

  cy.get('#pushBulletAccessToken')
    .should('have.value', token);

  cy.get('#pushBulletTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#pushBulletPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#pushBulletPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#pushBulletPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#pushBulletGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#pushBulletEnabled')
    .should('have.value', enabled);
}

describe('Check PushBullet Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty PushBullet Notification Agent Settings', () => {
    cy.request(`${BASE_URL}/notifications/pushbullet`)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(102);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.channel_tag).to.eq('');
        expect(body.extras.accessToken).to.eq('');
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#pushBulletChannelTag')
          .should('have.value', '');

        cy.get('#pushBulletAccessToken')
          .should('have.value', '');

        cy.get('#pushBulletTmdbApiConnectionNotification')
          .should('not.be.checked');

        cy.get('#pushBulletPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#pushBulletPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#pushBulletPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#pushBulletGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#pushBulletEnabled')
          .should('have.value', 'false');
      });
  });

  it('Set PushBullet Notification Agent Settings', () => {
    const pushBullet = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      channel_tag: faker.random.alphaNumeric(),
      accessToken: faker.random.alphaNumeric(),
    };

    cy.request('PUT', `${BASE_URL}/notifications/pushbullet`, pushBullet)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(100);
        expect(body.extras).to.eq(null);
      })
      .request(`${BASE_URL}/notifications/pushbullet`)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(102);
        expect(body.extras.channel_tag).to.eq(pushBullet.channel_tag);
        expect(body.extras.accessToken).to.eq(pushBullet.accessToken);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#pushBulletChannelTag')
          .should('have.value', pushBullet.channel_tag);

        cy.get('#pushBulletAccessToken')
          .should('have.value', pushBullet.accessToken);

        cy.get('#pushBulletTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#pushBulletPlexServerConnectionNotification')
          .should('be.checked');

        cy.get('#pushBulletPlexMetadataUpdateNotification')
          .should('be.checked');

        cy.get('#pushBulletPlexLibraryUpdateNotification')
          .should('be.checked');

        cy.get('#pushBulletGapsMissingCollectionsNotification')
          .should('be.checked');

        cy.get('#pushBulletEnabled')
          .should('have.value', pushBullet.enabled.toString());
      });
  });

  it('Set TMDB Only PushBullet Notification Agent Settings', () => {
    const pushBullet = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      channel_tag: faker.random.alphaNumeric(),
      accessToken: faker.random.alphaNumeric(),
    };

    cy.request('PUT', `${BASE_URL}/notifications/pushbullet`, pushBullet)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(100);
        expect(body.extras).to.eq(null);
      })
      .request(`${BASE_URL}/notifications/pushbullet`)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(102);
        expect(body.extras.channel_tag).to.eq(pushBullet.channel_tag);
        expect(body.extras.accessToken).to.eq(pushBullet.accessToken);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#pushBulletChannelTag')
          .should('have.value', pushBullet.channel_tag);

        cy.get('#pushBulletAccessToken')
          .should('have.value', pushBullet.accessToken);

        cy.get('#pushBulletTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#pushBulletPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#pushBulletPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#pushBulletPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#pushBulletGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#pushBulletEnabled')
          .should('have.value', pushBullet.enabled.toString());
      });
  });

  it('Check saving PushBullet Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#pushBulletShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    const channelTag = faker.random.alphaNumeric();
    const accessToken = faker.random.alphaNumeric();

    cy.get('#pushBulletChannelTag')
      .clear()
      .type(channelTag)
      .should('have.value', channelTag);

    cy.get('#pushBulletAccessToken')
      .clear()
      .type(accessToken)
      .should('have.value', accessToken);

    cy.get('#pushBulletTmdbApiConnectionNotification')
      .click();

    cy.get('#pushBulletPlexServerConnectionNotification')
      .click();

    cy.get('#pushBulletPlexMetadataUpdateNotification')
      .click();

    cy.get('#pushBulletPlexLibraryUpdateNotification')
      .click();

    cy.get('#pushBulletGapsMissingCollectionsNotification')
      .click();

    cy.get('#pushBulletEnabled')
      .select('Enabled');

    cy.get('#savePushBullet')
      .click();

    cy.get('#pushBulletTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushBulletSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing PushBullet Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#pushBulletShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#pushBulletChannelTag')
      .clear();

    cy.get('#pushBulletAccessToken')
      .clear();

    cy.get('#pushBulletTmdbApiConnectionNotification')
      .click();

    cy.get('#pushBulletPlexServerConnectionNotification')
      .click();

    cy.get('#pushBulletPlexMetadataUpdateNotification')
      .click();

    cy.get('#pushBulletPlexLibraryUpdateNotification')
      .click();

    cy.get('#pushBulletGapsMissingCollectionsNotification')
      .click();

    cy.get('#pushBulletEnabled')
      .select('Enabled');

    cy.get('#savePushBullet')
      .click();

    cy.get('#pushBulletTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushBulletSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testPushBullet')
      .click();

    cy.get('#pushBulletTestError', { timeout: 5000 })
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#pushBulletTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#pushBulletSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
