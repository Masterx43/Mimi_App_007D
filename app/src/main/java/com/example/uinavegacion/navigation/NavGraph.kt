package com.example.uinavegacion.navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // Para aplicar innerPadding
import androidx.compose.material3.Scaffold // Estructura base con slots
import androidx.compose.runtime.Composable // Marcador composable
import androidx.compose.ui.Modifier // Modificador
import androidx.navigation.NavHostController // Controlador de navegación
import androidx.navigation.compose.NavHost // Contenedor de destinos
import androidx.navigation.compose.composable // Declarar cada destino
import kotlinx.coroutines.launch // Para abrir/cerrar drawer con corrutinas

import androidx.compose.material3.ModalNavigationDrawer // Drawer lateral modal
import androidx.compose.material3.rememberDrawerState // Estado del drawer
import androidx.compose.material3.DrawerValue // Valores (Opened/Closed)
import androidx.compose.runtime.rememberCoroutineScope // Alcance de corrutina
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


import com.example.uinavegacion.ui.components.AppTopBar // Barra superior
import com.example.uinavegacion.ui.components.AppDrawer // Drawer composable
import com.example.uinavegacion.ui.components.defaultDrawerItems // Ítems por defecto
import com.example.uinavegacion.ui.screen.BookingScreen
import com.example.uinavegacion.ui.screen.HomeScreen // Pantalla Home
import com.example.uinavegacion.ui.screen.LoginScreenVm // Pantalla Login
import com.example.uinavegacion.ui.screen.RegisterScreenVm // Pantalla Registro
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.BookingViewModel

@Composable // Gráfico de navegación + Drawer + Scaffold
fun AppNavGraph(navController: NavHostController,
                authViewModel: AuthViewModel,
                bookingViewModel: BookingViewModel
) { // Recibe el controlador

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Estado del drawer
    val scope = rememberCoroutineScope() // Necesario para abrir/cerrar drawer

    // Helpers de navegación (reutilizamos en topbar/drawer/botones)
    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) }    // Ir a Home
    val goLogin: () -> Unit   = { navController.navigate(Route.Login.path) }   // Ir a Login
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) } // Ir a Registro
    val goReserve: () -> Unit= {navController.navigate(Route.Booking.path)}

    ModalNavigationDrawer( // Capa superior con drawer lateral
        drawerState = drawerState, // Estado del drawer
        drawerContent = { // Contenido del drawer (menú)
            AppDrawer( // Nuestro componente Drawer
                currentRoute = null, // Puedes pasar navController.currentBackStackEntry?.destination?.route
                items = defaultDrawerItems( // Lista estándar
                    onHome = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goHome() // Navega a Home
                    },
                    onLogin = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goLogin() // Navega a Login
                    },
                    onRegister = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goRegister() // Navega a Registro
                    },
                    onReserve = {
                        scope.launch { drawerState.close() }
                        goReserve()
                    }
                )
            )
        }
    ) {
        Scaffold( // Estructura base de pantalla
            containerColor = Color.Transparent,
            topBar = { // Barra superior con íconos/menú
                AppTopBar(
                    onOpenDrawer = { scope.launch { drawerState.open() } }, // Abre drawer
                    onHome = goHome,     // Botón Home
                    onLogin = goLogin,   // Botón Login
                    onRegister = goRegister, // Botón Registro
                    onReserve = goReserve
                )
            }
        ) { innerPadding ->
                NavHost( // Contenedor de destinos navegables
                    navController = navController, // Controlador
                    startDestination = Route.Home.path, // Inicio: Home
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(LilaPri)// Respeta topBar
                ) {
                    composable(Route.Home.path) { // Destino Home
                        HomeScreen(
                            onGoLogin = goLogin,     // Botón para ir a Login
                            onGoRegister = goRegister, // Botón para ir a Registro
                            onGoReserve = goReserve
                        )
                    }
                    composable(Route.Login.path) { // Destino Login
                        LoginScreenVm(
                            vm = authViewModel,            // <-- NUEVO: pasamos VM inyectado
                            onLoginOkNavigateHome = goHome,            // Si el VM marca success=true, navegamos a Home
                            onGoRegister = goRegister                  // Enlace para ir a la pantalla de Registro
                        )
                    }
                    composable(Route.Register.path) { // Destino Registro
                        RegisterScreenVm(
                            vm = authViewModel,
                            onRegisteredNavigateLogin = goLogin, // Botón para ir a Login
                            onGoLogin = goLogin     // Botón alternativo a Login
                        )
                    }
                    composable(Route.Booking.path) {
                        BookingScreen(
                            vm = bookingViewModel
                        )
                    }

                }
        }
    }
}