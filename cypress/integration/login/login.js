import {spyOnAddEventListener} from "../common";

describe('Verify Log In Page', function () {

    it('Default page', () => {
        cy.visit('/login', {onBeforeLoad: spyOnAddEventListener});

        cy.get(':nth-child(1) > label')
            .should('have.text', 'Username');

        cy.get(':nth-child(2) > label')
            .should('have.text', 'Password');

        cy.get('.btn')
            .should('have.text', 'Log In');

        cy.get('#username')
            .clear()
            .type('badUser')
            .should('have.value', 'badUser');

        cy.get('#password')
            .clear()
            .type('password')
            .should('have.value', 'password');

        cy.get('.btn')
            .click();

        cy.get('.card-img-top')
            .should('be.visible');
    });

});
