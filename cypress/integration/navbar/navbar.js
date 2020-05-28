import {spyOnAddEventListener} from "../common";

describe('Verify Navbars', function () {

    it('Index page', () => {
        cy.visit('/home', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy);
    });

    it('About page', () => {
        cy.visit('/about', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, undefined, undefined, undefined, undefined, undefined, false);
    });

    it('Settings page', () => {
        cy.visit('/configuration', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, undefined, undefined, undefined, false, undefined, undefined);
    });

    it('Libraries page', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, false, undefined, undefined, undefined, undefined, undefined);
    });

    it('Libraries page', () => {
        cy.visit('/libraries', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, false, undefined, undefined, undefined, undefined, undefined);
    });

    it('Recommended page', () => {
        cy.visit('/recommended', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, undefined, false, undefined, undefined, undefined, undefined);
    });

    it('RSS page', () => {
        cy.visit('/rssCheck', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, undefined, undefined, false, undefined, undefined, undefined);
    });

    it('Updates page', () => {
        cy.visit('/updates', {onBeforeLoad: spyOnAddEventListener});

        checkNavIcons(cy, undefined, undefined, undefined, undefined, false, undefined);
    });
});

function checkNavIcons(cy, isLibDefault = true, isMissingDefault = true, isRssDefault = true,
                       isSettingsDefault = true, isUpdatesDefault = true, isAboutDefault = true) {
    cy.get(':nth-child(1) > .nav-link > .icon')
        .should('have.class', isLibDefault ? 'list-ul-default' : 'list-ul-active');

    cy.get(':nth-child(2) > .nav-link > .icon')
        .should('have.class', isMissingDefault ? 'collection-fill-default' : 'collection-fill-active');

    cy.get(':nth-child(3) > .nav-link > .icon')
        .should('not.be.visible');

    cy.get(':nth-child(4) > .nav-link > .icon')
        .should('have.class', isRssDefault ? 'rss-default' : 'rss-active');

    cy.get(':nth-child(5) > .nav-link > .icon')
        .should('have.class', isSettingsDefault ? 'gear-default' : 'gear-active');

    cy.get(':nth-child(6) > .nav-link > .icon')
        .should('have.class', isUpdatesDefault ? 'arrow-clockwise-default' : 'arrow-clockwise-active');

    cy.get(':nth-child(7) > .nav-link > .icon')
        .should('have.class', isAboutDefault ? 'info-circle-default' : 'info-circle-active');
}
