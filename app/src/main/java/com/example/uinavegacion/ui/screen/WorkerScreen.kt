package com.example.uinavegacion.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
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
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedReservaId by remember { mutableStateOf<Long?>(null) }
    val id = session.userId



    LaunchedEffect(session.userId) {
        if (session.userId != null) {
            workerVm.cargarTodasLasReservas(id ?: -1)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
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
                imageVector = Icons.Filled.AssignmentInd,
                contentDescription = "Trabajador",
                tint = LilaPri,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = session.userName ?: "Trabajador",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = LilaPri,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = session.userEmail ?: "Sin correo registrado",
                style = MaterialTheme.typography.bodyLarge.copy(color = LilaPri)
            )

            Spacer(Modifier.height(24.dp))

            //Lista de reservas asignadas
            Text(
                "Reservas asignadas:",
                color = LilaPri,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            if (uiState.loading) {
                CircularProgressIndicator(
                    color = LilaPri,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally)
                )
                return@Column // evita que se renderice lo demás
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
                            Text("Fecha: ${reserva.fecha}")
                            Text("Hora: ${reserva.hora}")
                            Text("Cliente: ${reserva.usuario}")
                            Text("Servicio: ${reserva.servicio}")
                            Text("Estado: ${reserva.estado}")

                            Spacer(Modifier.height(8.dp))
                            if (reserva.estado == "Pendiente") {
                                Button(
                                    onClick = {
                                        selectedReservaId = reserva.idReserva
                                        showConfirmDialog = true
                                              },
                                    colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
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
//            uiState.successMessage?.let {
//                Text(it, color = Color.Green, textAlign = TextAlign.Center)
//            }
//            uiState.errorMessage?.let {
//                Text(it, color = Color.Red, textAlign = TextAlign.Center)
//            }

            Spacer(Modifier.height(32.dp))

            // Botón de cerrar sesión
            Button(
                onClick = {
                    vm.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", tint = Blanco)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", color = Blanco)
            }

            //Alerta instrusiva para confirmar
            if (showConfirmDialog && selectedReservaId != null) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = {
                        Text("Confirmar acción", color = LilaPri, fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text(
                            "¿Deseas marcar la reserva #$selectedReservaId como completada?",
                            color = Color.Black
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            workerVm.marcarCompletada(selectedReservaId!!, id ?: -1)
                            showConfirmDialog = false
                        }) {
                            Text("Confirmar", color = LilaPri)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("Cancelar", color = Color.Gray)
                        }
                    },
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            //Alerta instrusiva
            if (uiState.successMessage != null || uiState.errorMessage != null) {
                val isSuccess = uiState.successMessage != null
                AlertDialog(
                    onDismissRequest = { workerVm.clearMessages() },
                    confirmButton = {
                        TextButton(onClick = { workerVm.clearMessages() }) {
                            Text("Aceptar", color = LilaPri)
                        }
                    },
                    title = {
                        Text(
                            text = if (isSuccess) "¡Éxito!" else "Error",
                            color = if (isSuccess) LilaPri else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = uiState.successMessage ?: uiState.errorMessage ?: "",
                            color = Color.Black
                        )
                    },
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}