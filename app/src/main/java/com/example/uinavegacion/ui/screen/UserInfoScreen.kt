package com.example.uinavegacion.ui.screen

import android.R
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.theme.*
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.BookingViewModel
import com.example.uinavegacion.viewmodel.UserInfoViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private fun createTempImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply {
        if (!exists()) mkdirs()
    }
    return File(storageDir, "IMG_$timeStamp.jpg")
}

private fun getImageUriFile(context: Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}

@Composable
fun UserInfoScreen(
    vm: AuthViewModel,
    onLogout: () -> Unit,
    userInfoVm: UserInfoViewModel,
    userPrefs: UserPreferences,
    bookingVm: BookingViewModel
) {
    val context = LocalContext.current
    val session by vm.session.collectAsState()
    val userIdPref by userPrefs.userId.collectAsState(initial = null)
    val state by userInfoVm.uiState.collectAsState()
    val bookingState by bookingVm.uiState.collectAsState() //muestra reservas

    val scrollState = rememberScrollState()
    val effectiveUserId: Long? = userIdPref ?: session.userId

    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // --- Cargar usuario son sus reservas ---
    val currentUserId = remember(effectiveUserId, session.userId) {
        effectiveUserId ?: session.userId
    }

// --- Efecto 1: carga la info del usuario ---
    LaunchedEffect(currentUserId) {
        if (currentUserId != null && currentUserId > 0) {
            try {
                userInfoVm.cargarUsuario(currentUserId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

// --- Efecto 2: carga las reservas ---
    LaunchedEffect(currentUserId) {
        if (currentUserId != null && currentUserId > 0) {
            try {
                bookingVm.cargarReservasUsuario(currentUserId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- Loading ---
    if (state.loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Blanco)
        }
        return
    }

    // --- Error ---
    state.error?.let { msg ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = msg, color = Color.Red)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { effectiveUserId?.let { userInfoVm.cargarUsuario(it) } }) {
                    Text("Reintentar")
                }
            }
        }
        return
    }

    // --- Sin usuario ---
    if (state.user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Esperando ID de usuario...", color = Blanco)
        }
        return
    }

    // --- Lanzadores para cámara y galería ---
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUriString = pendingCaptureUri.toString()
            Toast.makeText(context, "Foto guardada exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            pendingCaptureUri = null
            Toast.makeText(context, "No se logró capturar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    val pickFromGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUriString = it.toString()
            Toast.makeText(context, "Imagen seleccionada desde galería", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Campos editables ---
    var nombre by remember { mutableStateOf(session.userName ?: "") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf(session.userEmail ?: "") }
    var telefono by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // --- Foto de perfil ---
            if (photoUriString.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil",
                    tint = LilaPri,  ///
                    modifier = Modifier.size(150.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(photoUriString))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(3.dp, LilaPri, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "${state.user?.nombre ?: ""} ${state.user?.apellido ?: ""}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = LilaPri,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // --- Botones de imagen ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val file = createTempImageFile(context)
                        val uri = getImageUriFile(context, file)
                        pendingCaptureUri = uri
                        takePictureLauncher.launch(uri)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LilaPri)
                ) {
                    Text("Tomar foto", color = Blanco)
                }

                OutlinedButton(
                    onClick = { pickFromGalleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = LilaPri.copy(alpha = 0.2f))
                ) {
                    Text("Desde galería", color = LilaPri)
                }
            }

            Spacer(Modifier.height(20.dp))

            // --- CARD de información ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                colors = CardDefaults.cardColors(containerColor =LilaPri.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Su informacion",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = LilaPri,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = state.user?.correo ?: "Correo no disponible",
                        style = MaterialTheme.typography.bodyLarge.copy(color = LilaPri),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = state.user?.phone ?: "Número no disponible",
                        style = MaterialTheme.typography.bodyLarge.copy(color = LilaPri),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = when (state.user?.rolId) {
                            1L -> "Cliente"
                            2L -> "Administrador"
                            3L -> "Trabajador"
                            else -> "Rol desconocido"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(color = LilaPri.copy(alpha = 0.9f)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Botón editar ---
            OutlinedButton(
                onClick = {
                    isEditing = !isEditing
                    if (isEditing) {
                        nombre = ""
                        apellido = ""
                        correo = ""
                        telefono = ""
                        contra = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = LilaPri)
                Spacer(Modifier.width(8.dp))
                Text(if (isEditing) "Cancelar" else "Editar información", color = LilaPri)
            }

            Spacer(Modifier.height(12.dp))

            // --- Campos editables (animados) ---
            AnimatedVisibility(visible = isEditing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    listOf(
                        "Nombre" to nombre,
                        "Apellido" to apellido,
                        "Correo" to correo,
                        "Teléfono" to telefono,
                        "Contraseña" to contra
                    ).forEach { (label, value) ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = {
                                when (label) {
                                    "Nombre" -> nombre = it
                                    "Apellido" -> apellido = it
                                    "Correo" -> correo = it
                                    "Teléfono" -> telefono = it
                                    "Contraseña" -> contra = it
                                }
                            },
                            label = { Text(label) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LilaPri,
                                focusedLabelColor = LilaPri
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            state.user?.let { currentUser ->
                                val updatedUser = currentUser.copy(
                                    nombre = nombre.ifEmpty { currentUser.nombre },
                                    apellido = apellido.ifEmpty { currentUser.apellido },
                                    correo = correo.ifEmpty { currentUser.correo },
                                    phone = telefono.ifEmpty { currentUser.phone },
                                    pass = contra.ifEmpty { currentUser.pass }
                                )
                                userInfoVm.actualizarUsuario(updatedUser)
                                Toast.makeText(context, "Datos actualizados", Toast.LENGTH_SHORT).show()
                                isEditing = false
                            } ?: Toast.makeText(context, "Error: usuario no cargado", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar cambios", color = Blanco)
                    }
                }
            }



            //seccion para ver las reservas
            Spacer(Modifier.height(24.dp))
            Text("Mis Reservas", style = MaterialTheme.typography.titleMedium, color = LilaPri)

            if (bookingState.reservaUsuario.isEmpty()) {
                Text("Aún no tienes reservas registradas.", color = Color.Gray)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    bookingState.reservaUsuario.forEach { reserva ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Servicio: ${reserva.servicio}", fontWeight = FontWeight.Bold, color = LilaPri)
                                Text("Fecha: ${reserva.fecha}")
                                Text("Hora: ${reserva.hora}")
                                Text("Estado: ${reserva.estado}")
                            }
                        }
                    }
                }
            }



            Spacer(Modifier.height(32.dp))

            // --- Botón cerrar sesión ---
            Button(
                onClick = {
                    vm.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Cerrar Sesión", color = Blanco)
            }

        }
    }
}
