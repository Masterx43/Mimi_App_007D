package com.example.uinavegacion.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.ui.theme.LilaPri

@Composable
fun AppBottomBar(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onReserve: () -> Unit
) {
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
                icon = { Icon(Icons.Filled.AccountCircle,
                    contentDescription = "Cuenta",
                    tint = Color.White) },
                label = { Text("Cuenta",
                    color = Color.White) },
                selected = false,
                onClick = onLogin
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
