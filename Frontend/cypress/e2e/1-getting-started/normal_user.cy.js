/// <reference types="cypress" />
const { getMaxListeners } = require("process")
describe('usuario normal', () => {
    beforeEach(() => {
      cy.visit('http://cultureindahouse.westeurope.cloudapp.azure.com/')
      Cypress.on('uncaught:exception', (err, runnable) => {
        return false
    })
    })
    it('register', () => {
        cy.get(".mdi-account-circle-outline").click()
        cy.contains("Registrate").click()
        cy.contains("Nombre").parent().type("Juán")
        cy.contains("Apellidos").parent().type("Gutierrez Rodriguez")
        cy.contains("Email").parent().type("example.email@gmail.com")
        cy.contains("Contraseña").parent().type("JuGuRo23_*")
        cy.contains("Repite").parent().type("JuGuRo23_*")
        cy.get("[type=submit]").click()
        cy.on ('window:alert', (text) => {
            expect(text).to.eq('Te has registrado correctamente')
            done()                                
          })
    })
    it('login', () => {
      cy.get(".mdi-account-circle-outline").click()
      cy.contains("Correo").parent().type("example.email@gmail.com")
      cy.contains("Contraseña").parent().type("JuGuRo23_*")
      cy.get("[type=submit]").click()
      cy.contains("Juán Gutierrez Rodriguez")
      cy.get(".mdi-logout").click()
    })
  })
  