import {redLibraryBefore, spyOnAddEventListener} from "../common";

describe('Not Searched Yet Library', function () {
    before(redLibraryBefore);

    it('Clean configuration page load', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#libraryTitle')
            .contains('Red');

        cy.get('#dropdownMenuLink')
            .should('have.text', 'Libraries');

        cy.get('[data-key="1"]')
            .should('have.text', 'Red - Movies with new Metadata');

        cy.get('.card-img-top')
            .should('be.visible');
    });
});
