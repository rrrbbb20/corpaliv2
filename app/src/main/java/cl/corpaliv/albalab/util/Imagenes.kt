package cl.corpaliv.albalab.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

// FileProvider entrega a la app de cámara una URI temporal, sin acceso a otras carpetas.
object Imagenes {
    fun archivoCamara(context: Context): File {
        val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
        return File.createTempFile("camara_", ".jpg", carpeta)
    }

    fun uriCamara(context: Context, archivo: File): Uri = FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", archivo
    )

    // Copiamos la foto seleccionada al espacio privado para que siga disponible al reiniciar.
    fun copiarGaleria(context: Context, uri: Uri): String {
        val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
        val salida = File.createTempFile("galeria_", ".jpg", carpeta)
        try {
            context.contentResolver.openInputStream(uri).use { entrada ->
                requireNotNull(entrada) { "Imagen no disponible" }
                val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
                val bitmap = BitmapFactory.decodeStream(entrada, null, opciones)
                    ?: error("El archivo elegido no es una imagen compatible")
                FileOutputStream(salida).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }
                bitmap.recycle()
            }
            return salida.absolutePath
        } catch (error: Exception) {
            salida.delete()
            throw error
        }
    }

    fun cargarMiniatura(ruta: String): Bitmap? {
        if (ruta.isBlank()) return null
        val opciones = BitmapFactory.Options().apply { inSampleSize = 2 }
        return BitmapFactory.decodeFile(ruta, opciones)
    }
}
