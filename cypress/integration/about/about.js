import {spyOnAddEventListener} from "../common";

describe('Verify About Page', function () {

    it('Default page', () => {
        cy.visit('/about', {onBeforeLoad: spyOnAddEventListener});

        cy.get('h3.top-margin')
            .should('have.text', 'About');

        cy.get('.container > :nth-child(3)')
            .should('have.text', 'v0.7.1');

        cy.get('.container > :nth-child(6)')
            .should('have.text', 'Software');

        cy.get(':nth-child(8)')
            .should('have.text', 'License');

        cy.get(':nth-child(10)')
            .should('have.text', 'Support');
    });

});
