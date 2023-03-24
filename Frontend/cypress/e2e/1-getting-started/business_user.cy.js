/// <reference types="cypress" />
const { getMaxListeners } = require("process")
const { createEmitAndSemanticDiagnosticsBuilderProgram } = require("typescript")
describe('business user', () => {
    beforeEach(() => {
      cy.visit('http://cultureindahouse.westeurope.cloudapp.azure.com/')
      cy.get(".mdi-account-circle-outline").click()
      cy.contains("Correo").parent().type("business1@fakemail.com")
      cy.contains("Contraseña").parent().type("admin")
      cy.get("[type=submit]").click()
      Cypress.on('uncaught:exception', (err, runnable) => {
        return false
    })
    })

    it('Manages company', () => {
        cy.contains("Gestionar empresa").click()
        cy.contains("Teatro Nacional de Cataluña")
    })
    it('add event', ()=>{
        cy.contains("Añadir evento").click()
        cy.contains("Nombre").parent().type("Ejemplo2")
        cy.contains("Descripción").parent().type("Esto es un ejemplo2")
        cy.contains("Precio").parent().type("20")
        cy.contains("Capacidad").parent().type("500")
        cy.contains("Duración").parent().type("02:00")
        cy.contains("Fecha").parent().type("2023-01-22")
        cy.contains("Categoría").parent().click()
        cy.contains("Teatro").click()
        cy.get("[type=file]").selectFile("C:/Users/cenoz/OneDrive/Escritorio/ejemplo.png", { force: true })
        cy.get("span.v-btn__content").contains("C").click()
        cy.wait(2000)
        cy.contains("Ejemplo2")
    })
    it('logout', ()=>{
        cy.get(".mdi-logout").click()
    })
})