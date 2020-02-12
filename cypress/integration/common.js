let appHasStarted;

export function spyOnAddEventListener(win) {
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
    };
}

export function searchPlexForDisneyMovies(cy) {
    cy.get('#dropdownMenuLink')
        .click();

    cy.get('[data-key="2"]')
        .click();

    cy.get('.card-body > .btn')
        .click();

    cy.get('label > input')
        .clear()
        .type('101 Dalmatians');

    cy.get('.col-md-10 > .card-body > .card-title')
        .should('have.text', '101 Dalmatians II Patch\'s London Adventure (2003)');
}

export function libraryBefore() {
    cy.request('PUT', '/nuke/jhouse')
        .then((response) => {
            expect(response.body).to.have.property('code', 30);
            expect(response.body).to.have.property('reason', 'Nuke successful. All files deleted.');
        });

    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

    cy.get('#plexTab')
        .click();

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
}