package com.example.uinavegacion.ui.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uinavegacion.ui.theme.FondoClaro
import com.example.uinavegacion.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BookingScreen(
    vm : BookingViewModel
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    val horasDisponibles = listOf(
        "09:00", "10:00", "11:00", "12:00",
        "13:00", "14:00", "15:00", "16:00",
        "17:00", "18:00"
    )

    fun abrirCalendario() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(
            context,
            { _, y, m, d ->
                val seleccionada = Calendar.getInstance().apply { set(y, m, d) }

                // Bloquear domingos
                if (seleccionada.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Toast.makeText(
                        context,
                        "No trabajamos los domingos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    vm.onFechaChange(formato.format(seleccionada.time))
                }
            },
            year, month, day
        )

        picker.datePicker.minDate = System.currentTimeMillis()
        picker.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reserva tu hora", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = vm::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.servicio,
            onValueChange = vm::onServicioChange,
            label = { Text("Servicio (Ej: Corte, Manicure...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Fecha
        OutlinedTextField(
            value = state.fecha,
            onValueChange = {},
            label = { Text("Fecha") },
            trailingIcon = {
                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { abrirCalendario() },
            readOnly = true
        )

        Spacer(Modifier.height(16.dp))

        // Horas disponibles
        Text("Selecciona una hora:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        horasDisponibles.forEach { h ->
            Button(
                onClick = { vm.onHoraChange(h) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.hora == h)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    h,
                    color = if (state.hora == h) Color.White else Color.Black // negro si no está seleccionada
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                vm.registrarReserva()
                if (state.successMessage != null)
                    Toast.makeText(context, state.successMessage, Toast.LENGTH_LONG).show()
                else if (state.errorMessage != null)
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agendar")
        }
    }
}
