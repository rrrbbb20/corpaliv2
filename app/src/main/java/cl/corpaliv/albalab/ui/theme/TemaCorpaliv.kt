package cl.corpaliv.albalab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val paleta = lightColorScheme(
    primary = Color(0xFF174D69),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7EFF5),
    onPrimaryContainer = Color(0xFF123544),
    secondary = Color(0xFF697637),
    background = Color(0xFFF7F9FA),
    surface = Color.White,
    surfaceVariant = Color(0xFFE9EFF0)
)

@Composable
fun TemaCorpaliv(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = paleta, content = contenido)
}
