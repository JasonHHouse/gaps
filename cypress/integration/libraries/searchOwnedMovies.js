import {libraryBefore} from '../common.js';

describe('Find owned movies', function () {
    before(libraryBefore);

    it('Find Disney Movies', () => {
        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();

        cy.get('.card-body > .btn')
            .click();

        cy.get(':nth-child(1) > .sorting_1 > .card > .row > .col-md-10 > .card-body > .card-title')
            .should('have.text', '101 Dalmatians II Patch\'s London Adventure (2003)');

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
            .click();

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();
    });
});
