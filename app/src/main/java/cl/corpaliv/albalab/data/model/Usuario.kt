package cl.corpaliv.albalab.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["correo"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val tipoCuenta: String, // "Persona" o "Empresa"
    val claveHash: String,
    val claveSal: String,
    val fotoRuta: String = ""
)
