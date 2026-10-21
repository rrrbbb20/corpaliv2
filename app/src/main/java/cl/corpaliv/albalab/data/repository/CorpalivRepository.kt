package cl.corpaliv.albalab.data.repository

import cl.corpaliv.albalab.data.local.CorpalivDao
import cl.corpaliv.albalab.data.model.Solicitud
import cl.corpaliv.albalab.data.model.Usuario
import kotlinx.coroutines.flow.Flow

// Repository: única entrada del ViewModel a Room.
class CorpalivRepository(private val dao: CorpalivDao) {
    suspend fun buscarUsuario(correo: String) = dao.buscarUsuario(correo)
    suspend fun registrar(usuario: Usuario) = dao.registrarUsuario(usuario)
    suspend fun cambiarFoto(id: Long, ruta: String) = dao.cambiarFoto(id, ruta)
    fun solicitudes(usuarioId: Long): Flow<List<Solicitud>> = dao.observarSolicitudes(usuarioId)
    suspend fun guardarSolicitud(solicitud: Solicitud) = dao.crearSolicitud(solicitud)
    suspend fun cambiarRevision(id: Long, usuarioId: Long, revisada: Boolean) =
        dao.cambiarRevision(id, usuarioId, revisada)
    suspend fun eliminar(id: Long, usuarioId: Long) = dao.eliminarSolicitud(id, usuarioId)
}
