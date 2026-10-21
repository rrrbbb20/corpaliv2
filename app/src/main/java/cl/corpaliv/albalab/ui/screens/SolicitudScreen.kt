package cl.corpaliv.albalab.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import cl.corpaliv.albalab.ui.components.CuerpoPantalla
import cl.corpaliv.albalab.ui.components.TextoError
import cl.corpaliv.albalab.ui.components.Titulo
import cl.corpaliv.albalab.util.SolicitudDatos
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@Composable
fun SolicitudScreen(errores: Map<String, String>, cargando: Boolean,
                    onEditar: () -> Unit, onGuardar: (SolicitudDatos) -> Unit) {
    val context = LocalContext.current
    var datos by remember { mutableStateOf(SolicitudDatos()) }
    fun editar(nuevos: SolicitudDatos) { datos = nuevos; onEditar() }
    val hoy = LocalDate.now()
    CuerpoPantalla {
        Titulo("Solicitud de colaboración", "Cuéntanos cómo te gustaría colaborar. Se guardará localmente.")
        Text("Modalidad de colaboración")
        listOf("Donación", "Alianza", "Voluntariado", "Productos", "Capacitación").forEach { opcion ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = datos.modalidad == opcion,
                    onClick = { editar(datos.copy(modalidad = opcion)) })
                Text(opcion)
            }
        }
        TextoError(errores["modalidad"])
        OutlinedTextField(datos.motivo, onValueChange = { editar(datos.copy(motivo = it)) },
            label = { Text("Motivo de colaboración") }, minLines = 2, maxLines = 4,
            isError = errores.containsKey("motivo"),
            supportingText = { TextoError(errores["motivo"]) },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(datos.participantes, onValueChange = { texto ->
            if (texto.all { it.isDigit() }) editar(datos.copy(participantes = texto))
        }, label = { Text("Participantes estimados (número)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errores.containsKey("participantes"),
            supportingText = { TextoError(errores["participantes"]) },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        TextButton(onClick = {
            val dia = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            DatePickerDialog(context, { _, anio, mes, diaDelMes ->
                editar(datos.copy(fecha = String.format(Locale.US,
                    "%04d-%02d-%02d", anio, mes + 1, diaDelMes)))
            }, hoy.year, hoy.monthValue - 1, hoy.dayOfMonth).apply {
                datePicker.minDate = dia.timeInMillis
                show()
            }
        }) { Text(if (datos.fecha.isEmpty()) "Elegir fecha tentativa" else "Fecha: ${datos.fecha}") }
        TextoError(errores["fecha"])
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Me interesa recibir contacto")
            Switch(checked = datos.contacto, onCheckedChange = { editar(datos.copy(contacto = it)) })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = datos.acepta, onCheckedChange = { editar(datos.copy(acepta = it)) })
            Text("Acepto terminos y condiciones.")
        }
        TextoError(errores["acepta"])
        Button(onClick = { onGuardar(datos) }, enabled = !cargando, modifier = Modifier.fillMaxWidth()) {
            if (cargando) CircularProgressIndicator() else Text("Guardar solicitud")
        }

    }
}
