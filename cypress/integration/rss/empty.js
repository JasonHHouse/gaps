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
});