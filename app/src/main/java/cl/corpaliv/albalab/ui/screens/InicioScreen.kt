package cl.corpaliv.albalab.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.Tarjeta
import cl.corpaliv.albalab.ui.components.Titulo

@Composable
fun InicioScreen(nombre: String?, onExplorar: () -> Unit,
                 onColaborar: () -> Unit, onRegistro: () -> Unit) {
    CuerpoPantalla {
        Titulo("Inclusión que conecta", if (nombre == null)
            "Conoce cómo colaborar con CORPALIV y la red Alba Lab."
        else "Hola, $nombre. Gracias por interesarte en la inclusión.")
        Tarjeta("¿Quiénes somos?",
            "CORPALIV trabaja por la autonomía, la participación y la inclusión de personas con discapacidad múltiple.")
        Tarjeta("Alba Lab",
            "Red sociolaboral que impulsa la formación y las oportunidades de inclusión laboral.")
        Button(onClick = onExplorar, modifier = Modifier.fillMaxWidth()) { Text("Conocer programas y servicios") }
        Button(onClick = onColaborar, modifier = Modifier.fillMaxWidth()) { Text("Quiero colaborar") }
        if (nombre == null) {
            OutlinedButton(onClick = onRegistro, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta de persona o empresa")
            }
        }

    }
}
