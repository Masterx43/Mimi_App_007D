package com.example.uinavegacion.ui.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)  //para autorizar uso de apis experimentales
@Composable
fun BookingScreen(
    vm : BookingViewModel,
    authVm : AuthViewModel
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()
    val session by authVm.session.collectAsState()

    //Lista de horas (que se actualizará cuando elijas una fecha)
    var horasDisponibles by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(session.userEmail) {
        if (session.userName != null) vm.onNombreChange(session.userName!!)
        if (session.userEmail != null) vm.onEmailChange(session.userEmail!!)
    }
    LaunchedEffect(Unit) {
        vm.clearMessages()
        vm.recargarServicios()
        vm.cargarTrabajadores()
    }


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
            readOnly = session.isLoggedIn,
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
            readOnly = session.isLoggedIn,
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
            // Campo de texto que actúa como ancla del menú
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

            // Menú desplegable con scroll
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    state.serviciosDisponibles.forEach { servicio ->
                        DropdownMenuItem(
                            text = { Text("${servicio.nombre} - $${servicio.precio}") },
                            onClick = {
                                vm.onServicioChange(servicio.nombre, servicio.idServicio)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }




        Spacer(Modifier.height(12.dp))

        // ⭐⭐⭐ SELECTOR DE TRABAJADOR ⭐⭐⭐

        Text("Seleccionar trabajador", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        var expandedTrabajador by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedTrabajador,
            onExpandedChange = { expandedTrabajador = !expandedTrabajador }
        ) {
            OutlinedTextField(
                value = state.trabajadores.firstOrNull { it.idUser == state.workerIdSeleccionado }?.nombre
                    ?: "Seleccione un trabajador",
                onValueChange = {},
                readOnly = true,
                label = { Text("Trabajador") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTrabajador) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expandedTrabajador = true }
            )

            ExposedDropdownMenu(
                expanded = expandedTrabajador,
                onDismissRequest = { expandedTrabajador = false }
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    state.trabajadores.forEach { trabajador ->
                        DropdownMenuItem(
                            text = { Text("${trabajador.nombre} ${trabajador.apellido}") },
                            onClick = {
                                vm.onWorkerSelected(trabajador.idUser)
                                expandedTrabajador = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        //Campo de la fecha
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

        //Muestra horas solo si hay fecha válida
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
            onClick = { vm.registrarReserva(session.userId ?: 0L) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,                   //hace el circulo de loader mas fino o grueso depende del valor
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Agendando...")
            } else {
                Text("Agendar")
            }
        }

        // Alerta intrusiva para la reserva
        if (state.successMessage != null || state.errorMessage != null) {
            val isSuccess = state.successMessage != null
            AlertDialog(
                onDismissRequest = { vm.clearMessages() },
                confirmButton = {
                    TextButton(onClick = { vm.clearMessages() }) {
                        Text("Aceptar", color = LilaPri)
                    }
                },
                title = {
                    Text(
                        text = if (isSuccess) "¡Reserva Exitosa!" else "Error al Agendar",
                        color = if (isSuccess) LilaPri else Color.Red
                    )
                },
                text = {
                    Text(
                        text = state.successMessage ?: state.errorMessage ?: "",
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


