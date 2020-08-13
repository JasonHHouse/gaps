import {nuke} from "../common";

describe('Check Email Notification Agent', function () {
    before(nuke);

    it('Check for Email Gotify Notification Agent Settings', () => {

        cy.request('/notifications/email')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(122);
                expect(body.extras).to.eq(null);
            })
            .visit('/configuration')
            .then((resp) => {
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
                    .should('have.value', '');

                cy.get('#emailTransportProtocol')
                    .should('have.value', '');

                cy.get('#emailSmtpAuth')
                    .should('have.value', '');

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

        let object = {
            "enabled": true,
            "notificationTypes": ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "username": "username",
            "password": "password",
            "mailTo": "mailTo",
            "mailFrom": "mailFrom",
            "mailServer": "mailServer",
            "mailPort": 12345,
            "mailTransportProtocol": "mailTransportProtocol",
            "mailSmtpAuth": "mailSmtpAuth",
            "mailSmtpTlsEnabled": true
        };

        cy.request('PUT', '/notifications/email', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(120);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/email')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(122);
                expect(body.extras.username).to.eq("username");
                expect(body.extras.password).to.eq("password");
                expect(body.extras.mailPort).to.eq(12345);
                expect(body.extras.mailSmtpTlsEnabled).to.eq(true);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#emailUsername')
                    .should('have.value', 'username');

                cy.get('#emailPassword')
                    .should('have.value', 'password');

                cy.get('#emailMailTo')
                    .should('have.value', 'mailTo');

                cy.get('#emailMailFrom')
                    .should('have.value', 'mailFrom');

                cy.get('#emailServer')
                    .should('have.value', 'mailServer');

                cy.get('#emailPort')
                    .should('have.value', '12345');

                cy.get('#emailTransportProtocol')
                    .should('have.value', 'mailTransportProtocol');

                cy.get('#emailSmtpAuth')
                    .should('have.value', 'mailSmtpAuth');

                cy.get('#emailSmtpTlsEnabled')
                    .should('have.value', 'true');

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
                    .should('have.value', 'true');
            });
    });


    it('Set TMDB Only Email Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TMDB_API_CONNECTION"],
            "username": "username",
            "password": "password",
            "mailTo": "mailTo",
            "mailFrom": "mailFrom",
            "mailServer": "mailServer",
            "mailPort": 12345,
            "mailTransportProtocol": "mailTransportProtocol",
            "mailSmtpAuth": "mailSmtpAuth",
            "mailSmtpTlsEnabled": true
        };

        cy.request('PUT', '/notifications/email', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(120);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/email')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(122);
                expect(body.extras.username).to.eq("username");
                expect(body.extras.password).to.eq("password");
                expect(body.extras.mailPort).to.eq(12345);
                expect(body.extras.mailSmtpTlsEnabled).to.eq(true);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#emailUsername')
                    .should('have.value', 'username');

                cy.get('#emailPassword')
                    .should('have.value', 'password');

                cy.get('#emailMailTo')
                    .should('have.value', 'mailTo');

                cy.get('#emailMailFrom')
                    .should('have.value', 'mailFrom');

                cy.get('#emailServer')
                    .should('have.value', 'mailServer');

                cy.get('#emailPort')
                    .should('have.value', '12345');

                cy.get('#emailTransportProtocol')
                    .should('have.value', 'mailTransportProtocol');

                cy.get('#emailSmtpAuth')
                    .should('have.value', 'mailSmtpAuth');

                cy.get('#emailSmtpTlsEnabled')
                    .should('have.value', 'true');

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
                    .should('have.value', 'true');
            });
    });
});