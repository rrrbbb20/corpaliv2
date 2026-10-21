package cl.corpaliv.albalab.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.TextoError
import cl.corpaliv.albalab.ui.components.Titulo

@Composable
fun LoginScreen(errores: Map<String, String>, cargando: Boolean,
                onIngresar: (String, String) -> Unit, onRegistro: () -> Unit,
                onEditar: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    CuerpoPantalla {
        Titulo("Ingresar", "Usa el correo y la contraseña de tu cuenta local.")
        OutlinedTextField(correo, onValueChange = { correo = it; onEditar() },
            label = { Text("Correo electrónico") },
            isError = errores.containsKey("correo"), supportingText = { TextoError(errores["correo"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(clave, onValueChange = { clave = it; onEditar() },
            label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(),
            isError = errores.containsKey("clave"), supportingText = { TextoError(errores["clave"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        Button(onClick = { onIngresar(correo, clave) }, enabled = !cargando,
            modifier = Modifier.fillMaxWidth()) {
            if (cargando) CircularProgressIndicator() else Text("Ingresar")
        }
        OutlinedButton(onClick = onRegistro, modifier = Modifier.fillMaxWidth()) {
            Text("No tengo cuenta: registrarme")
        }
        Text("Si aún no has creado una cuenta en este dispositivo, regístrate primero.")
    }
}
