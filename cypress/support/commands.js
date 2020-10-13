// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add('populateTmdb', (key) => {
  if (key) {
    cy.get('[data-cy=movieDbApiKey]')
      .clear()
      .type(key)
      .should('have.value', key);
  } else {
    cy.get('[data-cy=movieDbApiKey]')
      .clear()
      .should('have.value', '');
  }
});

Cypress.Commands.add('populatePlexConfiguration', (address, port, token) => {
  cy.get('#address')
    .clear()
    .type(address)
    .should('have.value', address);

  cy.get('#port')
    .clear()
    .type(port)
    .should('have.value', port);

  cy.get('#plexToken')
    .clear()
    .type(token)
    .should('have.value', token);
})