import {CYPRESS_VALUES, nuke} from "../common";

describe('Check Gotify Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty Gotify Notification Agent Settings', () => {

        cy.request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras.enabled).to.eq(false);
                expect(body.extras.notificationTypes.length).to.eq(0);
                expect(body.extras.address).to.eq("");
                expect(body.extras.token).to.eq("");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#gotifyAddress')
                    .should('have.value', '');

                cy.get('#gotifyToken')
                    .should('have.value', '');

                cy.get('#gotifyTmdbApiConnectionNotification')
                    .should('not.be.checked');

                cy.get('#gotifyPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#gotifyPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#gotifyPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#gotifyGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#gotifyEnabled')
                    .should('have.value', 'false');
            });
    });

    it('Set Gotify Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "address" : "address",
            "token" : "token"
        };

        cy.request('PUT', '/notifications/gotify',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(110);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras.address).to.eq("address");
                expect(body.extras.token).to.eq("token");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#gotifyAddress')
                    .should('have.value', 'address');

                cy.get('#gotifyToken')
                    .should('have.value', 'token');

                cy.get('#gotifyTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#gotifyPlexServerConnectionNotification')
                    .should('be.checked');

                cy.get('#gotifyPlexMetadataUpdateNotification')
                    .should('be.checked');

                cy.get('#gotifyPlexLibraryUpdateNotification')
                    .should('be.checked');

                cy.get('#gotifyGapsMissingCollectionsNotification')
                    .should('be.checked');

                cy.get('#gotifyEnabled')
                    .should('have.value', 'true');
            });
    });

    it('Set TMDB Only Gotify Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TMDB_API_CONNECTION"],
            "address" : "address",
            "token" : "token"
        };

        cy.request('PUT', '/notifications/gotify',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(110);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras.address).to.eq("address");
                expect(body.extras.token).to.eq("token");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#gotifyAddress')
                    .should('have.value', 'address');

                cy.get('#gotifyToken')
                    .should('have.value', 'token');

                cy.get('#gotifyTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#gotifyPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#gotifyPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#gotifyPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#gotifyGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#gotifyEnabled')
                    .should('have.value', 'true');
            });
    });

    it('Check saving Gotify Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        cy.get('#gotifyShowHide')
            .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#gotifyAddress')
            .clear()
            .type('address')
            .should('have.value', 'address');

        cy.get('#gotifyToken')
            .clear()
            .type('token')
            .should('have.value', 'token');

        cy.get('#gotifyTmdbApiConnectionNotification')
            .click();

        cy.get('#gotifyPlexServerConnectionNotification')
            .click();

        cy.get('#gotifyPlexMetadataUpdateNotification')
            .click();

        cy.get('#gotifyPlexLibraryUpdateNotification')
            .click();

        cy.get('#gotifyGapsMissingCollectionsNotification')
            .click();

        cy.get('#gotifyEnabled')
            .select('Enabled');

        cy.get('#saveGotify')
            .click();

        cy.get('#gotifyTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifyTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#gotifySaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check saving and test Gotify Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        cy.get('#gotifyShowHide')
            .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#gotifyAddress')
            .clear()
            .type(atob('aHR0cDovLzE5Mi4xNjguMS44OjgwNzA='))
            .should('have.value', atob('aHR0cDovLzE5Mi4xNjguMS44OjgwNzA='));

        cy.get('#gotifyToken')
            .clear()
            .type(atob('QVJkbS55SHRQUTlhaW5i'))
            .should('have.value', atob('QVJkbS55SHRQUTlhaW5i'));

        cy.get('#gotifyTmdbApiConnectionNotification')
            .click();

        cy.get('#gotifyPlexServerConnectionNotification')
            .click();

        cy.get('#gotifyPlexMetadataUpdateNotification')
            .click();

        cy.get('#gotifyPlexLibraryUpdateNotification')
            .click();

        cy.get('#gotifyGapsMissingCollectionsNotification')
            .click();

        cy.get('#gotifyEnabled')
            .select('Enabled');

        cy.get('#saveGotify')
            .click();

        cy.get('#gotifyTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifyTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#gotifySaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testGotify')
            .click();

        cy.get('#gotifyTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#gotifyTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing Gotify Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        cy.get('#gotifyShowHide')
            .click();

        checkElements('', '',CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#gotifyAddress')
            .clear();

        cy.get('#gotifyToken')
            .clear();

        cy.get('#gotifyTmdbApiConnectionNotification')
            .click();

        cy.get('#gotifyPlexServerConnectionNotification')
            .click();

        cy.get('#gotifyPlexMetadataUpdateNotification')
            .click();

        cy.get('#gotifyPlexLibraryUpdateNotification')
            .click();

        cy.get('#gotifyGapsMissingCollectionsNotification')
            .click();

        cy.get('#gotifyEnabled')
            .select('Enabled');

        cy.get('#saveGotify')
            .click();

        cy.get('#gotifyTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifyTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#gotifySaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testGotify')
            .click();

        cy.get('#gotifyTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifyTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#gotifySaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#gotifySpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});


function checkElements(address, token, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
    cy.get('#gotifyAddress')
        .should('have.value', address);

    cy.get('#gotifyToken')
        .should('have.value', token);

    cy.get('#gotifyTmdbApiConnectionNotification')
        .should(tmdbApi);

    cy.get('#gotifyPlexServerConnectionNotification')
        .should(plexServer);

    cy.get('#gotifyPlexMetadataUpdateNotification')
        .should(plexMetadata);

    cy.get('#gotifyPlexLibraryUpdateNotification')
        .should(plexLibrary);

    cy.get('#gotifyGapsMissingCollectionsNotification')
        .should(gapsCollections);

    cy.get('#gotifyEnabled')
        .should('have.value', enabled);
}
