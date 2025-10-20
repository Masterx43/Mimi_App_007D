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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.ui.theme.FondoClaro
import com.example.uinavegacion.ui.theme.textoNegro
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BookingScreen() {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var mostrarHoras by remember { mutableStateOf(false) }
    var horasDisponibles by remember { mutableStateOf(listOf<String>()) }
    val scrollState = rememberScrollState()

    // Función para abrir el calendario
    fun abrirCalendario() {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val seleccionada = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }

                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                fecha = formato.format(seleccionada.time)

                when (seleccionada.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SUNDAY -> {
                        Toast.makeText(
                            context,
                            "No trabajamos los domingos. Por favor elige otro día.",
                            Toast.LENGTH_SHORT
                        ).show()
                        mostrarHoras = false
                    }

                    Calendar.SATURDAY -> {
                        Toast.makeText(
                            context,
                            "Los sábados trabajamos hasta las 14:00 ",
                            Toast.LENGTH_SHORT
                        ).show()
                        horasDisponibles = listOf("10:00", "11:00", "12:00", "13:00", "14:00")
                        mostrarHoras = true
                    }

                    else -> {
                        horasDisponibles = listOf(
                            "10:00", "11:00", "12:00", "13:00",
                            "15:00", "16:00", "17:00", "18:00"
                        )
                        mostrarHoras = true
                    }
                }
            },
            year, month, day
        )

        // No permitir fechas pasadas
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    // interfaz de la screen
    Column(
        modifier = Modifier
            .background(FondoClaro)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reserva tu hora", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Servicio
        OutlinedTextField(
            value = servicio,
            onValueChange = { servicio = it },
            label = { Text("Servicio (Ej: Corte, Manicure...)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Fecha de la reserva
        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            label = { Text("Fecha de la reserva") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.clickable { abrirCalendario() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { abrirCalendario() }
        )

        //Horas disponibles (solo si no es domingo)
        if (mostrarHoras) {
            Spacer(Modifier.height(16.dp))
            Text("Selecciona la hora disponible:")
            Spacer(Modifier.height(8.dp))

            horasDisponibles.forEach { h ->
                Button(
                    onClick = { hora = h },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hora == h)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(h, color = textoNegro)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // BOTÓN FINAL DE AGENDAMIENTO
        Button(
            onClick = {
                if (nombre.isBlank() || email.isBlank() || servicio.isBlank() || fecha.isBlank() || hora.isBlank()) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    // Mensaje de confirmación
                    val mensaje = """
                        ¡Reserva agendada con éxito!
                        
                        Cliente: $nombre
                        Correo: $email
                        Servicio: $servicio
                        Fecha: $fecha
                        Hora: $hora
                        
                        Te llegará un correo con la confirmación y los detalles de tu cita.
                    """.trimIndent()

                    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()

                    // Simulación de envío de correo (más adelante puedes integrar un backend real)
                    println("Enviando correo a $email con los datos de la reserva...")

                    // Reset del formulario
                    nombre = ""
                    email = ""
                    servicio = ""
                    fecha = ""
                    hora = ""
                    mostrarHoras = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agendar hora")
        }
    }
}
