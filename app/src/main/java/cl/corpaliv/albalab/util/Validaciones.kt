package cl.corpaliv.albalab.util

import java.time.LocalDate

// La validación vive fuera de los Composables: es fácil probarla y modificarla.
data class RegistroDatos(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmar: String = "",
    val tipo: String = "Persona",
    val acepta: Boolean = false
)

data class SolicitudDatos(
    val modalidad: String = "Donación",
    val motivo: String = "",
    val participantes: String = "",
    val fecha: String = "",
    val contacto: Boolean = true,
    val acepta: Boolean = false
)

object Validaciones {
    fun registro(d: RegistroDatos): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        if (d.nombre.trim().length < 3) errores["nombre"] = "Ingresa al menos 3 caracteres."
        if (!Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(d.correo.trim()))
            errores["correo"] = "Ingresa un correo válido."
        if (d.clave.length < 8 || !d.clave.any { it.isDigit() })
            errores["clave"] = "Mínimo 8 caracteres y al menos un número."
        if (d.confirmar != d.clave) errores["confirmar"] = "Las contraseñas no coinciden."
        if (d.tipo !in listOf("Persona", "Empresa")) errores["tipo"] = "Selecciona un tipo de cuenta."
        if (!d.acepta) errores["acepta"] = "Debes aceptar el almacenamiento local."
        return errores
    }

    fun login(correo: String, clave: String): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        if (correo.isBlank() || !correo.contains('@')) errores["correo"] = "Ingresa tu correo."
        if (clave.isBlank()) errores["clave"] = "Ingresa tu contraseña."
        return errores
    }

    fun solicitud(d: SolicitudDatos, hoy: LocalDate = LocalDate.now()): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        if (d.modalidad !in listOf("Donación", "Alianza", "Voluntariado", "Productos", "Capacitación"))
            errores["modalidad"] = "Selecciona una modalidad."
        if (d.motivo.trim().length < 10) errores["motivo"] = "Describe tu interés en al menos 10 caracteres."
        val numero = d.participantes.toIntOrNull()
        if (numero == null || numero !in 1..10000)
            errores["participantes"] = "Indica un número entre 1 y 10000."
        val fecha = try { LocalDate.parse(d.fecha) } catch (_: Exception) { null }
        if (fecha == null || fecha.isBefore(hoy)) errores["fecha"] = "Elige hoy o una fecha futura."
        if (!d.acepta) errores["acepta"] = "Debes aceptar guardar la solicitud en este teléfono."
        return errores
    }
}
