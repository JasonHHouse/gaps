import {libraryBefore, searchPlexForDisneyMovies, spyOnAddEventListener} from "../common";

describe('Find owned movies', function () {
    before(libraryBefore);

    it('Find Disney Movies', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        searchPlexForDisneyMovies(cy);
    });

    it('Refresh Disney Movies', () => {
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
            .type('101 Dalmatians');

        cy.get('.col-md-10 > .card-body > .card-title')
            .should('have.text', '101 Dalmatians II Patch\'s London Adventure (2003)');
    });

    it('Regular Movies Empty', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
            .click();

        cy.get('.card-body > .btn')
            .should('be.visible');
    });
});
