import {CYPRESS_VALUES, nuke} from "../common";

describe('Check Slack Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty Slack Notification Agent Settings', () => {

        cy.request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.enabled).to.eq(false);
                expect(body.extras.notificationTypes.length).to.eq(0);
                expect(body.extras.webHookUrl).to.eq("");
            })
            .visit('/configuration')
            .then((resp) => {
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

        let object = {
            "enabled": true,
            "notificationTypes": ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "webHookUrl": "webHookUrl"
        };

        cy.request('PUT', '/notifications/slack', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(90);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.webHookUrl).to.eq("webHookUrl");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#slackWebHookUrl')
                    .should('have.value', 'webHookUrl');

                cy.get('#slackTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#slackPlexServerConnectionNotification')
                    .should('be.checked');

                cy.get('#slackPlexMetadataUpdateNotification')
                    .should('be.checked');

                cy.get('#slackPlexLibraryUpdateNotification')
                    .should('be.checked');

                cy.get('#slackGapsMissingCollectionsNotification')
                    .should('be.checked');

                cy.get('#slackEnabled')
                    .should('have.value', 'true');
            });
    });

    it('Set TMDB Only Slack Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TMDB_API_CONNECTION"],
            "webHookUrl": "webHookUrl"
        };

        cy.request('PUT', '/notifications/slack', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(90);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.webHookUrl).to.eq("webHookUrl");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#slackWebHookUrl')
                    .should('have.value', 'webHookUrl');

                cy.get('#slackTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#slackPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#slackPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#slackPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#slackGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#slackEnabled')
                    .should('have.value', 'true');
            });
    });


    it('Check saving Slack Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('',CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#slackWebHookUrl')
            .clear()
            .type('slackWebHookUrl')
            .should('have.value', 'slackWebHookUrl');

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

        checkElements('', CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

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

        checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

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