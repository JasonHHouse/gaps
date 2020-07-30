import {nuke, redLibraryBefore, searchPlexForMoviesFromSaw, spyOnAddEventListener} from "../common";

describe('Library API', function () {

    it('Get bad library', () => {
        cy.request('/libraries/abc/123')
            .then((resp) => {
                cy.log(resp.body);
                const result = resp.body;
                expect(result.code).to.eq(41);
            });
    })

    before(nuke);
    before(redLibraryBefore);

    it('Get library Red - Saw', () => {
        searchSawLibrary(cy);

        cy.request('/libraries/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
            .then((resp) => {
                cy.log(resp.body);
                const result = resp.body;
                expect(result.code).to.eq(40);
            });
    })
});

describe('Plex Movie List API', function () {
    beforeEach(nuke)
    beforeEach(redLibraryBefore);

    it('Get library Red - Saw', () => {
        cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
            .then((resp) => {
                cy.log(resp.body);
                const result = resp.body;
                expect(result).to.have.lengthOf(1);
                expect(result[0].imdbId).to.eq("tt0387564");
            });
    })

    it('Get library Red - Saw', () => {
        cy.request('/libraries/ABC/123')
            .then((resp) => {
                cy.log(resp.body);
                const result = resp.body;
                //expect(result).to.have.lengthOf(0);
            });
    })
});

function searchSawLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromSaw(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}
