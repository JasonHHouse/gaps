import {redLibraryBefore, searchPlexForMoviesFromBestMovies, searchPlexForMoviesFromMovies, searchPlexForMoviesFromSaw, spyOnAddEventListener} from "../common";

describe('Search for Duplicates', function () {
    before(redLibraryBefore);

    it('Check Best Movies and Recommended for Duplicates', () => {
        searchBestMovieLibrary(cy);

        cy.get('#dropdownMenuLink')
            .click();

        cy.get('[data-key="5"]')
            .click();

        cy.get('.card-body > .btn')
            .click();

        cy.wait(40000);

        let ownedMovies;
        cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/5')
            .then((resp) => {
                ownedMovies = resp.body;
                cy.log("ownedMovies.length: " + ownedMovies.length);
            }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/5')
            .then((resp) => {
                const recommendedMovies = resp.body.extras;
                checkForDuplicates(ownedMovies, recommendedMovies);
            });

    });

    it('Check Saw and Recommended for Duplicates', () => {
        cy.wait(40000);

        let ownedMovies;
        cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
            .then((resp) => {
                ownedMovies = resp.body;
                cy.log("ownedMovies.length: " + ownedMovies.length);
            }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
            .then((resp) => {
                const recommendedMovies = resp.body.extras;
                checkForDuplicates(ownedMovies, recommendedMovies);
            });

    });

    it('Check Movies and Recommended for Duplicates', () => {
        cy.wait(40000);

        let ownedMovies;
        cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
            .then((resp) => {
                ownedMovies = resp.body;
                cy.log("ownedMovies.length: " + ownedMovies.length);
            }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
            .then((resp) => {
                const recommendedMovies = resp.body.extras;
                checkForDuplicates(ownedMovies, recommendedMovies);
            });

    });

});

function checkForDuplicates(ownedMovies, recommendedMovies) {
    for (let recommendedMovie in recommendedMovies) {
        for (let ownedMovie in ownedMovies) {
            expect(recommendedMovies.tvdbId).to.not.eq(ownedMovie.tvdbId);
            expect(recommendedMovies.imdbId).to.not.eq(ownedMovie.imdbId);
        }
    }
}

function searchSawLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromSaw(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}

function searchtMovieLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}

function searchBestMovieLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromBestMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}