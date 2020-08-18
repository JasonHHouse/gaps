import {redLibraryBefore, searchPlexForMoviesFromBestMovies, spyOnAddEventListener} from "../common";

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

        waitUtilSearchingIsDone();

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
        waitUtilSearchingIsDone();

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
        waitUtilSearchingIsDone();

        let ownedMovies;
        cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
            .then((resp) => {
                ownedMovies = resp.body;
            }).request('/recommended/721fee4db63634b88ed699f8b0a16d7682a7a0d9/1')
            .then((resp) => {
                const recommendedMovies = resp.body.extras;
                checkForDuplicates(ownedMovies, recommendedMovies);
            });

    });

});

function checkForDuplicates(ownedMovies, recommendedMovies) {
    cy.log("recommendedMovies.length: " + recommendedMovies.length);
    cy.log("ownedMovies.length: " + ownedMovies.length);

    for (const recommendedMovie in recommendedMovies) {
        for (const ownedMovie in ownedMovies) {
            if(recommendedMovie.tvdbId === ownedMovie.tvdbId
                || recommendedMovie.imdbId === ownedMovie.imdbId) {
                cy.log("Recommended Movie: " + recommendedMovie);
                cy.log("Owned Movie: " + ownedMovie);
            }

            if((recommendedMovie.tvdbId !== undefined && ownedMovie.tvdbId !== undefined)) {
                expect(recommendedMovie.tvdbId).to.not.eq(ownedMovie.tvdbId);
            }

            if((recommendedMovie.imdbId !== undefined && ownedMovie.imdbId !== undefined)) {
                expect(recommendedMovie.imdbId).to.not.eq(ownedMovie.imdbId);
            }
        }
    }
}

function searchBestMovieLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromBestMovies(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}

function waitUtilSearchingIsDone() {
    cy.request('/searchStatus')
        .then((resp) => {

            if (resp.status === 200 && resp.body.isSearching === false) {
                return;
            }
            // else recurse
            waitUtilSearchingIsDone()
        });
}