import {CYPRESS_VALUES, nuke} from "../common";

describe('Check PushBullet Notification Agent', function () {
    beforeEach(nuke);

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

    it('Check saving PushBullet Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '',CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, 'false');

        cy.get('#pushBulletChannelTag')
            .clear()
            .type('channelTag')
            .should('have.value', 'channelTag');

        cy.get('#pushBulletAccessToken')
            .clear()
            .type('accessToken')
            .should('have.value', 'accessToken');

        cy.get('#pushBulletTmdbApiConnectionNotification')
            .click();

        cy.get('#pushBulletPlexServerConnectionNotification')
            .click();

        cy.get('#pushBulletPlexMetadataUpdateNotification')
            .click();

        cy.get('#pushBulletPlexLibraryUpdateNotification')
            .click();

        cy.get('#pushBulletGapsMissingCollectionsNotification')
            .click();

        cy.get('#pushBulletEnabled')
            .select('Enabled');

        cy.get('#savePushBullet')
            .click();

        cy.get('#pushBulletTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushBulletSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check saving and test PushBullet Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '', CYPRESS_VALUES.notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#pushBulletChannelTag')
            .clear()
            .type(atob('Z2Fwcw=='))
            .should('have.value', atob('Z2Fwcw=='));

        cy.get('#pushBulletAccessToken')
            .clear()
            .type(atob('by5pdjBXbXRuTFdJM3hmaFRTTmh1dVF2Tm1vdlVGSHN6dA=='))
            .should('have.value', atob('by5pdjBXbXRuTFdJM3hmaFRTTmh1dVF2Tm1vdlVGSHN6dA=='));

        cy.get('#pushBulletTmdbApiConnectionNotification')
            .click();

        cy.get('#pushBulletPlexServerConnectionNotification')
            .click();

        cy.get('#pushBulletPlexMetadataUpdateNotification')
            .click();

        cy.get('#pushBulletPlexLibraryUpdateNotification')
            .click();

        cy.get('#pushBulletGapsMissingCollectionsNotification')
            .click();

        cy.get('#pushBulletEnabled')
            .select('Enabled');

        cy.get('#savePushBullet')
            .click();

        cy.get('#pushBulletTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushBulletSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testPushBullet')
            .click();

        cy.get('#pushBulletTestSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushBulletTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });

    it('Check Successful Saving and Failure Testing PushBullet Notification', () => {

        cy.visit('/configuration');

        cy.get('#notificationTab')
            .click();

        checkElements('', '',CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked,CYPRESS_VALUES. notBeChecked, CYPRESS_VALUES.notBeChecked, 'false');

        cy.get('#pushBulletChannelTag')
            .clear();

        cy.get('#pushBulletAccessToken')
            .clear();

        cy.get('#pushBulletTmdbApiConnectionNotification')
            .click();

        cy.get('#pushBulletPlexServerConnectionNotification')
            .click();

        cy.get('#pushBulletPlexMetadataUpdateNotification')
            .click();

        cy.get('#pushBulletPlexLibraryUpdateNotification')
            .click();

        cy.get('#pushBulletGapsMissingCollectionsNotification')
            .click();

        cy.get('#pushBulletEnabled')
            .select('Enabled');

        cy.get('#savePushBullet')
            .click();

        cy.get('#pushBulletTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletTestError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveSuccess')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushBulletSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSpinner')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#testPushBullet')
            .click();

        cy.get('#pushBulletTestSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletTestError')
            .should(CYPRESS_VALUES.beVisible);

        cy.get('#pushBulletSaveSuccess')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSaveError')
            .should(CYPRESS_VALUES.notBeVisible);

        cy.get('#pushBulletSpinner')
            .should(CYPRESS_VALUES.notBeVisible);
    });
});

function checkElements(channelTag, token, tmdbApi, plexServer, plexMetadata, plexLibrary, gapsCollections, enabled) {
    cy.get('#pushBulletChannelTag')
        .should('have.value', channelTag);

    cy.get('#pushBulletAccessToken')
        .should('have.value', token);

    cy.get('#pushBulletTmdbApiConnectionNotification')
        .should(tmdbApi);

    cy.get('#pushBulletPlexServerConnectionNotification')
        .should(plexServer);

    cy.get('#pushBulletPlexMetadataUpdateNotification')
        .should(plexMetadata);

    cy.get('#pushBulletPlexLibraryUpdateNotification')
        .should(plexLibrary);

    cy.get('#pushBulletGapsMissingCollectionsNotification')
        .should(gapsCollections);

    cy.get('#pushBulletEnabled')
        .should('have.value', enabled);
}