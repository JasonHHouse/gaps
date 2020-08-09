import {nuke} from "../common";

describe('Check Slack Notification Agent', function () {
    before(nuke);

    it('Check for Empty Slack Notification Agent Settings', () => {

        cy.request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras).to.eq(null);
            });
    });

    it('Set Slack Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST"],
            "webHookUrl" : "webHookUrl"
        };

        cy.request('PUT', '/notifications/slack',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(90);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/slack')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(92);
                expect(body.extras.webHookUrl).to.eq("webHookUrl");
            });
    });

});