package cl.corpaliv.albalab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.corpaliv.albalab.ui.AppCorpaliv
import cl.corpaliv.albalab.ui.theme.TemaCorpaliv

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TemaCorpaliv { AppCorpaliv() } }
    }
}
