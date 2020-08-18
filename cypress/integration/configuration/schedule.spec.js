import {spyOnAddEventListener} from '../common.js';

describe('Schedule Configuration Tests', function () {
    before(function () {
        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        cy.get('#scheduleTab')
            .click();
    });

    it('Attempt to save empty monthly schedule', () => {
        cy.get('#setSchedule')
            .select('Monthly');

        cy.get('#saveSchedule')
            .click();

        cy.get('#scheduleSaveSuccess')
            .should('be.visible');

        cy.get('#scheduleSaveError')
            .should('not.be.visible');

        cy.request('/schedule')
            .then((resp) => {
                const result = resp.body;
                expect(result).to.have.property('message', 'Monthly');
            });
    });

    it('Attempt to save empty hourly schedule', () => {
        cy.get('#setSchedule')
            .select('Hourly');

        cy.get('#saveSchedule')
            .click();

        cy.get('#scheduleSaveSuccess')
            .should('be.visible');

        cy.get('#scheduleSaveError')
            .should('not.be.visible');

        cy.request('/schedule')
            .then((resp) => {
                const result = resp.body;
                expect(result).to.have.property('message', 'Hourly');
            });
    });
});