package cl.corpaliv.albalab.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.corpaliv.albalab.data.model.Usuario
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.Tarjeta
import cl.corpaliv.albalab.ui.components.Titulo
import cl.corpaliv.albalab.util.Imagenes
import java.io.File

@Composable
fun PerfilScreen(usuario: Usuario?, onCambiarFoto: (String) -> Unit) {
    if (usuario == null) return
    val context = LocalContext.current
    var fotoPendiente by remember { mutableStateOf<File?>(null) }
    val imagen = remember(usuario.fotoRuta) { Imagenes.cargarMiniatura(usuario.fotoRuta) }

    // Recurso nativo 1: abre la app de cámara mediante el contrato TakePicture.
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { bien ->
        val foto = fotoPendiente
        if (bien && foto != null) onCambiarFoto(foto.absolutePath)
        else foto?.delete()
        fotoPendiente = null
    }
    // Recurso nativo 2: selector de imágenes gestionado por Android.
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try { onCambiarFoto(Imagenes.copiarGaleria(context, uri)) }
            catch (_: Exception) {
                Toast.makeText(context, "No se pudo cargar la imagen.", Toast.LENGTH_LONG).show()
            }
        }
    }

    CuerpoPantalla {
        Titulo("Mi perfil", "Cuenta ${usuario.tipoCuenta.lowercase()} · guardada en Room")
        if (imagen != null) {
            Image(bitmap = imagen.asImageBitmap(), contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(160.dp).clip(CircleShape))
        } else Text("Sin fotografía de perfil")
        Tarjeta("Nombre", usuario.nombre)
        Tarjeta("Correo", usuario.correo)
        Button(onClick = {
            try {
                val archivo = Imagenes.archivoCamara(context)
                fotoPendiente = archivo
                camara.launch(Imagenes.uriCamara(context, archivo))
            } catch (_: Exception) {
                fotoPendiente = null
                Toast.makeText(context, "No se pudo abrir la cámara.", Toast.LENGTH_LONG).show()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Tomar foto con cámara") }
        OutlinedButton(onClick = { galeria.launch("image/*") },
            modifier = Modifier.fillMaxWidth()) { Text("Elegir foto de galería") }

    }
}
