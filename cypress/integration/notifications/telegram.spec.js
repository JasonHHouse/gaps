import {nuke} from "../common";

describe('Check Telegram Notification Agent', function () {
    before(nuke);

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

                cy.get('#telegramBotId')
                    .should('have.value', '');

                cy.get('#telegramChatId')
                    .should('have.value', '');

                cy.get('#telegramTmdbApiConnectionNotification')
                    .should('not.be.checked');

                cy.get('#telegramPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#telegramPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#telegramPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#telegramGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#telegramEnabled')
                    .should('have.value', 'false');
            });
    });

    it('Set Telegram Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "chatId" : "chatId",
            "botId" : "botId"
        };

        cy.request('PUT', '/notifications/telegram',object)
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

                cy.get('#telegramBotId')
                    .should('have.value', 'botId');

                cy.get('#telegramChatId')
                    .should('have.value', 'chatId');

                cy.get('#telegramTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#telegramPlexServerConnectionNotification')
                    .should('be.checked');

                cy.get('#telegramPlexMetadataUpdateNotification')
                    .should('be.checked');

                cy.get('#telegramPlexLibraryUpdateNotification')
                    .should('be.checked');

                cy.get('#telegramGapsMissingCollectionsNotification')
                    .should('be.checked');

                cy.get('#telegramEnabled')
                    .should('have.value', 'true');
            });
    });

    it('Set TMDB Only Telegram Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TMDB_API_CONNECTION"],
            "chatId" : "chatId",
            "botId" : "botId"
        };

        cy.request('PUT', '/notifications/telegram',object)
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

                cy.get('#telegramBotId')
                    .should('have.value', 'botId');

                cy.get('#telegramChatId')
                    .should('have.value', 'chatId');

                cy.get('#telegramTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#telegramPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#telegramPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#telegramPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#telegramGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#telegramEnabled')
                    .should('have.value', 'true');
            });
    });


});
