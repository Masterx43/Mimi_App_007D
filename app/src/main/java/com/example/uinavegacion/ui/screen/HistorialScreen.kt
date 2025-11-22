package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.viewmodel.HistorialViewModel
import com.example.uinavegacion.ui.components.CardReserva
import com.example.uinavegacion.ui.components.EstadoChip
import com.example.uinavegacion.ui.theme.BackDark
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.AuthViewModel

@Composable
fun HistorialScreen(
    historialVm: HistorialViewModel,
    authVm : AuthViewModel
) {
    val session by authVm.session.collectAsState()
    val userId = session.userId ?: 0                        // obtiene id actual

    val state by historialVm.uiState.collectAsState()

    LaunchedEffect(userId) {
        historialVm.cargarHistorialUsuario(userId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = "Agenda",
            tint = LilaPri,  ///
            modifier = Modifier.size(150.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Reservas",
            style = MaterialTheme.typography.headlineSmall.copy(color = LilaPri)// Título
        )

        Spacer(Modifier.height(16.dp))

        // Loading
        if (state.loading) {
            CircularProgressIndicator()
            return@Column
        }

        // Error
        state.errorMessage?.let {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = it,
                        color = LilaPri,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            return@Column
        }


        // Agrupar por fecha
        val reservasPorFecha = state.reservas.groupBy { it.fecha }

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


