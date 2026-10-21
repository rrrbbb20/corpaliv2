package cl.corpaliv.albalab.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.corpaliv.albalab.data.model.Solicitud
import cl.corpaliv.albalab.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface CorpalivDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarUsuario(correo: String): Usuario?

    @Insert
    suspend fun registrarUsuario(usuario: Usuario): Long

    @Query("UPDATE usuarios SET fotoRuta = :ruta WHERE id = :id")
    suspend fun cambiarFoto(id: Long, ruta: String)

    @Insert
    suspend fun crearSolicitud(solicitud: Solicitud)

    @Query("SELECT * FROM solicitudes WHERE usuarioId = :usuarioId ORDER BY id DESC")
    fun observarSolicitudes(usuarioId: Long): Flow<List<Solicitud>>

    @Query("UPDATE solicitudes SET revisada = :revisada WHERE id = :id AND usuarioId = :usuarioId")
    suspend fun cambiarRevision(id: Long, usuarioId: Long, revisada: Boolean)

    @Query("DELETE FROM solicitudes WHERE id = :id AND usuarioId = :usuarioId")
    suspend fun eliminarSolicitud(id: Long, usuarioId: Long)
}
