package cl.corpaliv.albalab.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ValidacionesTest {
    @Test
    fun registroInvalidoMuestraErroresPorCampo() {
        val errores = Validaciones.registro(RegistroDatos(
            nombre = "A", correo = "no-es-correo", clave = "abc", confirmar = "xyz",
            tipo = "Persona", acepta = false
        ))
        assertTrue(errores.containsKey("nombre"))
        assertTrue(errores.containsKey("correo"))
        assertTrue(errores.containsKey("clave"))
        assertTrue(errores.containsKey("confirmar"))
        assertTrue(errores.containsKey("acepta"))
    }

    @Test
    fun registroValidoNoTieneErrores() {
        val errores = Validaciones.registro(RegistroDatos(
            nombre = "Empresa Demo", correo = "demo@ejemplo.cl", clave = "Clave1234",
            confirmar = "Clave1234", tipo = "Empresa", acepta = true
        ))
        assertTrue(errores.isEmpty())
    }

    @Test
    fun solicitudInvalidaDetectaParticipantesYFecha() {
        val errores = Validaciones.solicitud(SolicitudDatos(
            modalidad = "Donación", motivo = "", participantes = "0",
            fecha = "2020-01-01", acepta = false
        ), LocalDate.of(2026, 10, 21))
        assertTrue(errores.containsKey("participantes"))
        assertTrue(errores.containsKey("fecha"))
        assertTrue(errores.containsKey("motivo"))
    }

    @Test
    fun solicitudValidaAceptaFechaActual() {
        val errores = Validaciones.solicitud(SolicitudDatos(
            modalidad = "Alianza", motivo = "Colaboración de prueba de ejemplo",
            participantes = "5", fecha = "2026-10-22", contacto = true, acepta = true
        ), LocalDate.of(2026, 10, 21))
        assertTrue(errores.isEmpty())
    }

    @Test
    fun iniciarSesionVacioEsInvalido() {
        assertFalse(Validaciones.login("", "").isEmpty())
    }
}
