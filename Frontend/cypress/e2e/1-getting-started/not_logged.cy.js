/// <reference types="cypress" />


describe('not-logged', () => {
    beforeEach(() => {
      // Código que se ejecuta antes de cada test
      cy.visit('http://cultureindahouse.westeurope.cloudapp.azure.com/')
      Cypress.on('uncaught:exception', (err, runnable) => {
        // returning false here prevents Cypress from
        // failing the test
        return false
    })
    })
  
    it('displays five shows by default', () => {
      cy.get('.pa-4').should('have.length', 5)
      cy.get('.pa-4').get("h2").first().should('have.text', 'El lago de los cisnes')
      cy.get('.pa-4').get("h2").last().should('have.text', 'Sinfonía n.º 9 de Dvorak')
    })

    it('search bar works', () => {
      cy.contains('Buscar').parent().type(`c`)
      cy.get("[id=input-17]").parent().click()
      cy.get(".v-menu__content").contains("Teatro").click()
      cy.get("[id=input-17]").blur()
      cy.contains("Cats").click()

      cy.get('.pa-4').should('have.length', 2)
      cy.get('.pa-4').get("h2").first().should('have.text', 'Cats')
      cy.get('.pa-4').get("h2").last().should('have.text', 'Macbeth')
    })
  })
  