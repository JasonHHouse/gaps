import {nuke} from "../common";

describe('Check Gotify Notification Agent', function () {
    before(nuke);

    it('Check for Empty Gotify Notification Agent Settings', () => {

        cy.request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras).to.eq(null);
            });
    });

    it('Set Gotify Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST"],
            "address" : "address",
            "token" : "token"
        };

        cy.request('PUT', '/notifications/gotify',object)
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(110);
                expect(body.extras).to.eq(null);
            })
            .request('/notifications/gotify')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(112);
                expect(body.extras.address).to.eq("address");
                expect(body.extras.token).to.eq("token");
            });
    });

});