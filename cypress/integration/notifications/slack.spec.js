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

function checkElements(slackWebHookUrl, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
  cy.get('#slackWebHookUrl')
    .should('have.value', slackWebHookUrl);

  cy.get('#slackTmdbApiConnectionNotification')
    .should(tmdbApi);

  cy.get('#slackPlexServerConnectionNotification')
    .should(plexServer);

  cy.get('#slackPlexMetadataUpdateNotification')
    .should(plexMetadata);

  cy.get('#slackPlexLibraryUpdateNotification')
    .should(plexLibrary);

  cy.get('#slackGapsMissingCollectionsNotification')
    .should(gapsCollections);

  cy.get('#slackEnabled')
    .should('have.value', enabled);
}

describe('Check Slack Notification Agent', () => {
  beforeEach(nuke);

  it('Check for Empty Slack Notification Agent Settings', () => {
    cy.request('/notifications/slack')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(92);
        expect(body.extras.enabled).to.eq(false);
        expect(body.extras.notificationTypes.length).to.eq(0);
        expect(body.extras.webHookUrl).to.eq('');
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        cy.get('#slackWebHookUrl')
          .should('have.value', '');

        cy.get('#slackTmdbApiConnectionNotification')
          .should('not.be.checked');

        cy.get('#slackPlexServerConnectionNotification')
          .should('not.be.checked');

        cy.get('#slackPlexMetadataUpdateNotification')
          .should('not.be.checked');

        cy.get('#slackPlexLibraryUpdateNotification')
          .should('not.be.checked');

        cy.get('#slackGapsMissingCollectionsNotification')
          .should('not.be.checked');

        cy.get('#slackEnabled')
          .should('have.value', 'false');
      });
  });

  it('Set Slack Notification Agent Settings', () => {
    const slack = {
      "enabled": faker.random.boolean(),
      notificationTypes: ['TEST', 'TMDB_API_CONNECTION', 'PLEX_SERVER_CONNECTION', 'PLEX_METADATA_UPDATE', 'PLEX_LIBRARY_UPDATE', 'GAPS_MISSING_COLLECTIONS'],
      webHookUrl: faker.internet.url(),
    };

    cy.request('PUT', '/notifications/slack', slack)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(90);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/slack')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(92);
        expect(body.extras.webHookUrl).to.eq(slack.webHookUrl);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(slack.webHookUrl, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked, slack.enabled.toString());
      });
  });

  it('Set TMDB Only Slack Notification Agent Settings', () => {
    const slack = {
      "enabled": faker.random.boolean(),
      notificationTypes: ['TMDB_API_CONNECTION'],
      webHookUrl: faker.internet.url(),
    };

    cy.request('PUT', '/notifications/slack', slack)
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(90);
        expect(body.extras).to.eq(null);
      })
      .request('/notifications/slack')
      .then((resp) => {
        const { body } = resp;
        expect(body.code).to.eq(92);
        expect(body.extras.webHookUrl).to.eq(slack.webHookUrl);
      })
      .visit('/configuration')
      .then(() => {
        cy.get('#notificationTab')
          .click();

        checkElements(slack.webHookUrl, CYPRESS_VALUES.beChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, slack.enabled.toString());
      });
  });

  it('Check saving Slack Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#slackShowHide')
      .click();

    checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    const url = faker.internet.url();

    cy.get('#slackWebHookUrl')
      .clear()
      .type(url)
      .should('have.value', url);

    cy.get('#slackTmdbApiConnectionNotification')
      .click();

    cy.get('#slackPlexServerConnectionNotification')
      .click();

    cy.get('#slackPlexMetadataUpdateNotification')
      .click();

    cy.get('#slackPlexLibraryUpdateNotification')
      .click();

    cy.get('#slackGapsMissingCollectionsNotification')
      .click();

    cy.get('#slackEnabled')
      .select('Enabled');

    cy.get('#saveSlack')
      .click();

    cy.get('#slackTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#slackSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check saving and test Slack Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#slackShowHide')
      .click();

    checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#slackWebHookUrl')
      .clear()
      .type(atob('aHR0cHM6Ly9ob29rcy5zbGFjay5jb20vc2VydmljZXMvVDAxN1hSMEJNUlYvQjAxOEs0TkE3NjAvemsxcVVWMno1N0w4c3lqQ0ExUU8xRFFE'))
      .should('have.value', atob('aHR0cHM6Ly9ob29rcy5zbGFjay5jb20vc2VydmljZXMvVDAxN1hSMEJNUlYvQjAxOEs0TkE3NjAvemsxcVVWMno1N0w4c3lqQ0ExUU8xRFFE'));

    cy.get('#slackTmdbApiConnectionNotification')
      .click();

    cy.get('#slackPlexServerConnectionNotification')
      .click();

    cy.get('#slackPlexMetadataUpdateNotification')
      .click();

    cy.get('#slackPlexLibraryUpdateNotification')
      .click();

    cy.get('#slackGapsMissingCollectionsNotification')
      .click();

    cy.get('#slackEnabled')
      .select('Enabled');

    cy.get('#saveSlack')
      .click();

    cy.get('#slackTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#slackSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testSlack')
      .click();

    cy.wait(2000);

    cy.get('#slackTestSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#slackTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });

  it('Check Successful Saving and Failure Testing Slack Notification', () => {
    cy.visit('/configuration');

    cy.get('#notificationTab')
      .click();

    cy.get('#slackShowHide')
      .click();

    checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

    cy.get('#slackWebHookUrl')
      .clear();

    cy.get('#slackTmdbApiConnectionNotification')
      .click();

    cy.get('#slackPlexServerConnectionNotification')
      .click();

    cy.get('#slackPlexMetadataUpdateNotification')
      .click();

    cy.get('#slackPlexLibraryUpdateNotification')
      .click();

    cy.get('#slackGapsMissingCollectionsNotification')
      .click();

    cy.get('#slackEnabled')
      .select('Enabled');

    cy.get('#saveSlack')
      .click();

    cy.get('#slackTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackTestError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveSuccess')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#slackSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSpinner')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#testSlack')
      .click();

    cy.wait(2000);

    cy.get('#slackTestSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackTestError')
      .should(CYPRESS_VALUES.beVisible);

    cy.get('#slackSaveSuccess')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSaveError')
      .should(CYPRESS_VALUES.notBeVisible);

    cy.get('#slackSpinner')
      .should(CYPRESS_VALUES.notBeVisible);
  });
});
