import {CYPRESS_VALUES, nuke} from "../common";

describe('Check Email Notification Agent', function () {
    beforeEach(nuke);

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

    it('Check saving Email Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', '', '', '', '', 'smtp', 'true', 'false', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#emailUsername')
            .clear()
            .type('username')
            .should('have.value', 'username');

        cy.get('#emailPassword')
            .clear()
            .type('password')
            .should('have.value', 'password');

        cy.get('#emailMailTo')
            .clear()
            .type('mailTo')
            .should('have.value', 'mailTo');

        cy.get('#emailMailFrom')
            .clear()
            .type('MailFrom')
            .should('have.value', 'MailFrom');

        cy.get('#emailServer')
            .clear()
            .type('111.222.333.444')
            .should('have.value', '111.222.333.444');

        cy.get('#emailPort')
            .clear()
            .type('12345')
            .should('have.value', '12345');

        cy.get('#emailTransportProtocol')
            .clear()
            .type('etp')
            .should('have.value', 'etp');

        cy.get('#emailSmtpAuth')
            .clear()
            .type('smtp')
            .should('have.value', 'smtp');

        cy.get('#emailSmtpTlsEnabled')
            .select("true")
            .should('have.value', 'true');

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

    it('Check saving and test Email Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', '', '', '', '', 'smtp', 'true', 'false',  CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#emailUsername')
            .clear()
            .type(atob('amg1OTc1'))
            .should('have.value', atob('amg1OTc1'));

        cy.get('#emailPassword')
            .clear()
            .type(atob('aWJnYnR3aXdyd2xvZWNucA=='))
            .should('have.value', atob('aWJnYnR3aXdyd2xvZWNucA=='));

        cy.get('#emailMailTo')
            .clear()
            .type(atob('amg1OTc1QGdtYWlsLmNvbQ=='))
            .should('have.value', atob('amg1OTc1QGdtYWlsLmNvbQ=='));

        cy.get('#emailMailFrom')
            .clear()
            .type(atob('amg1OTc1QGdtYWlsLmNvbQ=='))
            .should('have.value', atob('amg1OTc1QGdtYWlsLmNvbQ=='));

        cy.get('#emailServer')
            .clear()
            .type(atob('c210cC5nbWFpbC5jb20='))
            .should('have.value', atob('c210cC5nbWFpbC5jb20='));

        cy.get('#emailPort')
            .clear()
            .type(atob('NTg3'))
            .should('have.value', atob('NTg3'));

        cy.get('#emailTransportProtocol')
            .clear()
            .type(atob('c210cA=='))
            .should('have.value', atob('c210cA=='));

        cy.get('#emailSmtpAuth')
            .clear()
            .type(atob('amg1OTc1'))
            .should('have.value', atob('amg1OTc1'));

        cy.get('#emailSmtpTlsEnabled')
            .select("true")
            .should('have.value', 'true');

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

        cy.wait(1000);

        cy.get('#emailTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#emailTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing Email Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', '', '', '', '', 'smtp', 'true', 'false', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

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

        cy.get('#emailTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#emailSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#emailSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});

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
