import {CYPRESS_VALUES, nuke} from "../common";

describe('Check Telegram Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty Telegram Notification Agent Settings', () => {

        cy.request('/notifications/telegram')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(82);
                expect(body.extras.enabled).to.eq(false);
                expect(body.extras.notificationTypes.length).to.eq(0);
                expect(body.extras.botId).to.eq("");
                expect(body.extras.chatId).to.eq("");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('', '', CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');
            });
    });

    it('Set Telegram Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "chatId": "chatId",
            "botId": "botId"
        };

        cy.request('PUT', '/notifications/telegram', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(80);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/telegram')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(82);
                expect(body.extras.chatId).to.eq("chatId");
                expect(body.extras.botId).to.eq("botId");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('botId', 'chatId', CYPRESS_VALUES.beChecked, CYPRESS_VALUES.beChecked,CYPRESS_VALUES. beChecked,CYPRESS_VALUES. beChecked,CYPRESS_VALUES. beChecked, 'true');
            });
    });

    it('Set TMDB Only Telegram Notification Agent Settings', () => {

        let object = {
            "enabled": true,
            "notificationTypes": ["TMDB_API_CONNECTION"],
            "chatId": "chatId",
            "botId": "botId"
        };

        cy.request('PUT', '/notifications/telegram', object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(80);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/telegram')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(82);
                expect(body.extras.chatId).to.eq("chatId");
                expect(body.extras.botId).to.eq("botId");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('botId', 'chatId', CYPRESS_VALUES.beChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'true');
            });
    });

    it('Check saving Telegram Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#telegramBotId')
            .clear()
            .type('botId')
            .should('have.value', 'botId');

        cy.get('#telegramChatId')
            .clear()
            .type('chatId')
            .should('have.value', 'chatId');

        cy.get('#telegramTmdbApiConnectionNotification')
            .click();

        cy.get('#telegramPlexServerConnectionNotification')
            .click();

        cy.get('#telegramPlexMetadataUpdateNotification')
            .click();

        cy.get('#telegramPlexLibraryUpdateNotification')
            .click();

        cy.get('#telegramGapsMissingCollectionsNotification')
            .click();

        cy.get('#telegramEnabled')
            .select('Enabled');

        cy.get('#saveTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#telegramSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check saving and test Telegram Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#telegramBotId')
            .clear()
            .type(atob('MTMwMjEzOTExOTpBQUV1cGh0RXVvcFhhVHBpdGlHbFdDWS0zbDMzU0JrUGUybw=='))
            .should('have.value', atob('MTMwMjEzOTExOTpBQUV1cGh0RXVvcFhhVHBpdGlHbFdDWS0zbDMzU0JrUGUybw=='));

        cy.get('#telegramChatId')
            .clear()
            .type(atob('MTA0MTA4MTMxNw=='))
            .should('have.value', atob('MTA0MTA4MTMxNw=='));

        cy.get('#telegramTmdbApiConnectionNotification')
            .click();

        cy.get('#telegramPlexServerConnectionNotification')
            .click();

        cy.get('#telegramPlexMetadataUpdateNotification')
            .click();

        cy.get('#telegramPlexLibraryUpdateNotification')
            .click();

        cy.get('#telegramGapsMissingCollectionsNotification')
            .click();

        cy.get('#telegramEnabled')
            .select('Enabled');

        cy.get('#saveTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#telegramSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#telegramTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing Telegram Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '',CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#telegramBotId')
            .clear();

        cy.get('#telegramChatId')
            .clear();

        cy.get('#telegramTmdbApiConnectionNotification')
            .click();

        cy.get('#telegramPlexServerConnectionNotification')
            .click();

        cy.get('#telegramPlexMetadataUpdateNotification')
            .click();

        cy.get('#telegramPlexLibraryUpdateNotification')
            .click();

        cy.get('#telegramGapsMissingCollectionsNotification')
            .click();

        cy.get('#telegramEnabled')
            .select('Enabled');

        cy.get('#saveTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#telegramSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#telegramSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#telegramSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});

function checkElements(botId, chatId, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
    cy.get('#telegramBotId')
        .should('have.value', botId);

    cy.get('#telegramChatId')
        .should('have.value', chatId);

    cy.get('#telegramTmdbApiConnectionNotification')
        .should(tmdbApi);

    cy.get('#telegramPlexServerConnectionNotification')
        .should(plexServer);

    cy.get('#telegramPlexMetadataUpdateNotification')
        .should(plexMetadata);

    cy.get('#telegramPlexLibraryUpdateNotification')
        .should(plexLibrary);

    cy.get('#telegramGapsMissingCollectionsNotification')
        .should(gapsCollections);

    cy.get('#telegramEnabled')
        .should('have.value', enabled);
}
