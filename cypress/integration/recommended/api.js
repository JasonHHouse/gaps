import {nuke, redLibraryBefore, searchPlexForMoviesFromSaw, spyOnAddEventListener} from "../common";

describe('Recommended API', function () {

    it('Get Bad recommended', () => {
        cy.request('/recommended/a/2')
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

function searchSawLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromSaw(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}
