import {redLibraryBefore, spyOnAddEventListener} from "../common";

describe('Not Searched Yet Recommended', function () {
    before(redLibraryBefore);

    it('Clean configuration page load', () => {
        cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#noMovieContainer > .card > .card-img-top')
            .should('be.visible');

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
            .should('have.text', 'Red - Movies');

    });
});
