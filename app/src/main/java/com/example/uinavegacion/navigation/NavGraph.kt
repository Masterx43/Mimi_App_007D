package com.example.uinavegacion.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uinavegacion.ui.components.*
import com.example.uinavegacion.ui.screen.*
import com.example.uinavegacion.ui.theme.LilaPri
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val goHome: () -> Unit = { navController.navigate(Route.Home.path) }
    val goLogin: () -> Unit = { navController.navigate(Route.Login.path) }
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) }
    val goReserve: () -> Unit = { navController.navigate(Route.Booking.path) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = null,
                items = defaultDrawerItems(
                    onHome = { scope.launch { drawerState.close() }; goHome() },
                    onLogin = { scope.launch { drawerState.close() }; goLogin() },
                    onRegister = { scope.launch { drawerState.close() }; goRegister() },
                    onReserve = { scope.launch { drawerState.close() }; goReserve() }
                )
            )
        }
    ) {
        Scaffold(
            //  Fondo transparente (para que se vean las curvas)
            containerColor = Color.Transparent,
            // Elimina paddings automáticos del sistema
            contentWindowInsets = WindowInsets(0, 0, 0, 0),

            topBar = {
                AppTopBar(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onHome = goHome,
                    onLogin = goLogin,
                    onRegister = goRegister,
                    onReserve = goReserve
                )
            },
            bottomBar = {
                AppBottomBar(
                    onLogin = goLogin,
                    onRegister = goRegister,
                    onReserve = goReserve
                )
            }
        ) { innerPadding ->

            // 👇 El contenido principal NO debe pintar el fondo del BottomBar
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White) // fondo del contenido
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Route.Home.path,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Route.Home.path) {
                        HomeScreen(
                            onGoLogin = goLogin,
                            onGoRegister = goRegister,
                            onGoReserve = goReserve
                        )
                    }
                    composable(Route.Login.path) {
                        LoginScreenVm(
                            onLoginOkNavigateHome = goHome,
                            onGoRegister = goRegister
                        )
                    }
                    composable(Route.Register.path) {
                        RegisterScreenVm(
                            onRegisteredNavigateLogin = goLogin,
                            onGoLogin = goLogin
                        )
                    }
                    composable(Route.Booking.path) {
                        BookingScreen()
                    }
                }
            }
        }
    }
}
