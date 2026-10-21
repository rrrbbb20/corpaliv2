package cl.corpaliv.albalab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.TextoError
import cl.corpaliv.albalab.ui.components.Titulo
import cl.corpaliv.albalab.util.RegistroDatos

@Composable
fun RegistroScreen(errores: Map<String, String>, cargando: Boolean,
                   onRegistrar: (RegistroDatos) -> Unit, onEditar: () -> Unit) {
    var datos by remember { mutableStateOf(RegistroDatos()) }
    fun editar(nuevos: RegistroDatos) { datos = nuevos; onEditar() }
    CuerpoPantalla {
        Titulo("Crear cuenta", "Puedes registrarte como persona natural o empresa.")
        Text("Tipo de cuenta")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Persona", "Empresa").forEach { tipo ->
                FilterChip(selected = datos.tipo == tipo, onClick = { editar(datos.copy(tipo = tipo)) },
                    label = { Text(tipo) })
            }
        }
        TextoError(errores["tipo"])
        OutlinedTextField(datos.nombre, onValueChange = { editar(datos.copy(nombre = it)) },
            label = { Text(if (datos.tipo == "Empresa") "Nombre de la empresa" else "Nombre completo") },
            isError = errores.containsKey("nombre"), supportingText = { TextoError(errores["nombre"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(datos.correo, onValueChange = { editar(datos.copy(correo = it)) },
            label = { Text("Correo electrónico") },
            isError = errores.containsKey("correo"), supportingText = { TextoError(errores["correo"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(datos.clave, onValueChange = { editar(datos.copy(clave = it)) },
            label = { Text("Contraseña (8 caracteres y un número)") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errores.containsKey("clave"), supportingText = { TextoError(errores["clave"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(datos.confirmar, onValueChange = { editar(datos.copy(confirmar = it)) },
            label = { Text("Repetir contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errores.containsKey("confirmar"), supportingText = { TextoError(errores["confirmar"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = datos.acepta, onCheckedChange = { editar(datos.copy(acepta = it)) })
            Text("Acepto guardar mi cuenta de demostración localmente en este teléfono.")
        }
        TextoError(errores["acepta"])
        Button(onClick = { onRegistrar(datos) }, enabled = !cargando,
            modifier = Modifier.fillMaxWidth()) {
            if (cargando) CircularProgressIndicator() else Text("Crear mi cuenta")
        }

    }
}
