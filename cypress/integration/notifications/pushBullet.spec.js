import {nuke} from "../common";

describe('Check PushBullet Notification Agent', function () {
    before(nuke);

    it('Check for Empty PushBullet Notification Agent Settings', () => {

        cy.request('/notifications/pushbullet')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(102);
                expect(body.extras).to.eq(null);
            });
    });

    it('Set PushBullet Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST"],
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
            });
    });

});