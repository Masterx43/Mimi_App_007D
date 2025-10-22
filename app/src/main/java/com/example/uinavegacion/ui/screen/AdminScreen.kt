package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
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
import com.example.uinavegacion.viewmodel.AdminViewModel
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.theme.Blanco
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.ui.theme.Rosado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    vm: AuthViewModel,
    onLogout: () -> Unit,
    adminVm: AdminViewModel = viewModel()
) {
    val session by vm.session.collectAsState()
    val uiState by adminVm.uiState.collectAsState()

    // Campos de formulario
    var serviceName by remember { mutableStateOf("") }
    var serviceDesc by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var roleName by remember { mutableStateOf("") }

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
            // Cabecera
            Icon(
                imageVector = Icons.Filled.AdminPanelSettings,
                contentDescription = "Administrador",
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = session.userName ?: "Administrador",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = session.userEmail ?: "admin@mimiapp.cl",
                style = MaterialTheme.typography.bodyLarge.copy(color = Blanco)
            )

            Spacer(Modifier.height(24.dp))

            // 🔧 CREAR SERVICIO
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear servicio", fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = serviceName, onValueChange = { serviceName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = serviceDesc, onValueChange = { serviceDesc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = servicePrice, onValueChange = { servicePrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val precio = servicePrice.toIntOrNull() ?: 0
                            adminVm.crearServicio(serviceName, serviceDesc, precio, 1L)
                            serviceName = ""
                            serviceDesc = ""
                            servicePrice = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar servicio", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 🧾 CREAR CATEGORÍA
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear categoría", fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = categoryName, onValueChange = { categoryName = it }, label = { Text("Nombre de categoría") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            adminVm.crearCategoria(categoryName)
                            categoryName = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar categoría", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 👥 CREAR ROL
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear rol", fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = roleName, onValueChange = { roleName = it }, label = { Text("Nombre del rol") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            adminVm.crearRol(roleName)
                            roleName = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar rol", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Mensajes de resultado
            uiState.successMessage?.let {
                Text(it, color = Color.Green, textAlign = TextAlign.Center)
            }
            uiState.errorMessage?.let {
                Text(it, color = Color.Red, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(16.dp))

            // 🔍 Listados
            Text("Servicios registrados:", color = Blanco, fontWeight = FontWeight.Bold)
            uiState.servicios.forEach {
                Text("- ${it.nombre} ($${it.precio})", color = Blanco)
            }

            Spacer(Modifier.height(8.dp))
            Text("Categorías:", color = Blanco, fontWeight = FontWeight.Bold)
            uiState.categorias.forEach {
                Text("- ${it.nombreCategoria}", color = Blanco)
            }

            Spacer(Modifier.height(8.dp))
            Text("Roles:", color = Blanco, fontWeight = FontWeight.Bold)
            uiState.roles.forEach {
                Text("- ${it.descripcion}", color = Blanco)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { vm.logout(); onLogout() },
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
