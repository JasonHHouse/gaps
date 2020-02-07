let appHasStarted;

function spyOnAddEventListener(win) {
    // win = window object in our application
    const addListener = win.EventTarget.prototype.addEventListener;
    win.EventTarget.prototype.addEventListener = function (name) {
        if (name === 'change') {
            // web app added an event listener to the input box -
            // that means the web application has started
            appHasStarted = true;
            // restore the original event listener
            win.EventTarget.prototype.addEventListener = addListener;
        }
        return addListener.apply(this, arguments);
    }
}

describe('Hooks', function () {
    before(function () {
        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#plexTab')
            .click();

        //Remove existing server before each test
        cy.get("body").then($body => {
            if ($body.find("[onclick=\"removePlexServer(this.getAttribute('data-machineIdentifier'))\"]").length > 0) {
                cy.get("[onclick=\"removePlexServer(this.getAttribute('data-machineIdentifier'))\"]")
                    .each(($el) => {
                        cy.wrap($el).click();
                    });
            }
        });
    });

    it('Test invalid new Plex Server', () => {
        cy.get('#address')
            .clear()
            .type('111.222.121.212')
            .should('have.value', '111.222.121.212');

        cy.get('#port')
            .clear()
            .type('11212')
            .should('have.value', '11212');

        cy.get('#plexToken')
            .clear()
            .type('123qwe')
            .should('have.value', '123qwe');

        cy.get('#testPlexServer')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');
    });

    it('Test valid new Plex Server', () => {
        cy.get('#address')
            .clear()
            .type('174.58.64.67')
            .should('have.value', '174.58.64.67');

        cy.get('#port')
            .clear()
            .type('32400')
            .should('have.value', '32400');

        cy.get('#plexToken')
            .clear()
            .type('xPUCxLh4cTz8pcgorQQs')
            .should('have.value', 'xPUCxLh4cTz8pcgorQQs');

        cy.get('#testPlexServer')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');
    });


    it('Save invalid Plex Server', () => {
        cy.get('#address')
            .clear()
            .type('111.222.121.212')
            .should('have.value', '111.222.121.212');

        cy.get('#port')
            .clear()
            .type('11212')
            .should('have.value', '11212');

        cy.get('#plexToken')
            .clear()
            .type('123qwe')
            .should('have.value', '123qwe');

        cy.get('#addPlexServer')
            .click();

        //Wait for timeout from plex
        cy.wait(1000);

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');
    });

    it('Save valid Plex Server', () => {
        cy.get('#address')
            .clear()
            .type('174.58.64.67')
            .should('have.value', '174.58.64.67');

        cy.get('#port')
            .clear()
            .type('32400')
            .should('have.value', '32400');

        cy.get('#plexToken')
            .clear()
            .type('xPUCxLh4cTz8pcgorQQs')
            .should('have.value', 'xPUCxLh4cTz8pcgorQQs');

        cy.get('#addPlexServer')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');

        //Define card here
        cy.get('.card-header')
            .should('have.text', 'KnoxServer');

        cy.get('.list-group > :nth-child(1)')
            .should('have.text', 'Disney Classic Movies');

        cy.get('.list-group > :nth-child(2)')
            .should('have.text', 'Movies');

    });

    it('Test valid existing Plex Server', () => {
        cy.get('#testExisting')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');
    });

    it('Remove valid existing Plex Server', () => {
        cy.get('#removeExisting')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');
    });

    it('Save duplicate valid Plex Server', () => {
        cy.get('#address')
            .clear()
            .type('174.58.64.67')
            .should('have.value', '174.58.64.67');

        cy.get('#port')
            .clear()
            .type('32400')
            .should('have.value', '32400');

        cy.get('#plexToken')
            .clear()
            .type('xPUCxLh4cTz8pcgorQQs')
            .should('have.value', 'xPUCxLh4cTz8pcgorQQs');

        cy.get('#addPlexServer')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('not.be.visible');

        //Define card here
        cy.get('.card-header')
            .should('have.text', 'KnoxServer');

        cy.get('.list-group > :nth-child(1)')
            .should('have.text', 'Disney Classic Movies');

        cy.get('.list-group > :nth-child(2)')
            .should('have.text', 'Movies');

        cy.get('#address')
            .clear()
            .type('174.58.64.67')
            .should('have.value', '174.58.64.67');

        cy.get('#port')
            .clear()
            .type('32400')
            .should('have.value', '32400');

        cy.get('#plexToken')
            .clear()
            .type('xPUCxLh4cTz8pcgorQQs')
            .should('have.value', 'xPUCxLh4cTz8pcgorQQs');

        cy.get('#addPlexServer')
            .click();

        cy.get('#plexSpinner')
            .should('not.be.visible');

        cy.get('#plexTestError')
            .should('not.be.visible');

        cy.get('#plexTestSuccess')
            .should('not.be.visible');

        cy.get('#plexSaveError')
            .should('not.be.visible');

        cy.get('#plexSaveSuccess')
            .should('not.be.visible');

        cy.get('#plexDeleteError')
            .should('not.be.visible');

        cy.get('#plexDeleteSuccess')
            .should('not.be.visible');

        cy.get('#plexDuplicateError')
            .should('be.visible');

    });
});


/*

it('Save invalid TMDB Key', () => {
    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

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
    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

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
    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

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
    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

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

*/
