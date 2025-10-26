package com.example.uinavegacion.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.components.*
import com.example.uinavegacion.ui.screen.*
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.AdminViewModel

import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.BookingViewModel
import com.example.uinavegacion.viewmodel.UserInfoViewModel
import com.example.uinavegacion.viewmodel.WorkerViewModel
import kotlinx.coroutines.launch

@Composable // Gráfico de navegación + Drawer + Scaffold
fun AppNavGraph(navController: NavHostController,
                authViewModel: AuthViewModel,
                bookingViewModel: BookingViewModel,
                adminViewModel: AdminViewModel,
                workerViewModel: WorkerViewModel,
                userInfoViewModel: UserInfoViewModel,
                userPreferences: UserPreferences
) { // Recibe el controlador


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // Helpers de navegación (reutilizamos en topbar/drawer/botones)
    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) {launchSingleTop = true} }    // Ir a Home
    val goLogin: () -> Unit   = { navController.navigate(Route.Login.path) {launchSingleTop = true} }   // Ir a Login
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) {launchSingleTop = true} } // Ir a Registro
    val goReserve: () -> Unit= {navController.navigate(Route.Booking.path) {launchSingleTop = true} }
    val goUserInfo:() -> Unit = {navController.navigate(Route.UserInfo.path) {launchSingleTop = true} }
    val goAdmin: () -> Unit = {navController.navigate(Route.AdminInfo.path) {launchSingleTop = true} }
    val goWorker: () -> Unit = {navController.navigate(Route.WorkerInfo.path) {launchSingleTop = true} }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = null,
                items = defaultDrawerItems(
                    onHome = { scope.launch { drawerState.close() }; goHome() },
                    onLogin = { scope.launch { drawerState.close() }; goLogin() },
                    onRegister = { scope.launch { drawerState.close() }; goRegister() },
                    onReserve = { scope.launch { drawerState.close() }; goReserve() },
                    onUserInfo = {scope.launch { drawerState.close(); goUserInfo() }}
                )
            )
        }
    ) {
        Scaffold(
            //  Fondo transparente (para que se vean las curvas)
            containerColor = Color.Transparent,
            // Elimina paddings automáticos del sistema
            contentWindowInsets = WindowInsets.safeDrawing,

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
                    onReserve = goReserve,
                    onUserInfo = goUserInfo,
                    onAdmin = goAdmin,
                    onWorker = goWorker
                )
            },
            modifier = Modifier
                .fillMaxSize()

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
                            vm = authViewModel,            // <-- NUEVO: pasamos VM inyectado
                            onLoginOkNavigateHome = goHome,            // Si el VM marca success=true, navegamos a Home
                            onGoRegister = goRegister                  // Enlace para ir a la pantalla de Registro

                        )
                    }
                    composable(Route.Register.path) {
                        RegisterScreenVm(
                            vm = authViewModel,
                            onRegisteredNavigateLogin = goLogin, // Botón para ir a Login
                            onGoLogin = goLogin     // Botón alternativo a Login

                        )
                    }
                    composable(Route.Booking.path) {
                        BookingScreen(
                            vm = bookingViewModel,
                            authVm = authViewModel
                        )
                    }

                    composable(Route.UserInfo.path){
                        UserInfoScreen(
                            vm = authViewModel,
                            onLogout = goLogin,
                            userInfoVm = userInfoViewModel,
                            userPrefs = userPreferences
                        )
                    }

                    composable(Route.AdminInfo.path) {
                        AdminScreen(
                            vm = authViewModel,
                            onLogout = goLogin,
                            adminVm = adminViewModel
                        )
                    }

                    composable(Route.WorkerInfo.path) {
                        WorkerScreen(
                            vm = authViewModel,
                            onLogout = goLogin,
                            workerVm = workerViewModel
                        )
                    }
                }
            }
        }
    }
}
