package com.example.uinavegacion.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uinavegacion.viewmodel.WorkerViewModel
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerScreen(
    vm: AuthViewModel,
    onLogout: () -> Unit,
    workerVm: WorkerViewModel = viewModel()
) {
    val session by vm.session.collectAsState()
    val uiState by workerVm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        session.userId?.let { workerVm.cargarReservas(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LilaPri)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = "Trabajador",
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = session.userName ?: "Trabajador",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = session.userEmail ?: "Sin correo registrado",
                style = MaterialTheme.typography.bodyLarge.copy(color = Blanco)
            )

            Spacer(Modifier.height(24.dp))

            // 🗓 Lista de reservas asignadas
            Text(
                "Reservas asignadas:",
                color = Blanco,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            if (uiState.reservas.isEmpty()) {
                Text(
                    "No tienes reservas asignadas.",
                    color = Blanco,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                uiState.reservas.forEach { reserva ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Blanco),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Reserva #${reserva.idReserva}",
                                fontWeight = FontWeight.Bold,
                                color = LilaOscuro
                            )
                            Text("Fecha: ${reserva.fechaReserva}")
                            Text("Servicio ID: ${reserva.servicioId}")
                            Text("Estado: ${if (reserva.estadoId == 1L) "Pendiente" else "Completada"}")

                            Spacer(Modifier.height(8.dp))
                            if (reserva.estadoId == 1L) {
                                Button(
                                    onClick = { workerVm.marcarCompletada(reserva) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = "Completar", tint = Blanco)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Marcar completada", color = Blanco)
                                }
                            }
                        }
                    }
                }
            }

            // Mensajes de estado
            uiState.successMessage?.let {
                Text(it, color = Color.Green, textAlign = TextAlign.Center)
            }
            uiState.errorMessage?.let {
                Text(it, color = Color.Red, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(32.dp))

            // Botón de cerrar sesión
            Button(
                onClick = {
                    vm.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", tint = Blanco)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", color = Blanco)
            }
        }
    }
}