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

export function searchPlexForMovies(cy) {
    cy.get('#dropdownMenuLink')
        .click();

    cy.get('[data-key="1"]')
        .click();

    cy.get('.card-body > .btn')
        .click();

    cy.get('label > input')
        .clear()
        .type('Saw');

    cy.get('.col-md-10 > .card-body > .card-title')
        .should('have.text', 'Saw (2004)');
}

export function redLibraryBefore() {
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
        .type('192.168.1.9')
        .should('have.value', '192.168.1.9');

    cy.get('#port')
        .clear()
        .type('32400')
        .should('have.value', '32400');

    cy.get('#plexToken')
        .clear()
        .type('mQw4uawxTyYEmqNUrvBz')
        .should('have.value', 'mQw4uawxTyYEmqNUrvBz');

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
        .should('have.text', 'Red');

    cy.get('.list-group > :nth-child(1)')
        .should('have.text', 'Movies');
}

export function jokerLibraryBefore() {
    cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

    cy.get('#plexTab')
        .click();

    cy.get('#address')
        .clear()
        .type('192.168.1.8')
        .should('have.value', '192.168.1.8');

    cy.get('#port')
        .clear()
        .type('32400')
        .should('have.value', '32400');

    cy.get('#plexToken')
        .clear()
        .type('mQw4uawxTyYEmqNUrvBz')
        .should('have.value', 'mQw4uawxTyYEmqNUrvBz');

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
}