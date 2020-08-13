import {nuke} from "../common";

describe('Check Gotify Notification Agent', function () {
    before(nuke);

    it('Check for Empty Gotify Notification Agent Settings', () => {

        cy.request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras).to.eq(null);
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
});