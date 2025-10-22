package com.example.uinavegacion.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.theme.*

@Composable
fun UserInfoScreen(
    vm: AuthViewModel,            // Recibimos el ViewModel global
    onLogout: () -> Unit          // Acción para cerrar sesión
) {
    val session by vm.session.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LilaPri)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Ícono principal
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = session.userName ?: "Usuario desconocido",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = session.userEmail ?: "Sin correo registrado",
                style = MaterialTheme.typography.bodyLarge.copy(color = Blanco)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = when (session.userRoleId) {
                    2L -> "Rol: Administrador"
                    1L -> "Rol: Cliente"
                    else -> "Rol desconocido"
                },
                style = MaterialTheme.typography.bodyMedium.copy(color = Blanco)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    vm.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Cerrar Sesión", color = Blanco)
            }
        }
    }
}
