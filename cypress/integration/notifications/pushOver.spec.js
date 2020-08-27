/*jshint esversion: 6 */
import {CYPRESS_VALUES, nuke} from "../common";

describe('Check PushOver Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty PushOver Notification Agent Settings', () => {

        cy.request('/notifications/pushOver')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(132);
                expect(body.extras).to.eq(null);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('', '',-2,'pushover', '60','3600', CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');
            });
    });

    it('Set PushOver Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "token": "token",
            "user": "user",
            "priority": 1,
            "sound": "updown",
            "retry": 60,
            "expire": 3600
        };

        cy.request('PUT', '/notifications/pushOver', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(130);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/pushOver')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(132);
                expect(body.extras.token).to.eq("token");
                expect(body.extras.user).to.eq("user");
                expect(body.extras.priority).to.eq(1);
                expect(body.extras.sound).to.eq("updown");
                expect(body.extras.retry).to.eq(60);
                expect(body.extras.expire).to.eq(3600);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('token', 'user',1,'updown', '60','3600', CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked,CYPRESS_VALUES. beChecked,CYPRESS_VALUES. beChecked,CYPRESS_VALUES. beChecked, 'true');
            });
    });

    it('Set TMDB Only PushOver Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TMDB_API_CONNECTION"],
            "token": "token",
            "user": "user",
            "priority": 1,
            "sound": "updown",
            "retry": 60,
            "expire": 3600
        };

        cy.request('PUT', '/notifications/pushOver', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(130);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/pushOver')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(132);
                expect(body.extras.token).to.eq("token");
                expect(body.extras.user).to.eq("user");
                expect(body.extras.priority).to.eq(1);
                expect(body.extras.sound).to.eq("updown");
                expect(body.extras.retry).to.eq(60);
                expect(body.extras.expire).to.eq(3600);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('token', 'user',1, 'updown', 60, 3600, CYPRESS_VALUES.beChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'true');
            });
    });

    it('Check saving PushOver Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', -2,'pushover', '60','3600', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

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

    it('Check saving and test PushOver Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '',-2,'pushover', '60','3600',  CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#pushOverToken')
            .clear()
            .type(atob('YWM1ZjJya2JwejEyMnBqenNlMnBlbmJkdmYxZ2dh'))
            .should('have.value', atob('YWM1ZjJya2JwejEyMnBqenNlMnBlbmJkdmYxZ2dh'));

        cy.get('#pushOverUser')
            .clear()
            .type(atob('dTdiemZkZW5kdmRqbnM1eXRrMmV3YjIxZDduaWJ5'))
            .should('have.value', atob('dTdiemZkZW5kdmRqbnM1eXRrMmV3YjIxZDduaWJ5'));

        cy.get('#pushOverPriority')
            .select('1')
            .should('have.value', '1');

        cy.get('#pushOverSound')
            .select('spacealarm')
            .should('have.value', 'spacealarm');

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

        cy.get('#testPushOver')
            .click();

        cy.get('#pushOverTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushOverTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing PushOver Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '',-2,'pushover', '60','3600',CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

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

        cy.get('#pushOverTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushOverSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushOverSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});

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
