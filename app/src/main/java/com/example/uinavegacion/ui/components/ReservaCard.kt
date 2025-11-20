package com.example.uinavegacion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.data.local.entities.reservas.ReservaDetalle
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO

@Composable
fun CardReserva(reserva: ReservaDetalleDTO) {
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
                reserva.servicio,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text("Cliente: ${reserva.usuario}")
            Text("Fecha: ${reserva.fecha}")
            Text("Hora: ${reserva.hora}")

            Spacer(Modifier.height(6.dp))

            EstadoChip(reserva.estado)
        }
    }
}
