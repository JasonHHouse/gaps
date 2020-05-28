import {nuke} from "../common";

let appHasStarted;

function spyOnAddEventListener(win) {
    // win = window object in our application
    const addListener = win.EventTarget.prototype.addEventListener
    win.EventTarget.prototype.addEventListener = function (name) {
        if (name === 'change') {
            // web app added an event listener to the input box -
            // that means the web application has started
            appHasStarted = true;
            // restore the original event listener
            win.EventTarget.prototype.addEventListener = addListener
        }
        return addListener.apply(this, arguments)
    }
}

describe('Configuration Tests', function () {
    it('Clean configuration page load', () => {
        nuke();

        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        cy.get('.active > .nav-link')
            .should('have.attr', 'href', '/configuration')
            .parent()
            .should('have.attr', 'aria-current', 'page');

        cy.get('.navbar-nav > :nth-child(1) > .nav-link')
            .should('have.attr', 'href', '/libraries')
            .parent()
            .should('not.have.attr', 'aria-current', 'page');

        cy.get('.navbar-nav > :nth-child(2) > .nav-link')
            .should('have.attr', 'href', '/recommended')
            .parent()
            .should('not.have.attr', 'aria-current', 'page');

        cy.get('.navbar-nav > :nth-child(4) > .nav-link')
            .should('have.attr', 'href', '/rssCheck')
            .parent()
            .should('not.have.attr', 'aria-current', 'page');

        cy.get('.navbar-nav > :nth-child(7) > .nav-link')
            .should('have.attr', 'href', '/about')
            .parent()
            .should('not.have.attr', 'aria-current', 'page');

        cy.get('#tmdbTab')
            .should('have.class', 'active');

        cy.get('#plexTab')
            .should('not.have.class', 'active');

        cy.get('#folderTab')
            .should('have.class', 'disabled');

        cy.get('#tmdbTestError')
            .should('not.be.visible');

        cy.get('#tmdbTestSuccess')
            .should('not.be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('not.be.visible');
    });
});