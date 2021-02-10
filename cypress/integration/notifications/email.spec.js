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
import { BASE_URL, CYPRESS_VALUES, nuke } from '../common.spec.js';

function checkElements(username, password, mailTo, mailFrom, mailServer, mailPort, mailTransportProtocol, mailSmtpAuth, mailSmtpTlsEnabled, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#emailUsername')
    .should('have.value', username);

  cy.get('#emailPassword')
    .should('have.value', password);

  cy.get('#emailMailTo')
    .should('have.value', mailTo);

  cy.get('#emailMailFrom')
    .should('have.value', mailFrom);

  cy.get('#emailServer')
    .should('have.value', mailServer);

  cy.get('#emailPort')
    .should('have.value', mailPort);

  cy.get('#emailTransportProtocol')
    .should('have.value', mailTransportProtocol);

  cy.get('#emailSmtpAuth')
    .should('have.value', mailSmtpAuth);

  cy.get('#emailSmtpTlsEnabled')
    .should('have.value', mailSmtpTlsEnabled);

  cy.get('#emailTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#emailPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#emailPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#emailPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#emailGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#emailEnabled')
    .should('have.value', enabled);
}

describe('Check Email Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Email Gotify Notification Agent Settings', () => {
    cy.request('/notifications/email')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(122);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.username).to.eq('');
        expect(body.extras.password).to.eq('');
        expect(body.extras.mailTo).to.eq('');
        expect(body.extras.mailFrom).to.eq('');
        expect(body.extras.mailServer).to.eq('');
        expect(body.extras.mailPort).to.eq(0);
        expect(body.extras.mailTransportProtocol).to.eq('');
        expect(body.extras.mailSmtpAuth).to.eq('');
        expect(body.extras.mailSmtpTlsEnabled).to.eq(false);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#emailUsername')
          .should('have.value', '');

        cy.get('#emailPassword')
          .should('have.value', '');

        cy.get('#emailMailTo')
          .should('have.value', '');

        cy.get('#emailMailFrom')
          .should('have.value', '');

        cy.get('#emailServer')
          .should('have.value', '');

        cy.get('#emailPort')
          .should('have.value', 0);

        cy.get('#emailTransportProtocol')
          .should('have.value', 'smtp');

        cy.get('#emailSmtpAuth')
          .should('have.value', 'true');

        cy.get('#emailSmtpTlsEnabled')
          .should('have.value', 'false');

        cy.get('#emailTmdbApiConnectionNotification')
          .should('not.be.checked');

        cy.get('#emailPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#emailPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#emailPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#emailGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#emailEnabled')
          .should('have.value', 'false');
      });
  });

  it('Set Email Notification Agent Settings', () => {
    const email = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      username: faker.internet.userName(),
      password: faker.internet.password(),
      mailTo: faker.internet.email(),
      mailFrom: faker.internet.email(),
      mailServer: faker.internet.url(),
      mailPort: faker.random.number(64000),
      mailTransportProtocol: faker.lorem.word(),
      mailSmtpAuth: faker.lorem.word(),
      mailSmtpTlsEnabled: faker.random.boolean(),
    };

    cy.request('PUT', '/notifications/email', email)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(120);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/email')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(122);
        expect(body.extras.username).to.eq(email.username);
        expect(body.extras.password).to.eq(email.password);
        expect(body.extras.mailPort).to.eq(email.mailPort);
        expect(body.extras.mailSmtpTlsEnabled).to.eq(email.mailSmtpTlsEnabled);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#emailUsername')
          .should('have.value', email.username);

        cy.get('#emailPassword')
          .should('have.value', email.password);

        cy.get('#emailMailTo')
          .should('have.value', email.mailTo);

        cy.get('#emailMailFrom')
          .should('have.value', email.mailFrom);

        cy.get('#emailServer')
          .should('have.value', email.mailServer);

        cy.get('#emailPort')
          .should('have.value', email.mailPort);

        cy.get('#emailTransportProtocol')
          .should('have.value', email.mailTransportProtocol);

        cy.get('#emailSmtpAuth')
          .should('have.value', email.mailSmtpAuth);

        cy.get('#emailSmtpTlsEnabled')
          .should('have.value', email.mailSmtpTlsEnabled.toString());

        cy.get('#emailTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#emailPlexServerConnectionNotification')
          .should('be.checked');

        cy.get('#emailPlexMetadataUpdateNotification')
          .should('be.checked');

        cy.get('#emailPlexLibraryUpdateNotification')
          .should('be.checked');

        cy.get('#emailGapsMissingCollectionsNotification')
          .should('be.checked');

        cy.get('#emailEnabled')
          .should('have.value', email.enabled.toString());
      });
  });

  it('Set TMDB Only Email Notification Agent Settings', () => {
    const email = {
      enabled: faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      username: faker.internet.userName(),
      password: faker.internet.password(),
      mailTo: faker.internet.email(),
      mailFrom: faker.internet.email(),
      mailServer: faker.internet.url(),
      mailPort: faker.random.number(64000),
      mailTransportProtocol: faker.lorem.word(),
      mailSmtpAuth: faker.lorem.word(),
      mailSmtpTlsEnabled: faker.random.boolean(),
    };

    cy.request('PUT', '/notifications/email', email)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(120);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/email')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(122);
        expect(body.extras.username).to.eq(email.username);
        expect(body.extras.password).to.eq(email.password);
        expect(body.extras.mailPort).to.eq(email.mailPort);
        expect(body.extras.mailSmtpTlsEnabled).to.eq(email.mailSmtpTlsEnabled);
      })
      .visit(`${BASE_URL}/configuration`)
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#emailUsername')
          .should('have.value', email.username);

        cy.get('#emailPassword')
          .should('have.value', email.password);

        cy.get('#emailMailTo')
          .should('have.value', email.mailTo);

        cy.get('#emailMailFrom')
          .should('have.value', email.mailFrom);

        cy.get('#emailServer')
          .should('have.value', email.mailServer);

        cy.get('#emailPort')
          .should('have.value', email.mailPort);

        cy.get('#emailTransportProtocol')
          .should('have.value', email.mailTransportProtocol);

        cy.get('#emailSmtpAuth')
          .should('have.value', email.mailSmtpAuth);

        cy.get('#emailSmtpTlsEnabled')
          .should('have.value', email.mailSmtpTlsEnabled.toString());

        cy.get('#emailTmdbApiConnectionNotification')
          .should('be.checked');

        cy.get('#emailPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#emailPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#emailPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#emailGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#emailEnabled')
          .should('have.value', email.enabled.toString());
      });
  });

  it('Check saving Email Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#emailShowHide')
      .click();

    checkElements('', '', '', '', '', 0, 'smtp', 'true', 'false', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    const username = faker.internet.userName();
    const password = faker.internet.password();
    const mailTo = faker.internet.email();
    const mailFrom = faker.internet.email();
    const mailServer = faker.internet.url();
    const mailPort = faker.random.number(64000);
    const mailTransportProtocol = faker.lorem.word();
    const mailSmtpAuth = faker.lorem.word();
    const mailSmtpTlsEnabled = faker.random.boolean();

    cy.get('#emailUsername')
      .clear()
      .type(username)
      .should('have.value', username);

    cy.get('#emailPassword')
      .clear()
      .type(password)
      .should('have.value', password);

    cy.get('#emailMailTo')
      .clear()
      .type(mailTo)
      .should('have.value', mailTo);

    cy.get('#emailMailFrom')
      .clear()
      .type(mailFrom)
      .should('have.value', mailFrom);

    cy.get('#emailServer')
      .clear()
      .type(mailServer)
      .should('have.value', mailServer);

    cy.get('#emailPort')
      .clear()
      .type(mailPort)
      .should('have.value', mailPort);

    cy.get('#emailTransportProtocol')
      .clear()
      .type(mailTransportProtocol)
      .should('have.value', mailTransportProtocol);

    cy.get('#emailSmtpAuth')
      .clear()
      .type(mailSmtpAuth)
      .should('have.value', mailSmtpAuth);

    cy.get('#emailSmtpTlsEnabled')
      .select(mailSmtpTlsEnabled.toString())
      .should('have.value', mailSmtpTlsEnabled.toString());

    cy.get('#emailTmdbApiConnectionNotification')
      .click();

    cy.get('#emailPlexServerConnectionNotification')
      .click();

    cy.get('#emailPlexMetadataUpdateNotification')
      .click();

    cy.get('#emailPlexLibraryUpdateNotification')
      .click();

    cy.get('#emailGapsMissingCollectionsNotification')
      .click();

    cy.get('#emailEnabled')
      .select('Enabled');

    cy.get('#saveEmail')
      .click();

    cy.get('#emailTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#emailSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing Email Notification', () => {
    cy.visit(`${BASE_URL}/configuration`);

    cy.get('#notificationTab')
      .click();

    cy.get('#emailShowHide')
      .click();

    checkElements('', '', '', '', '', 0, 'smtp', 'true', 'false', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#emailUsername')
      .clear();

    cy.get('#emailPassword')
      .clear();

    cy.get('#emailMailTo')
      .clear();

    cy.get('#emailMailFrom')
      .clear();

    cy.get('#emailServer')
      .clear();

    cy.get('#emailPort')
      .clear();

    cy.get('#emailTransportProtocol')
      .clear();

    cy.get('#emailSmtpAuth')
      .clear();

    cy.get('#emailTmdbApiConnectionNotification')
      .click();

    cy.get('#emailPlexServerConnectionNotification')
      .click();

    cy.get('#emailPlexMetadataUpdateNotification')
      .click();

    cy.get('#emailPlexLibraryUpdateNotification')
      .click();

    cy.get('#emailGapsMissingCollectionsNotification')
      .click();

    cy.get('#emailEnabled')
      .select('Enabled');

    cy.get('#saveEmail')
      .click();

    cy.get('#emailTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#emailSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testEmail')
      .click();

    cy.get('#emailTestError', { timeout: 5000 })
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#emailTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#emailSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
