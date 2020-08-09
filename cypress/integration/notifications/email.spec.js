import {nuke} from "../common";

describe('Check Email Notification Agent', function () {
    before(nuke);

    it('Check for Email Gotify Notification Agent Settings', () => {

        cy.request('/notifications/email')
            .then((resp) => {
                let body = resp.body;
                expect(body.code).to.eq(122);
                expect(body.extras).to.eq(null);
            });
    });

    it('Set Email Notification Agent Settings', () => {

        let object = {
            "enabled" : true,
            "notificationTypes" : ["TEST"],
            "username" : "username",
            "password" : "password",
            "mailTo" : "mailTo",
            "mailFrom" : "mailFrom",
            "mailServer":  "mailServer",
            "mailPort" : 12345,
            "mailTransportProtocol" : "mailTransportProtocol",
            "mailSmtpAuth" : "mailSmtpAuth",
            "mailSmtpTlsEnabled" : true
        };

        cy.request('PUT', '/notifications/email',object)
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
            });
    });

});