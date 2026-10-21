package cl.corpaliv.albalab.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "solicitudes",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["usuarioId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["usuarioId"])]
)
data class Solicitud(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val modalidad: String,
    val motivo: String,
    val participantes: Int,
    val fecha: String, // AAAA-MM-DD
    val recibirContacto: Boolean,
    val revisada: Boolean = false
)
