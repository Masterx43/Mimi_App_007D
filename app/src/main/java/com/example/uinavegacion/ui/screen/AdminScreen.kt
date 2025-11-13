package com.example.uinavegacion.ui.screen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity
import com.example.uinavegacion.data.local.entities.rol.RolEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.viewmodel.AdminViewModel
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.theme.Blanco
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.ui.theme.textoNegro

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
            .background(Blanco)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera
            Icon(
                imageVector = Icons.Filled.AdminPanelSettings,
                contentDescription = "Administrador",
                tint = LilaPri,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = session.userName ?: "Administrador",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = LilaPri,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = session.userEmail ?: "admin@mimiapp.cl",
                style = MaterialTheme.typography.bodyLarge.copy(color = textoNegro)
            )

            Spacer(Modifier.height(24.dp))

            // CREAR SERVICIO
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear servicio", fontWeight = FontWeight.Bold, color = LilaPri)
                    OutlinedTextField(value = serviceName, onValueChange = { serviceName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    ))
                    OutlinedTextField(value = serviceDesc, onValueChange = { serviceDesc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    ))
                    OutlinedTextField(value = servicePrice, onValueChange = { servicePrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    ))
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val precio = servicePrice.toIntOrNull() ?: 0
                            adminVm.crearServicio(serviceName, serviceDesc, precio, 1L)
                            serviceName = ""
                            serviceDesc = ""
                            servicePrice = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                        modifier = Modifier.fillMaxWidth(),

                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar servicio", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // CREAR CATEGORÍA
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear categoría", fontWeight = FontWeight.Bold, color = LilaPri)
                    OutlinedTextField(value = categoryName, onValueChange = { categoryName = it }, label = { Text("Nombre de categoría") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    ))
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            adminVm.crearCategoria(categoryName)
                            categoryName = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar categoría", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            //CREAR ROL
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Crear rol", fontWeight = FontWeight.Bold, color = LilaPri)
                    OutlinedTextField(value = roleName, onValueChange = { roleName = it }, label = { Text("Nombre del rol") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    ))
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            adminVm.crearRol(roleName)
                            roleName = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Blanco)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar rol", color = Blanco)
                    }
                }
            }


            Spacer(Modifier.height(24.dp))
            //Listados  y estidar /eliminar
            Card (
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                var nombreTrabajador by remember { mutableStateOf("") }
                var apellidoTrabajador by remember { mutableStateOf("") }
                var correoTrabajador by remember { mutableStateOf("") }
                var telefonoTrabajador by remember { mutableStateOf("") }
                var contrasenaTrabajador by remember { mutableStateOf("") }


                Spacer(Modifier.height(16.dp))
                Text("Registrar Nuevo Trabajador",fontWeight = FontWeight.Bold, color = LilaPri)

                OutlinedTextField(
                    value = nombreTrabajador,
                    onValueChange = { nombreTrabajador = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    )
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = apellidoTrabajador,
                    onValueChange = { apellidoTrabajador = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    )
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = correoTrabajador,
                    onValueChange = { correoTrabajador = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    )
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = telefonoTrabajador,
                    onValueChange = { telefonoTrabajador = it.filter { c -> c.isDigit() } },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    )
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contrasenaTrabajador,
                    onValueChange = { contrasenaTrabajador = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
                    )
                )

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nombreTrabajador.isNotBlank() && apellidoTrabajador.isNotBlank()
                            && correoTrabajador.isNotBlank() && telefonoTrabajador.isNotBlank()
                            && contrasenaTrabajador.isNotBlank()
                        ) {
                            adminVm.crearTrabajador(
                                nombreTrabajador.trim(),
                                apellidoTrabajador.trim(),
                                correoTrabajador.trim(),
                                telefonoTrabajador.trim(),
                                contrasenaTrabajador.trim()
                            )
                            nombreTrabajador = ""
                            apellidoTrabajador = ""
                            correoTrabajador = ""
                            telefonoTrabajador = ""
                            contrasenaTrabajador = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Blanco)
                    Spacer(Modifier.width(8.dp))
                    Text("Registrar Trabajador", color = Blanco)
                }

                Spacer(Modifier.height(24.dp))
                Text("Trabajadores registrados:", fontWeight = FontWeight.Bold,color = LilaPri)
                if (uiState.trbajadores.isEmpty()) {
                    Text("Aún no hay trabajadores registrados.", color = Color.Gray)
                } else {
                    uiState.trbajadores.forEach { t ->
                        Text("• ${t.nombre} ${t.apellido} — ${t.correo}", color = Color.DarkGray)
                    }
                }

                Spacer(Modifier.height(32.dp))
            }

            // Mensajes de resultado
            uiState.successMessage?.let {
                Text(it, color = Color.Green, textAlign = TextAlign.Center)
            }
            uiState.errorMessage?.let {
                Text(it, color = Color.Red, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = LilaPri.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    //SERVICIOS
                    Text(
                        "Servicios registrados:",
                        color = textoNegro,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    uiState.servicios.forEach { servicio ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("- ${servicio.nombre} ($${servicio.precio})", color = textoNegro)
                            Row {
                                IconButton(onClick = { adminVm.abrirDialogoEditarServicio(servicio) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LilaPri)// boton para modificar
                                }
                                IconButton(onClick = { adminVm.eliminarServicio(servicio.idServicio) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) //boton para eliminar
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    //CATEGORÍAS
                    Text("Categorías:", color = textoNegro, fontWeight = FontWeight.Bold)
                    uiState.categorias.forEach { categoria ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("- ${categoria.nombreCategoria}", color = textoNegro)
                            Row {
                                IconButton(onClick = { adminVm.abrirDialogoEditarCategoria(categoria) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LilaPri) // boton para modificar
                                }
                                IconButton(onClick = { adminVm.eliminarCategoria(categoria.idCategoria) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) //boton para eliminar
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    //ROLES
                    Text("Roles:", color = textoNegro, fontWeight = FontWeight.Bold)
                    uiState.roles.forEach { rol ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("- ${rol.descripcion}", color = textoNegro)
                            Row {
                                IconButton(onClick = { adminVm.abrirDialogoEditarRol(rol) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LilaPri) // boton para modificar
                                }
                                IconButton(onClick = { adminVm.eliminarRol(rol.idRol) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) //boton para eliminar
                                }
                            }
                        }
                    }
                }
            }




            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { vm.logout(); onLogout() },
                colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", tint = Blanco)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", color = Blanco)
            }


            Spacer(Modifier.height(24.dp))



        }
    }

    // llama las funciones si es que cambian los valores
    if (uiState.servicioAEditar != null) {
        EditarServicioDialog(
            servicio = uiState.servicioAEditar!!,
            onDismiss = { adminVm.cerrarDialogoEditarServicio() },
            onGuardar = { nombre, desc, precio ->
                adminVm.actualizarServicio(
                    uiState.servicioAEditar!!.idServicio,
                    nombre, desc, precio
                )
            }
        )
    }

    if (uiState.categoriaAEditar != null) {
        EditarCategoriaDialog(
            categoria = uiState.categoriaAEditar!!,
            onDismiss = { adminVm.cerrarDialogoEditarCategoria() },
            onGuardar = { nuevoNombre ->
                adminVm.actualizarCategoria(uiState.categoriaAEditar!!.idCategoria, nuevoNombre)
            }
        )
    }

    if (uiState.rolAEditar != null) {
        EditarRolDialog(
            rol = uiState.rolAEditar!!,
            onDismiss = { adminVm.cerrarDialogoEditarRol() },
            onGuardar = { nuevoNombre ->
                adminVm.actualizarRol(uiState.rolAEditar!!.idRol, nuevoNombre)
            }
        )
    }



}

//editar los servcicios que existen
@Composable
fun EditarServicioDialog(
    servicio: ServicioEntity,
    onDismiss: () -> Unit,
    onGuardar: (String, String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf(servicio.nombre) }
    var desc by remember { mutableStateOf(servicio.descripcion) }
    var precio by remember { mutableStateOf(servicio.precio.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar servicio", color = LilaPri) },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LilaPri,
                    focusedLabelColor = LilaPri
                ))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LilaPri,
                    focusedLabelColor = LilaPri
                ))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LilaPri,
                    focusedLabelColor = LilaPri
                ))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(nombre, desc, precio.toIntOrNull() ?: 0)
                onDismiss()
            }) { Text("Guardar", color = LilaPri) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        }
    )
}

//Editar las categorias que existen
@Composable
fun EditarCategoriaDialog(
    categoria: CategoriaEntity,
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {
    var nombre by remember { mutableStateOf(categoria.nombreCategoria) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar categoría", color = LilaPri) },
        text = {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LilaPri,
                focusedLabelColor = LilaPri
            ))
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(nombre)
                onDismiss()
            }) { Text("Guardar", color = LilaPri) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        }
    )
}

//editar los roles que existen
@Composable
fun EditarRolDialog(
    rol: RolEntity,
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {
    var nombre by remember { mutableStateOf(rol.descripcion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar rol", color = LilaPri) },
        text = {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(),colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LilaPri,
                focusedLabelColor = LilaPri
            ))
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(nombre)
                onDismiss()
            }) { Text("Guardar", color = LilaPri) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        }
    )
}

