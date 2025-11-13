package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uinavegacion.viewmodel.BookingViewModel

@Composable
fun HistorialScreen(
    navController: NavController,
    vm: BookingViewModel,
    userId: Long
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.cargarReservasUsuario(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Historial de Reservas",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF5A2D82)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.reservaUsuario.isEmpty()) {
            Text("No tienes reservas aún.")
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            val reservasPorFecha = state.reservaUsuario.groupBy { it.fecha }

            reservasPorFecha.forEach { (fecha, reservasLista) ->

                item {
                    Text(
                        text = " $fecha",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF5A2D82),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(reservasLista) { reserva ->

                    Card (
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Servicio: ${reserva.servicio}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Hora: ${reserva.hora}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            val colorEstado = when (reserva.estado) {
                                "Pendiente" -> Color(0xFFFFC107)
                                "Confirmada" -> Color(0xFF4CAF50)
                                "Completada" -> Color(0xFF2196F3)
                                else -> Color.Gray
                            }

                            Text(
                                text = "Estado: ${reserva.estado}",
                                color = colorEstado,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
