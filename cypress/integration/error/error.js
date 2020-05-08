describe('Check Error Page', function () {

    it('Does error page load', () => {
        cy.request({url: '/bad', method: 'GET', failOnStatusCode: false})
            .should((response) => {
                expect(response.status).to.eq(404)
            });
    });

});
