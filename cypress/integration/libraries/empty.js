let appHasStarted;

function spyOnAddEventListener(win) {
    // win = window object in our application
    const addListener = win.EventTarget.prototype.addEventListener
    win.EventTarget.prototype.addEventListener = function (name) {
        if (name === 'change') {
            // web app added an event listener to the input box -
            // that means the web application has started
            appHasStarted = true
            // restore the original event listener
            win.EventTarget.prototype.addEventListener = addListener
        }
        return addListener.apply(this, arguments)
    }
}

describe('Not Searched Yet Library Tests', function () {
    it('Clean configuration page load', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        //Switch to contains
        /*cy.get('#libraryTitle')
            .should('have.text', 'KnoxServer - Movies');*/

        cy.get('#dropdownMenuLink')
             .should('have.text', 'Libraries');

        cy.get('[data-key="1"]')
            .should('have.text', 'KnoxServer - Movies');

        cy.get('[data-key="2"]')
            .should('have.text', 'KnoxServer - Disney Classic Movies');

        cy.get('.card-img-top')
            .should('be.visible');
    });
});