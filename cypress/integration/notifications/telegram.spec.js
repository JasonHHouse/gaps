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

function checkElements(botId, chatId, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#telegramBotId')
    .should('have.value', botId);

  cy.get('#telegramChatId')
    .should('have.value', chatId);

  cy.get('#telegramTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#telegramPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#telegramPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#telegramPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#telegramGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#telegramEnabled')
    .should('have.value', enabled);
}

describe('Check Telegram Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty Telegram Notification Agent Settings', () => {
    cy.request('/notifications/telegram')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(82);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.botId).to.eq('');
        expect(body.extras.chatId).to.eq('');
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');
      });
  });

  it('Set Telegram Notification Agent Settings', () => {
    const telegram = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      chatId: faker.random.alphaNumeric(),
      botId: faker.random.alphaNumeric(),
    };

    cy.request('PUT', '/notifications/telegram', telegram)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(80);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/telegram')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(82);
        expect(body.extras.chatId).to.eq(telegram.chatId);
        expect(body.extras.botId).to.eq(telegram.botId);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(telegram.botId, telegram.chatId, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, telegram.enabled.toString());
      });
  });

  it('Set TMDB Only Telegram Notification Agent Settings', () => {
    const telegram = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      chatId: faker.random.alphaNumeric(),
      botId: faker.random.alphaNumeric(),
    };

    cy.request('PUT', '/notifications/telegram', telegram)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(80);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/telegram')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(82);
        expect(body.extras.chatId).to.eq(telegram.chatId);
        expect(body.extras.botId).to.eq(telegram.botId);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(telegram.botId, telegram.chatId, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, telegram.enabled.toString());
      });
  });

  it('Check saving Telegram Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#telegramShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#telegramBotId')
      .clear()
      .type('botId')
      .should('have.value', 'botId');

    cy.get('#telegramChatId')
      .clear()
      .type('chatId')
      .should('have.value', 'chatId');

    cy.get('#telegramTmdbApiConnectionNotification')
      .click();

    cy.get('#telegramPlexServerConnectionNotification')
      .click();

    cy.get('#telegramPlexMetadataUpdateNotification')
      .click();

    cy.get('#telegramPlexLibraryUpdateNotification')
      .click();

    cy.get('#telegramGapsMissingCollectionsNotification')
      .click();

    cy.get('#telegramEnabled')
      .select('Enabled');

    cy.get('#saveTelegram')
      .click();

    cy.get('#telegramTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#telegramSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing Telegram Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#telegramShowHide')
      .click();

    checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#telegramBotId')
      .clear();

    cy.get('#telegramChatId')
      .clear();

    cy.get('#telegramTmdbApiConnectionNotification')
      .click();

    cy.get('#telegramPlexServerConnectionNotification')
      .click();

    cy.get('#telegramPlexMetadataUpdateNotification')
      .click();

    cy.get('#telegramPlexLibraryUpdateNotification')
      .click();

    cy.get('#telegramGapsMissingCollectionsNotification')
      .click();

    cy.get('#telegramEnabled')
      .select('Enabled');

    cy.get('#saveTelegram')
      .click();

    cy.get('#telegramTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#telegramSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testTelegram')
      .click();

    cy.get('#telegramTestError', { timeout: 5000 })
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#telegramTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#telegramSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
