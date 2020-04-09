import {jokerLibraryBefore, redLibraryBefore, searchPlexForMovies, spyOnAddEventListener} from "../common";

describe('Find owned movies', function () {
    before(redLibraryBefore);

    it('Find Movies', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        searchPlexForMovies(cy);
    });

    it('Refresh Movies', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
            .click();

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();

        cy.get('label > input')
            .clear()
            .type('Saw');

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 1 of 1 entries');
    });

    it('Research Movies', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();

        cy.get('label > input')
            .clear()
            .type('Saw');

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 1 of 1 entries');

        cy.get('#movieContainer > .top-margin')
            .click();

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 1 of 1 entries');
    });

    it('Regular Movies Empty', () => {
        jokerLibraryBefore();

        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"][data-machineidentifier="721fee4db63634b88ed699f8b0a16d7682a7a0d9"]')
            .click();

        cy.get('.card-body > .btn')
            .should('be.visible');
    });
});
