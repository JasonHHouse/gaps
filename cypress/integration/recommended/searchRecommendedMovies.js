import {redLibraryBefore, searchPlexForMovies, spyOnAddEventListener} from "../common";

describe('Search for Recommended', function () {
    beforeEach(redLibraryBefore);

    it('Clean configuration page load', () => {
        searchSawLibrary(cy);

        cy.get('#libraryTitle').then( ($libraryTitle) => {
            if($libraryTitle.text() !== "Red - Saw") {
                cy.get('#dropdownMenuLink').click();
                cy.get('[data-key="2"]').click();
            }
        });

        cy.get('#noMovieContainer > .card > .card-img-top')
            .should('not.be.visible');

    });

    it('Search Movies', () => {
        searchSawLibrary(cy);

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();

        cy.get('.card-body > .btn')
            .click();

        cy.wait(5000);

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 7 of 7 entries');
    });

    it('Research Movies', () => {
        searchSawLibrary(cy);

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="2"]')
            .click();

        cy.get('.card-body > .btn')
            .click();

        cy.wait(5000);

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 7 of 7 entries');

        cy.get('#movieContainer > [onclick="searchForMovies()"]')
            .click();

        cy.wait(5000);

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 7 of 7 entries');
    });
});

function searchSawLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}