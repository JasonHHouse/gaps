import {redLibraryBefore, searchPlexForMoviesFromMovies, spyOnAddEventListener} from "../common";

describe('Search for Recommended Best Movies', function () {
    beforeEach(redLibraryBefore);

    it('Clean configuration page load', () => {
        searchMoviesLibrary(cy);

        cy.get('#libraryTitle').then( ($libraryTitle) => {
            if($libraryTitle.text() !== "Movies") {
                cy.get('#dropdownMenuLink')
                    .click();
                cy.get('[data-key="1"]')
                    .click();
            }
        });

        cy.get('#noMovieContainer > .card > .card-img-top')
            .should('not.be.visible');

    });

    it('Search Movies with bad chars', () => {
        searchMoviesLibrary(cy);

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
            .click();

        cy.get('.card-body > .btn')
            .click();

        cy.wait(5000);

        cy.get('#movies_info')
            .should('have.text', 'Showing 1 to 7 of 7 entries');
    });

    it('Research Movies with bad chars', () => {
        searchMoviesLibrary(cy);

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="1"]')
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

        cy.get(':nth-child(1) > td > .card > .row > .col-md > .card-body > h5.card-title')
            .should('have.text', '2010 (1984)');
    });
});

function searchMoviesLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}