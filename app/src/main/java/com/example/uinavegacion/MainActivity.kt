package com.example.uinavegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uinavegacion.data.local.database.AppDatabase
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.data.repository.CategoriaRepository
import com.example.uinavegacion.data.repository.EstadoRepository
import com.example.uinavegacion.data.repository.ReservaRepository
import com.example.uinavegacion.data.repository.RolRepository
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.navigation.AppNavGraph
import com.example.uinavegacion.ui.theme.UINavegacionTheme
import com.example.uinavegacion.viewmodel.AdminViewModel
import com.example.uinavegacion.viewmodel.AdminViewModelFactory
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.AuthViewModelFactory
import com.example.uinavegacion.viewmodel.BookingViewModel
import com.example.uinavegacion.viewmodel.BookingViewModelFactory
import com.example.uinavegacion.viewmodel.WorkerViewModel
import com.example.uinavegacion.viewmodel.WorkerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}


/*
* En Compose, Surface es un contenedor visual que viene de Material 3.Crea un bloque
*  que puedes personalizar con color, forma, sombra (elevación).
Sirve para aplicar un fondo (color, borde, elevación, forma) siguiendo las guías de diseño
* de Material.
Piensa en él como una “lona base” sobre la cual vas a pintar tu UI.
* Si cambias el tema a dark mode, colorScheme.background
* cambia automáticamente y el Surface pinta la pantalla con el nuevo color.
* */
@Composable // Indica que esta función dibuja UI
fun AppRoot() { // Raíz de la app para separar responsabilidades
    // ====== NUEVO: construcción de dependencias (Composition Root) ======
    val context = LocalContext.current.applicationContext
    // ^ Obtenemos el applicationContext para construir la base de datos de Room.

    val db = AppDatabase.getInstance(context)
    // ^ Singleton de Room. No crea múltiples instancias.

    val userDao = db.userDao()
    val reservaDao = db.reservaDao()
    val servicioDao = db.servicioDao()
    val rolDao = db.rolDao()
    val categoriaDao = db.categoriaDao()
    // ^ Daos para viewModel.

    val userRepository = UserRepository(userDao)
    val reservaRepository = ReservaRepository(reservaDao)
    val servicioRepository = ServicioRepository(servicioDao)
    val rolRepository = RolRepository(rolDao)
    val categoriaRepository = CategoriaRepository(categoriaDao)
    // ^ Repositorios.

    val userPrefs = UserPreferences(context)

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository, userPrefs)
    )
    val adminVm: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(
            servicioRepository,categoriaRepository,rolRepository,userRepository)
    )

    val workervm: WorkerViewModel = viewModel(
        factory = WorkerViewModelFactory(reservaRepository)
    )

    val authState by authViewModel.session.collectAsState()
    val userId = authState.userId ?: 1L

    val bookingViewModel : BookingViewModel = viewModel (
        factory = BookingViewModelFactory(reservaRepository, servicioRepository, userId)
    )
    // ^ Creamos el ViewModel con factory para inyectar el repositorio.

    // ====== TU NAVEGACIÓN ORIGINAL ======
    val navController = rememberNavController() // Controlador de navegación (igual que antes)
    MaterialTheme { // Provee colores/tipografías Material 3 (igual que antes)
        Surface(color = MaterialTheme.colorScheme.background) { // Fondo general (igual que antes)

            // ====== MOD: pasamos el AuthViewModel a tu NavGraph ======
            // Si tu AppNavGraph ya recibía el VM o lo creaba adentro, lo mejor ahora es PASARLO
            // para que toda la app use la MISMA instancia que acabamos de inyectar.
            AppNavGraph(
                navController = navController,
                authViewModel=  authViewModel,
                bookingViewModel = bookingViewModel,// <-- NUEVO parámetro
                adminViewModel = adminVm,
                workerViewModel = workervm

            )
            // NOTA: Si tu AppNavGraph no tiene este parámetro aún, basta con agregarlo:
            // fun AppNavGraph(navController: NavHostController, authViewModel: AuthViewModel) { ... }
            // y luego pasar ese authViewModel a las pantallas Login/Register donde se use.
        }
    }

}