import {CYPRESS_VALUES, nuke} from "../common";

describe('Check Discord Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty Discord Notification Agent Settings', () => {

        cy.request('/notifications/discord')
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

        let object = {
            "enabled": true,
            "notificationTypes": ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "webHookUrl": "webHookUrl"
        };

        cy.request('PUT', '/notifications/discord', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(90);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/discord')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.webHookUrl).to.eq("webHookUrl");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#discordWebHookUrl')
                    .should('have.value', 'webHookUrl');

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
                    .should('have.value', 'true');
            });
    });

    it('Set TMDB Only Discord Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TMDB_API_CONNECTION"],
            "webHookUrl": "webHookUrl"
        };

        cy.request('PUT', '/notifications/discord', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(90);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/discord')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.webHookUrl).to.eq("webHookUrl");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#discordWebHookUrl')
                    .should('have.value', 'webHookUrl');

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
                    .should('have.value', 'true');
            });
    });


    it('Check saving Discord Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('',CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#discordWebHookUrl')
            .clear()
            .type('discordWebHookUrl')
            .should('have.value', 'discordWebHookUrl');

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

    it('Check saving and test Discord Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#discordWebHookUrl')
            .clear()
            .type(atob('aHR0cHM6Ly9kaXNjb3JkYXBwLmNvbS9hcGkvd2ViaG9va3MvNzU2MzExMTkwOTU0NTczOTU2L0Uwa25VRF91akxIQU5oZTA3MGEwYW9PMUNnMGRNQUV4Z1FBbEZGTS1GZUgwNnl3VGExLU42c2ZhREpQSC1lM2dDVEZz'))
            .should('have.value', atob('aHR0cHM6Ly9kaXNjb3JkYXBwLmNvbS9hcGkvd2ViaG9va3MvNzU2MzExMTkwOTU0NTczOTU2L0Uwa25VRF91akxIQU5oZTA3MGEwYW9PMUNnMGRNQUV4Z1FBbEZGTS1GZUgwNnl3VGExLU42c2ZhREpQSC1lM2dDVEZz'));

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

        cy.get('#discordTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#discordTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing Discord Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

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

        cy.get('#discordTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#discordSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#discordSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});


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