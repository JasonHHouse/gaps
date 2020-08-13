import {nuke} from "../common";

describe('Check Slack Notification Agent', function () {
    before(nuke);

    it('Check for Empty Slack Notification Agent Settings', () => {

        cy.request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras).to.eq(null);
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
            "enabled" : true,
            "notificationTypes" : ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "webHookUrl" : "webHookUrl"
        };

        cy.request('PUT', '/notifications/slack',object)
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
            "enabled" : true,
            "notificationTypes" : ["TMDB_API_CONNECTION"],
            "webHookUrl" : "webHookUrl"
        };

        cy.request('PUT', '/notifications/slack',object)
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
});