import {nuke} from "../common";

const notBeChecked = 'not.be.checked';
const beChecked = 'be.checked';
const notBeVisible = 'not.be.visible';
const beVisible = 'be.visible';

describe('Check Telegram Notification Agent', function () {
    beforeEach(nuke);

    it('Check for Empty Telegram Notification Agent Settings', () => {

        cy.request('/notifications/telegram')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(82);
                expect(body.extras).to.eq(null);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                checkElements('', '', notBeChecked, notBeChecked, notBeChecked, notBeChecked, notBeChecked, 'false');
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

                checkElements('botId', 'chatId', beChecked, beChecked, beChecked, beChecked, beChecked, 'true');
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

                checkElements('botId', 'chatId', beChecked, notBeChecked, notBeChecked, notBeChecked, notBeChecked, 'true');
            });
    });

    it('Check saving Telegram Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', notBeChecked, notBeChecked, notBeChecked, notBeChecked, notBeChecked, 'false');

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
            .should(notBeVisible);

        cy.get('#telegramTestError')
            .should(notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(beVisible);

        cy.get('#telegramSaveError')
            .should(notBeVisible);

        cy.get('#telegramSpinner')
            .should(notBeVisible);
    });

    it('Check saving and test Telegram Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', notBeChecked, notBeChecked, notBeChecked, notBeChecked, notBeChecked, 'false');

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
            .should(notBeVisible);

        cy.get('#telegramTestError')
            .should(notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(beVisible);

        cy.get('#telegramSaveError')
            .should(notBeVisible);

        cy.get('#telegramSpinner')
            .should(notBeVisible);

        cy.get('#testTelegram')
            .click();

        cy.get('#telegramTestSuccess')
            .should(beVisible);

        cy.get('#telegramTestError')
            .should(notBeVisible);

        cy.get('#telegramSaveSuccess')
            .should(notBeVisible);

        cy.get('#telegramSaveError')
            .should(notBeVisible);

        cy.get('#telegramSpinner')
            .should(notBeVisible);
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
