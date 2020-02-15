import {redLibraryBefore, searchPlexForMovies, spyOnAddEventListener} from "../common";

describe('Search for Recommended', function () {
    before(redLibraryBefore);

    it('Clean configuration page load', () => {
        searchDisneyLibrary(cy);

        cy.get('#noMovieContainer > .card > .card-img-top')
            .should('not.be.visible');

    });
});

function searchDisneyLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}