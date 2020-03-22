import {nuke, spyOnAddEventListener} from "../common";

describe('Not Searched Yet RSS', function () {
    before(nuke);

    it('Clean configuration page load', () => {
        cy.visit('/rssCheck', {onBeforeLoad: spyOnAddEventListener});

        cy.get('.card-img-top')
            .should('be.visible');

        cy.get('.card-body')
            .should('be.visible');
    });

    it('Clean configuration page load', () => {
        cy.request('/rss?library=abc_1', {onBeforeLoad: spyOnAddEventListener})
            .should((response) => {
                expect(response.body).to.eq("No RSS feed found.")
            });
    });
});
