package cl.corpaliv.albalab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.corpaliv.albalab.data.local.CorpalivDatabase
import cl.corpaliv.albalab.data.model.Solicitud
import cl.corpaliv.albalab.data.model.Usuario
import cl.corpaliv.albalab.data.repository.CorpalivRepository
import cl.corpaliv.albalab.util.RegistroDatos
import cl.corpaliv.albalab.util.Seguridad
import cl.corpaliv.albalab.util.SolicitudDatos
import cl.corpaliv.albalab.util.Validaciones
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// Estado compartido: la UI lo observa y Compose se actualiza automáticamente.
data class EstadoApp(
    val usuario: Usuario? = null,
    val solicitudes: List<Solicitud> = emptyList(),
    val cargando: Boolean = false,
    val mensaje: String = "",
    val errores: Map<String, String> = emptyMap()
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CorpalivRepository(CorpalivDatabase.get(application).dao())
    private val _estado = MutableStateFlow(EstadoApp())
    val estado = _estado.asStateFlow()
    private var observarJob: Job? = null

    fun limpiarMensajes() { _estado.value = _estado.value.copy(mensaje = "", errores = emptyMap()) }
    private fun errorCampo(errores: Map<String, String>) {
        _estado.value = _estado.value.copy(errores = errores, mensaje = "Revisa los campos indicados.")
    }
    private fun errorGeneral() {
        _estado.value = _estado.value.copy(cargando = false, mensaje = "No fue posible guardar los datos. Intenta nuevamente.")
    }

    fun registrar(datos: RegistroDatos) {
        val errores = Validaciones.registro(datos)
        if (errores.isNotEmpty()) { errorCampo(errores); return }
        if (_estado.value.cargando) return
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, mensaje = "", errores = emptyMap())
            try {
                val correo = datos.correo.trim().lowercase()
                if (repository.buscarUsuario(correo) != null) {
                    _estado.value = _estado.value.copy(cargando = false, errores = mapOf("correo" to "Este correo ya está registrado."))
                    return@launch
                }
                val sal = Seguridad.generarSal()
                val hash = Seguridad.hash(datos.clave, sal)
                val nuevo = Usuario(nombre = datos.nombre.trim(), correo = correo,
                    tipoCuenta = datos.tipo, claveHash = hash, claveSal = sal)
                val id = repository.registrar(nuevo)
                iniciarSesion(nuevo.copy(id = id))
            } catch (_: Exception) { errorGeneral() }
        }
    }

    fun ingresar(correo: String, clave: String) {
        val errores = Validaciones.login(correo, clave)
        if (errores.isNotEmpty()) { errorCampo(errores); return }
        if (_estado.value.cargando) return
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, mensaje = "", errores = emptyMap())
            try {
                val usuario = repository.buscarUsuario(correo.trim().lowercase())
                if (usuario != null && Seguridad.verificar(clave, usuario.claveSal, usuario.claveHash)) {
                    iniciarSesion(usuario)
                } else {
                    _estado.value = _estado.value.copy(cargando = false, mensaje = "Correo o contraseña incorrectos.")
                }
            } catch (_: Exception) { errorGeneral() }
        }
    }

    private fun iniciarSesion(usuario: Usuario) {
        observarJob?.cancel()
        _estado.value = _estado.value.copy(usuario = usuario, solicitudes = emptyList(),
            cargando = false, mensaje = "Bienvenido/a, ${usuario.nombre}", errores = emptyMap())
        observarJob = viewModelScope.launch {
            repository.solicitudes(usuario.id).collect { lista ->
                _estado.value = _estado.value.copy(solicitudes = lista)
            }
        }
    }

    fun salir() {
        observarJob?.cancel()
        _estado.value = EstadoApp()
    }

    fun guardarSolicitud(datos: SolicitudDatos, alGuardar: () -> Unit) {
        val errores = Validaciones.solicitud(datos)
        if (errores.isNotEmpty()) { errorCampo(errores); return }
        val usuario = _estado.value.usuario ?: return
        if (_estado.value.cargando) return
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, errores = emptyMap(), mensaje = "")
            try {
                repository.guardarSolicitud(Solicitud(usuarioId = usuario.id,
                    modalidad = datos.modalidad, motivo = datos.motivo.trim(),
                    participantes = datos.participantes.toInt(), fecha = datos.fecha,
                    recibirContacto = datos.contacto))
                _estado.value = _estado.value.copy(cargando = false, mensaje = "Solicitud guardada solo en este dispositivo.")
                alGuardar()
            } catch (_: Exception) { errorGeneral() }
        }
    }

    fun marcarRevisada(solicitud: Solicitud) {
        val usuario = _estado.value.usuario ?: return
        viewModelScope.launch {
            try {
                repository.cambiarRevision(solicitud.id, usuario.id, !solicitud.revisada)
            } catch (_: Exception) { errorGeneral() }
        }
    }

    fun borrar(solicitud: Solicitud) {
        val usuario = _estado.value.usuario ?: return
        viewModelScope.launch {
            try { repository.eliminar(solicitud.id, usuario.id) }
            catch (_: Exception) { errorGeneral() }
        }
    }

    fun cambiarFoto(ruta: String) {
        val usuario = _estado.value.usuario ?: return
        viewModelScope.launch {
            try {
                repository.cambiarFoto(usuario.id, ruta)
                _estado.value = _estado.value.copy(usuario = usuario.copy(fotoRuta = ruta),
                    mensaje = "Foto de perfil guardada en este dispositivo.")
            } catch (_: Exception) { errorGeneral() }
        }
    }
}
