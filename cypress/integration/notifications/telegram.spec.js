import {nuke} from "../common";

describe('Check Telegram Notification Agent', function () {
    before(nuke);

    it('Check for Empty Telegram Notification Agent Settings', () => {

        cy.request('/notifications/telegram')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(82);
                expect(body.extras).to.eq(null);
            });
    });

    it('Set Telegram Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST"],
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
            });
    });

});
