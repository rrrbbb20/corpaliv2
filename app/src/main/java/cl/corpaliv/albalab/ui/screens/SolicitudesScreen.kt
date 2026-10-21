package cl.corpaliv.albalab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.corpaliv.albalab.data.model.Solicitud

@Composable
fun SolicitudesScreen(solicitudes: List<Solicitud>, onNueva: () -> Unit,
                     onRevisar: (Solicitud) -> Unit, onBorrar: (Solicitud) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)) {
        item {
            Text("Historial local (${solicitudes.size})", style = MaterialTheme.typography.headlineSmall)
            Text("Puedes consultar, actualizar el estado o borrar tus solicitudes.")
            Button(onClick = onNueva, modifier = Modifier.fillMaxWidth()) { Text("Nueva solicitud") }
        }
        if (solicitudes.isEmpty()) {
            item { Text("Aún no tienes solicitudes guardadas.") }
        }
        items(solicitudes, key = { it.id }) { s ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(s.modalidad, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)
                    Text(s.motivo)
                    Text("Fecha: ${s.fecha} · Participantes: ${s.participantes}")
                    Text(if (s.revisada) "Estado local: Revisada" else "Estado local: Pendiente")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { onRevisar(s) }) {
                            Text(if (s.revisada) "Marcar pendiente" else "Marcar revisada")
                        }
                        TextButton(onClick = { onBorrar(s) }) { Text("Eliminar") }
                    }
                }
            }
        }
    }
}
