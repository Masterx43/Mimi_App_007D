package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.viewmodel.HistorialViewModel
import com.example.uinavegacion.ui.components.CardReserva
import com.example.uinavegacion.viewmodel.AuthViewModel

@Composable
fun HistorialScreen(
    historialVm: HistorialViewModel,
    authVm : AuthViewModel
) {
    val session by authVm.session.collectAsState()
    val userId = session.userId ?: 0                        // ⭐ obtiene id actual

    val state by historialVm.uiState.collectAsState()

    LaunchedEffect(userId) {
        historialVm.cargarHistorialUsuario(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Historial de Reservas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        // Loading
        if (state.loading) {
            CircularProgressIndicator()
            return@Column
        }

        // Error
        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
            return@Column
        }

        // Sin reservas
        if (state.reservas.isEmpty()) {
            Text("No tienes reservas aún.")
            return@Column
        }

        // Agrupar por fecha
        val reservasPorFecha = state.reservas.groupBy { it.fechaReserva }

        reservasPorFecha.forEach { (fecha, lista) ->

            Spacer(Modifier.height(12.dp))

            Text(
                text = fecha,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(8.dp))

            lista.forEach { reserva ->

                CardReserva(reserva)

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}


