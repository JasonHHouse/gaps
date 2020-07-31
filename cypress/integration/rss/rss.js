import {nuke, redLibraryBefore, searchPlexForMoviesFromSaw, spyOnAddEventListener} from "../common";

describe('Searched RSS', function () {
    before(nuke)
    before(redLibraryBefore);

    it('Get full RSS for Red', () => {
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

        cy.visit('/rssCheck', {onBeforeLoad: spyOnAddEventListener});

        cy.request('/rss/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
            .then((resp) => {
                const result = resp.body;
                expect(result).to.have.lengthOf(7);
                expect(result[0].imdb_id).to.eq('tt3348730')
            });
    })
});

function searchSawLibrary(cy) {
    cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

    searchPlexForMoviesFromSaw(cy);

    cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});
}
