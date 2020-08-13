import {nuke} from "../common";

describe('Check PushBullet Notification Agent', function () {
    before(nuke);

    it('Check for Empty PushBullet Notification Agent Settings', () => {

        cy.request('/notifications/pushbullet')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(102);
                expect(body.extras).to.eq(null);
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#pushBulletChannelTag')
                    .should('have.value', '');

                cy.get('#pushBulletAccessToken')
                    .should('have.value', '');

                cy.get('#pushBulletTmdbApiConnectionNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletEnabled')
                    .should('have.value', 'false');
            });
    });

    it('Set PushBullet Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST", "TMDB_API_CONNECTION", "PLEX_SERVER_CONNECTION", "PLEX_METADATA_UPDATE", "PLEX_LIBRARY_UPDATE", "GAPS_MISSING_COLLECTIONS"],
            "channel_tag" : "channel_tag",
            "accessToken" : "accessToken"
        };

        cy.request('PUT', '/notifications/pushbullet',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(100);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/pushbullet')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(102);
                expect(body.extras.channel_tag).to.eq("channel_tag");
                expect(body.extras.accessToken).to.eq("accessToken");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#pushBulletChannelTag')
                    .should('have.value', 'channel_tag');

                cy.get('#pushBulletAccessToken')
                    .should('have.value', 'accessToken');

                cy.get('#pushBulletTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#pushBulletPlexServerConnectionNotification')
                    .should('be.checked');

                cy.get('#pushBulletPlexMetadataUpdateNotification')
                    .should('be.checked');

                cy.get('#pushBulletPlexLibraryUpdateNotification')
                    .should('be.checked');

                cy.get('#pushBulletGapsMissingCollectionsNotification')
                    .should('be.checked');

                cy.get('#pushBulletEnabled')
                    .should('have.value', 'true');
            });
    });

    it('Set TMDB Only PushBullet Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TMDB_API_CONNECTION"],
            "channel_tag" : "channel_tag",
            "accessToken" : "accessToken"
        };

        cy.request('PUT', '/notifications/pushbullet',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(100);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/pushbullet')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(102);
                expect(body.extras.channel_tag).to.eq("channel_tag");
                expect(body.extras.accessToken).to.eq("accessToken");
            })
            .visit('/configuration')
            .then((resp) => {
                cy.get('#notificationTab')
                    .click();

                cy.get('#pushBulletChannelTag')
                    .should('have.value', 'channel_tag');

                cy.get('#pushBulletAccessToken')
                    .should('have.value', 'accessToken');

                cy.get('#pushBulletTmdbApiConnectionNotification')
                    .should('be.checked');

                cy.get('#pushBulletPlexServerConnectionNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletPlexMetadataUpdateNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletPlexLibraryUpdateNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletGapsMissingCollectionsNotification')
                    .should('not.be.checked');

                cy.get('#pushBulletEnabled')
                    .should('have.value', 'true');
            });
    });
});