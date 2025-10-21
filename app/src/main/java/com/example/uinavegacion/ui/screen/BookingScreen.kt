package com.example.uinavegacion.ui.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uinavegacion.ui.theme.FondoClaro
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    vm : BookingViewModel
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    //Lista de horas (que se actualizará cuando elijas una fecha)
    var horasDisponibles by remember { mutableStateOf<List<String>>(emptyList()) }

    //Abrir calendario
    fun abrirCalendario() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(
            context,
            { _, y, m, d ->
                val seleccionada = Calendar.getInstance().apply { set(y, m, d) }

                // Domingo → No trabajamos
                if (seleccionada.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Toast.makeText(context, "No trabajamos los domingos", Toast.LENGTH_SHORT).show()
                    vm.onFechaChange("")
                    horasDisponibles = emptyList()
                } else {
                    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fechaStr = formato.format(seleccionada.time)
                    vm.onFechaChange(fechaStr)

                    // Sábado → Hasta las 14:00
                    // Otros días → Hasta las 18:00
                    horasDisponibles = if (seleccionada.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        listOf("09:00", "10:00", "11:00", "12:00", "13:00", "14:00")
                    } else {
                        listOf(
                            "09:00", "10:00", "11:00", "12:00",
                            "13:00", "14:00", "15:00", "16:00",
                            "17:00", "18:00"
                        )
                    }
                }
            },
            year, month, day
        )

        picker.datePicker.minDate = System.currentTimeMillis()
        picker.show()
    }

    //UI principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reserva tu hora", style = MaterialTheme.typography.headlineSmall,
            color = LilaPri)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = vm::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LilaPri,
                focusedLabelColor = LilaPri
            )
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LilaPri,
                focusedLabelColor = LilaPri
            )
        )

        Spacer(Modifier.height(12.dp))


        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = state.servicio,
                onValueChange = {},
                readOnly = true,
                label = { Text("Servicio") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LilaPri,
                    focusedLabelColor = LilaPri

                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.serviciosDisponibles.forEach { servicios ->
                    DropdownMenuItem(
                        text = {Text("${servicios.nombre} - $${servicios.precio}")},
                        onClick = {
                            vm.onServicioChange(servicios.nombre, servicios.idServicio)
                            expanded= false
                        }
                    )
                }
            }
        }


        Spacer(Modifier.height(12.dp))

        //Campo de fecha
        OutlinedTextField(
            value = state.fecha,
            onValueChange = {},
            label = { Text("Fecha de reserva") },
            trailingIcon = {
                IconButton(onClick = { abrirCalendario() }) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Seleccionar fecha",
                        tint = LilaPri
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LilaPri,
                focusedLabelColor = LilaPri
            )
        )

        Spacer(Modifier.height(16.dp))

        //Mostrar horas solo si hay fecha válida
        if (horasDisponibles.isNotEmpty()) {
            Text("Selecciona una hora:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            horasDisponibles.forEach { h ->
                Button(
                    onClick = { vm.onHoraChange(h) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.hora == h) LilaPri else Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        h,
                        color = if (state.hora == h) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        //Botón "Agendar"
        Button(
            onClick = {
                vm.registrarReserva()
                if (state.successMessage != null)
                    Toast.makeText(context, state.successMessage, Toast.LENGTH_LONG).show()
                else if (state.errorMessage != null)
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LilaPri)
        ) {
            Text("Agendar")
        }
    }
}
