package com.example.uinavegacion.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.theme.LilaPri

@Composable
fun AppBottomBar(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onReserve: () -> Unit,
    onUserInfo: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs =remember { UserPreferences(context) }
    val isLoggedIn by userPrefs.isLoogedIn.collectAsStateWithLifecycle(false)
    val roleId by userPrefs.userRoleId.collectAsStateWithLifecycle(null)

    Surface(
        tonalElevation = 8.dp,  // 👈 sombra suave para dar profundidad
        shadowElevation = 8.dp, // 👈 mejora el contraste con el fondo
        color = LilaPri,
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp
        ), // 🔥 bordes curvos superiores
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .navigationBarsPadding()
    ) {
        NavigationBar(containerColor = Color.Transparent,
                        tonalElevation = 0.dp) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isLoggedIn) {
                            if (roleId == 2L) Icons.Filled.AccountCircle // Admin
                            else Icons.Filled.Person // Cliente
                        } else Icons.Filled.PersonOff,
                        contentDescription = "Estado usuario",
                        tint = Color.White
                    )
                },
                label = {
                    Text(
                        if (isLoggedIn) {
                            if (roleId == 2L) "Admin" else "Cliente"
                        } else "Invitado",
                        color = Color.White
                    )
                },
                selected = false,
                onClick = {
                    if (isLoggedIn) {
                        onUserInfo()   //Si está logueado → ir a UserInfoScreen
                    } else {
                        onLogin()      // Si no está logueado → ir al Login
                    }
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Registro", tint = Color.White) },
                label = { Text("Registro", color = Color.White) },
                selected = false,
                onClick = onRegister
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.DateRange, contentDescription = "Agendar", tint = Color.White) },
                label = { Text("Agendar", color = Color.White) },
                selected = false,
                onClick = onReserve
            )
        }
    }
}
