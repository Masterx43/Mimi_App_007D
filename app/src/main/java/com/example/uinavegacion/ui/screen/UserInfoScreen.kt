package com.example.uinavegacion.ui.screen


import android.content.Context
import android.net.Uri
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
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
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
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.theme.*
import com.example.uinavegacion.viewmodel.UserInfoViewModel
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun createTempImageFile(context: Context): File{
    val timeStamp= SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply {
        if(!exists()) mkdirs()//crea la carpeta si no existe
    }
    return File(storageDir, "IMG_$timeStamp.jpg")
}
private fun getImageUriFile(context: Context, file: File): Uri{
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context,authority,file)
}

@Composable
fun UserInfoScreen(
    vm: AuthViewModel,                  // Recibimos el ViewModel global
    onLogout: () -> Unit,               // Acción para cerrar sesión
    userInfoVm: UserInfoViewModel,
    userPrefs: UserPreferences
) {
    val session by vm.session.collectAsState()
    val context= LocalContext.current
    val scrollState = rememberScrollState()
    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember {mutableStateOf<Uri?>(null)}
    var isEditing by remember { mutableStateOf(false) }

    val userId by userPrefs.userId.collectAsState(initial = null)
    val state by userInfoVm.uiState.collectAsState()


    LaunchedEffect(userId) {
        if (userId != null) {
            userInfoVm.cargarUsuario(userId!!)
        }
    }


    if (state.loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    if (state.user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Cargando información del usuario...",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }


    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success->
        if (success){
            photoUriString=pendingCaptureUri.toString()
            Toast.makeText(context, "Foto guartdada exitosamente", Toast.LENGTH_SHORT).show()

        }else{
            pendingCaptureUri=null
            Toast.makeText(context, "No se logro capturar la imagen", Toast.LENGTH_SHORT).show()
        }

    }

    //campos editables del usuario
    var nombre by remember { mutableStateOf(session.userName ?: "") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf(session.userEmail ?: "") }
    var telefono by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LilaPri)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)

        ) {  //FOTO DE PERFIL
            if (photoUriString.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
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
                        .border(3.dp, Rosado, CircleShape)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón de cámara
            Button(
                onClick = {
                    val file = createTempImageFile(context)
                    val uri = getImageUriFile(context, file)
                    pendingCaptureUri = uri
                    takePictureLauncher.launch(uri)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Rosado)
            ) {
                Text(if (photoUriString.isNullOrEmpty()) "Tomar foto" else "Volver a tomar")
            }

            // 🧾 DATOS DE USUARIO
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.user?.nombre ?: "Nombre no disponible"} , ${state.user?.apellido ?: "Apellido no disponible"}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Blanco,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = state.user?.correo ?: "Correo no disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Blanco.copy(alpha = 0.9f)
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = state.user?.phone ?: "Numero de Celular no disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Blanco.copy(alpha = 0.9f)
                    ),
                    textAlign = TextAlign.Center
                )



                Spacer(Modifier.height(4.dp))

                Text(
                    text = when (state.user?.rolId) {
                        2L -> "Administrador"
                        3L -> "Trabajador"
                        1L -> "Cliente"
                        else -> "Rol desconocido"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blanco.copy(alpha = 0.8f)),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))



            // Botón Editar
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
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Blanco)
                Spacer(Modifier.width(8.dp))
                Text(if (isEditing) "Cancelar" else "Editar información", color = Blanco)
            }

            Spacer(Modifier.height(12.dp))

            AnimatedVisibility(visible = isEditing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Rosado,
                            focusedLabelColor = Rosado
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Rosado,
                            focusedLabelColor = Rosado
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Rosado,
                            focusedLabelColor = Rosado
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Rosado,
                            focusedLabelColor = Rosado
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contra,
                        onValueChange = { contra = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Rosado,
                            focusedLabelColor = Rosado
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            state.user?.let { currentUser -> //  Solo si el usuario está cargado
                                val updatedUser = currentUser.copy( //  Creamos una copia modificada
                                    nombre = nombre.ifEmpty { currentUser.nombre },
                                    apellido = apellido.ifEmpty { currentUser.apellido },
                                    correo = correo.ifEmpty { currentUser.correo },
                                    phone = telefono.ifEmpty { currentUser.phone },
                                    pass = contra.ifEmpty { currentUser.pass } // mantiene la anterior si no cambia
                                )
                                userInfoVm.actualizarUsuario(updatedUser) //  Llama al metodo del VM
                                Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                                isEditing = false
                            } ?: Toast.makeText(context, "Error: usuario no cargado", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar cambios", color = Blanco)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    vm.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Rosado),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Cerrar Sesión", color = Blanco)
            }
        }
    }
}