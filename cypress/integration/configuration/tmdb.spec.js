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

describe('TMDB Configuration Tests', function () {
    before(function () {
        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});
    });
    it('Enter invalid TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .type('ABC123')
            .should('have.value', 'ABC123');

        cy.get('#testTmdbKey')
            .click();

        cy.get('#tmdbTestError')
            .should('be.visible');

        cy.get('#tmdbTestSuccess')
            .should('not.be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('not.be.visible');
    });

    it('Enter valid TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .type('723b4c763114904392ca441909aa0375')
            .should('have.value', '723b4c763114904392ca441909aa0375');

        cy.get('#testTmdbKey')
            .click();

        cy.get('#tmdbTestError')
            .should('not.be.visible');

        cy.get('#tmdbTestSuccess')
            .should('be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('not.be.visible');
    });

    it('Save invalid TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .type('ABC123')
            .should('have.value', 'ABC123');

        cy.get('#saveTmdbKey')
            .click();

        cy.get('#tmdbTestError')
            .should('not.be.visible');

        cy.get('#tmdbTestSuccess')
            .should('not.be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('be.visible');

        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#movieDbApiKey')
            .should('have.value', 'ABC123');
    });

    it('Save valid TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .type('723b4c763114904392ca441909aa0375')
            .should('have.value', '723b4c763114904392ca441909aa0375');

        cy.get('#saveTmdbKey')
            .click();

        cy.get('#tmdbTestError')
            .should('not.be.visible');

        cy.get('#tmdbTestSuccess')
            .should('not.be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('be.visible');

        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#movieDbApiKey')
            .should('have.value', '723b4c763114904392ca441909aa0375');
    });

    it('Attempt to save empty TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .should('have.value', '');

        cy.get('#saveTmdbKey')
            .click();

        cy.get('#emptyTmdbKeyLabel')
            .should('be.visible');

        cy.get('#tmdbTestError')
            .should('not.be.visible');

        cy.get('#tmdbTestSuccess')
            .should('not.be.visible');

        cy.get('#tmdbSaveError')
            .should('not.be.visible');

        cy.get('#tmdbSaveSuccess')
            .should('not.be.visible');
    });

    it('Attempt to test empty TMDB Key', () => {
        cy.get('#movieDbApiKey')
            .clear()
            .should('have.value', '');

        cy.get('#testTmdbKey')
            .click();

        cy.get('#emptyTmdbKeyLabel')
            .should('be.visible');

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

