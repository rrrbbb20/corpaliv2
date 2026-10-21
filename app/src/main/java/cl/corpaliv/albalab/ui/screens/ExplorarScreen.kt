package cl.corpaliv.albalab.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.Tarjeta
import cl.corpaliv.albalab.ui.components.Titulo

private val programas = listOf(
    "Taller sociolaboral" to "Formación de habilidades sociales, emocionales y laborales.",
    "Intermediación laboral" to "Acciones de preparación y acompañamiento para la inclusión laboral.",
    "Capacitaciones de inclusión" to "Formación para empresas y organizaciones en inclusión y discapacidad.",
    "Experiencias de inclusión" to "Actividades colaborativas para sensibilizar y vincular equipos.",
    "Accesibilidad e inclusión" to "Orientación sobre accesibilidad universal y puestos de trabajo.",
    "Productos con impacto social" to "Catálogo oficial de productos desarrollados en Alba Lab."
)

@Composable
fun ExplorarScreen(onColaborar: () -> Unit) {
    val context = LocalContext.current
    var busqueda by remember { mutableStateOf("") }
    val visibles = programas.filter { (titulo, detalle) ->
        titulo.contains(busqueda, ignoreCase = true) || detalle.contains(busqueda, ignoreCase = true)
    }
    CuerpoPantalla {
        Titulo("Programas y servicios", "Explora las formas en que Alba Lab impulsa la inclusión.")
        OutlinedTextField(value = busqueda, onValueChange = { busqueda = it },
            label = { Text("Buscar programa o servicio") },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        if (visibles.isEmpty()) Text("No hay resultados para esta búsqueda.")
        visibles.forEach { (titulo, detalle) -> Tarjeta(titulo, detalle) }
        OutlinedButton(onClick = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://albalab.corpaliv.cl/")))
        }, modifier = Modifier.fillMaxWidth()) { Text("Abrir catálogo oficial en navegador") }
        Button(onClick = onColaborar, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar interés en colaborar")
        }
    }
}
