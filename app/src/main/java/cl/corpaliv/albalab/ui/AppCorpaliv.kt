package cl.corpaliv.albalab.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.corpaliv.albalab.ui.screens.ExplorarScreen
import cl.corpaliv.albalab.ui.screens.InicioScreen
import cl.corpaliv.albalab.ui.screens.LoginScreen
import cl.corpaliv.albalab.ui.screens.PerfilScreen
import cl.corpaliv.albalab.ui.screens.RegistroScreen
import cl.corpaliv.albalab.ui.screens.SolicitudScreen
import cl.corpaliv.albalab.ui.screens.SolicitudesScreen
import cl.corpaliv.albalab.viewmodel.AppViewModel

// Navegación manual y pequeña: fácil de defender; los datos siguen en el ViewModel.
enum class Pantalla(val titulo: String) {
    INICIO("Inicio"), EXPLORAR("Programas"), MIS_SOLICITUDES("Mis solicitudes"),
    PERFIL("Mi perfil"), REGISTRO("Crear cuenta"), ACCESO("Ingresar"), COLABORAR("Colaborar")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCorpaliv(vm: AppViewModel = viewModel()) {
    val estado by vm.estado.collectAsState()
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.INICIO) }
    val snackbar = remember { SnackbarHostState() }

    fun navegar(destino: Pantalla) {
        vm.limpiarMensajes()
        pantalla = if (destino in listOf(Pantalla.PERFIL, Pantalla.MIS_SOLICITUDES, Pantalla.COLABORAR)
            && estado.usuario == null) Pantalla.ACCESO else destino
    }

    LaunchedEffect(estado.usuario?.id) {
        if (estado.usuario != null && pantalla in listOf(Pantalla.REGISTRO, Pantalla.ACCESO)) {
            pantalla = Pantalla.INICIO
        }
    }
    LaunchedEffect(estado.mensaje) {
        if (estado.mensaje.isNotBlank()) {
            snackbar.showSnackbar(estado.mensaje)
            vm.limpiarMensajes()
        }
    }
    BackHandler(enabled = pantalla != Pantalla.INICIO) {
        navegar(Pantalla.INICIO)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (pantalla == Pantalla.INICIO) "CORPALIV · Alba Lab" else pantalla.titulo) },
                navigationIcon = {
                    if (pantalla in listOf(Pantalla.REGISTRO, Pantalla.ACCESO, Pantalla.COLABORAR)) {
                        TextButton(onClick = { navegar(Pantalla.INICIO) }) { Text("←") }
                    }
                },
                actions = {
                    if (estado.usuario == null) {
                        if (pantalla != Pantalla.ACCESO && pantalla != Pantalla.REGISTRO) {
                            TextButton(onClick = { navegar(Pantalla.ACCESO) }) { Text("Ingresar") }
                        }
                    } else {
                        TextButton(onClick = { vm.salir(); pantalla = Pantalla.INICIO }) { Text("Salir") }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (pantalla in listOf(Pantalla.INICIO, Pantalla.EXPLORAR,
                    Pantalla.MIS_SOLICITUDES, Pantalla.PERFIL)) {
                NavigationBar {
                    listOf(
                        Triple(Pantalla.INICIO, "⌂", "Inicio"),
                        Triple(Pantalla.EXPLORAR, "◎", "Explorar"),
                        Triple(Pantalla.MIS_SOLICITUDES, "▤", "Solicitudes"),
                        Triple(Pantalla.PERFIL, "◉", "Perfil")
                    ).forEach { (destino, icono, nombre) ->
                        NavigationBarItem(
                            selected = pantalla == destino,
                            onClick = { navegar(destino) },
                            icon = { Text(icono) },
                            label = { Text(nombre) }
                        )
                    }
                }
            }
        }
    ) { margen ->
        Box(Modifier.fillMaxSize().padding(margen)) {
            Crossfade(targetState = pantalla, label = "cambio de pantalla") { actual ->
                when (actual) {
                    Pantalla.INICIO -> InicioScreen(
                        nombre = estado.usuario?.nombre,
                        onExplorar = { navegar(Pantalla.EXPLORAR) },
                        onColaborar = { navegar(Pantalla.COLABORAR) },
                        onRegistro = { navegar(Pantalla.REGISTRO) }
                    )
                    Pantalla.EXPLORAR -> ExplorarScreen(
                        onColaborar = { navegar(Pantalla.COLABORAR) }
                    )
                    Pantalla.ACCESO -> LoginScreen(
                        errores = estado.errores, cargando = estado.cargando,
                        onIngresar = vm::ingresar,
                        onRegistro = { navegar(Pantalla.REGISTRO) },
                        onEditar = vm::limpiarMensajes
                    )
                    Pantalla.REGISTRO -> RegistroScreen(
                        errores = estado.errores, cargando = estado.cargando,
                        onRegistrar = vm::registrar,
                        onEditar = vm::limpiarMensajes
                    )
                    Pantalla.COLABORAR -> SolicitudScreen(
                        errores = estado.errores, cargando = estado.cargando,
                        onEditar = vm::limpiarMensajes,
                        onGuardar = { datos -> vm.guardarSolicitud(datos) { pantalla = Pantalla.MIS_SOLICITUDES } }
                    )
                    Pantalla.MIS_SOLICITUDES -> SolicitudesScreen(
                        solicitudes = estado.solicitudes,
                        onNueva = { navegar(Pantalla.COLABORAR) },
                        onRevisar = vm::marcarRevisada,
                        onBorrar = vm::borrar
                    )
                    Pantalla.PERFIL -> PerfilScreen(
                        usuario = estado.usuario,
                        onCambiarFoto = vm::cambiarFoto
                    )
                }
            }
        }
    }
}
