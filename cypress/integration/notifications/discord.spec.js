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

function checkElements(discordWebHookUrl, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#discordWebHookUrl')
    .should('have.value', discordWebHookUrl);

  cy.get('#discordTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#discordPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#discordPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#discordPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#discordGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#discordEnabled')
    .should('have.value', enabled);
}

describe('Check Discord Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty Discord Notification Agent Settings', () => {
    cy.request(`${BASE_URL}/notifications/discord`)
      .then((resp) => {
        const { body } = resp;
        cy.fixture('discord.empty.json')
          .then((discord) => {
            expect(body.code).to.eq(discord.code);
            expect(body.extras.enabled).to.eq(discord.enabled);
            expect(body.extras.notificationTypes.length).to.eq(discord.notificationTypesLength);
            expect(body.extras.webHookUrl).to.eq(discord.webHookUrl);
          });
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#discordWebHookUrl')
          .should('have.value', '');

        cy.get('#discordTmdbApiConnectionNotification')
          .should('not.be.checked');

        cy.get('#discordPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#discordPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#discordPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#discordGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#discordEnabled')
          .should('have.value', 'false');
      });
  });

  it('Set Discord Notification Agent Settings', () => {
    const dummyAll = {
      enabled: faker.random.boolean(),
      notificationTypes: [
        'TEST',
        'TMDB_API_CONNECTION',
        'PLEX_SERVER_CONNECTION',
        'PLEX_METADATA_UPDATE',
        'PLEX_LIBRARY_UPDATE',
        'GAPS_MISSING_COLLECTIONS',
      ],
      webHookUrl: faker.internet.url(),
    };

    cy.request('PUT', `${BASE_URL}/notifications/discord`, dummyAll)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(140);
        expect(body.extras).to.eq(null);
      })
      .request(`${BASE_URL}/notifications/discord`)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(142);
        expect(body.extras.webHookUrl).to.eq(dummyAll.webHookUrl);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#discordWebHookUrl')
          .should('have.value', dummyAll.webHookUrl);

        cy.get('#discordTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#discordPlexServerConnectionNotification')
          .should('be.checked');

        cy.get('#discordPlexMetadataUpdateNotification')
          .should('be.checked');

        cy.get('#discordPlexLibraryUpdateNotification')
          .should('be.checked');

        cy.get('#discordGapsMissingCollectionsNotification')
          .should('be.checked');

        cy.get('#discordEnabled')
          .should('have.value', dummyAll.enabled.toString());
      });
  });

  it('Set TMDB Only Discord Notification Agent Settings', () => {
    const tmdbOnly = {
      enabled: faker.random.boolean(),
      notificationTypes: [
        'TMDB_API_CONNECTION',
      ],
      webHookUrl: faker.internet.url(),
    };
    cy.request('PUT', `${BASE_URL}/notifications/discord`, tmdbOnly)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(140);
        expect(body.extras).to.eq(null);
      })
      .request(`${BASE_URL}/notifications/discord`)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(142);
        expect(body.extras.webHookUrl).to.eq(tmdbOnly.webHookUrl);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#discordWebHookUrl')
          .should('have.value', tmdbOnly.webHookUrl);

        cy.get('#discordTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#discordPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#discordPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#discordPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#discordGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#discordEnabled')
          .should('have.value', tmdbOnly.enabled.toString());
      });
  });

  it('Check saving Discord Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#discordShowHide')
      .click();

    checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    const url = faker.internet.url();

    cy.get('#discordWebHookUrl')
      .clear()
      .type(url)
      .should('have.value', url);

    cy.get('#discordTmdbApiConnectionNotification')
      .click();

    cy.get('#discordPlexServerConnectionNotification')
      .click();

    cy.get('#discordPlexMetadataUpdateNotification')
      .click();

    cy.get('#discordPlexLibraryUpdateNotification')
      .click();

    cy.get('#discordGapsMissingCollectionsNotification')
      .click();

    cy.get('#discordEnabled')
      .select('Enabled');

    cy.get('#saveDiscord')
      .click();

    cy.get('#discordTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#discordSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing Discord Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#discordShowHide')
      .click();

    checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#discordWebHookUrl')
      .clear();

    cy.get('#discordTmdbApiConnectionNotification')
      .click();

    cy.get('#discordPlexServerConnectionNotification')
      .click();

    cy.get('#discordPlexMetadataUpdateNotification')
      .click();

    cy.get('#discordPlexLibraryUpdateNotification')
      .click();

    cy.get('#discordGapsMissingCollectionsNotification')
      .click();

    cy.get('#discordEnabled')
      .select('Enabled');

    cy.get('#saveDiscord')
      .click();

    cy.get('#discordTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#discordSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testDiscord')
      .click();

    cy.get('#discordTestError', { timeout: 5000 })
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#discordTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#discordSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
