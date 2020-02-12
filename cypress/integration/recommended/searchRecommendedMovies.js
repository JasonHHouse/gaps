import {libraryBefore, searchPlexForDisneyMovies, spyOnAddEventListener} from "../common";

describe('Search for Recommended', function () {
    before(libraryBefore);

    it('Clean configuration page load', () => {
        searchDisneyLibrary(cy);

        cy.get('#noMovieContainer > .card > .card-img-top')
            .should('not.be.visible');

    });
});

function searchDisneyLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForDisneyMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});

}