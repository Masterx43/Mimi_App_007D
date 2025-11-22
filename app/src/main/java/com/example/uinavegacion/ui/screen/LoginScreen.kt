package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.theme.Blanco
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun LoginScreenVm(
    onLoginOkNavigateHome:()->Unit,
    onGoRegister: () -> Unit,
    vm : AuthViewModel
){
    val state by vm.login.collectAsStateWithLifecycle()
    val session by vm.session.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    LaunchedEffect(state.success) {
        if (state.success){
            userPrefs.setLoggedIn(true)
            vm.clearLoginResults()
            onLoginOkNavigateHome()
        }
    }

    LoginScreen(
        email=state.email,
        contra=state.contra,
        emailError= state.emailError,
        contraError=state.contraError,
        isSubmitting=state.isSubmitting,
        canSubmit=state.canSubmit,
        errorMsg=state.errorMsg,
        success = state.success,
        userName = session.userName,
        useRoleId= session.userRoleId,
        onEmailChange= vm::onLoginEmailChange,
        onPassChange= vm::onLoginContraChange,
        onSubmit= vm::submitLoginAPI,
        onGoRegister= onGoRegister,
        onClear= vm::clearLoginResults,
        onNavigateHome= onLoginOkNavigateHome
    )
}
@Composable // Pantalla Login (solo navegación, sin formularios)
private fun LoginScreen(
    email: String,
    contra: String,
    emailError: String?,
    contraError: String?,
    isSubmitting: Boolean,
    canSubmit: Boolean,
    errorMsg: String?,
    success: Boolean,
    userName: String?,
    useRoleId: Long?,
    onEmailChange: (String)-> Unit,
    onPassChange: (String)-> Unit,
    onSubmit: ()->Unit,
    onGoRegister: () -> Unit,// Acción para ir a Registro
    onClear: ()-> Unit,
    onNavigateHome: ()-> Unit
) {
    val Blanco = Blanco //fondo
    var showPass by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo
            .background(Blanco) // Fondo
            .padding(16.dp), // Margen
        contentAlignment = Alignment.Center // Centro
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = LilaPri,  ///
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall.copy(color = LilaPri)// Título
            )
            Spacer(Modifier.height(12.dp)) // Separación
            Text(
                text = "Inicie sesion para poder reservar nuestros servicios.",
                textAlign = TextAlign.Center // Alineación centrada
            )
            Spacer(Modifier.height(20.dp)) // Separación

            OutlinedTextField(
                value = email,                               // Valor actual
                onValueChange = onEmailChange, // Notifica VM (valida email)
                label = { Text("Email")},                   // Etiqueta
                singleLine = true,                           // Una línea
                isError = emailError != null,                // Marca error si corresponde
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email        // Teclado de email
                ),
                modifier = Modifier.fillMaxWidth(),// Ancho completo
                colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LilaPri,
                        focusedLabelColor = LilaPri
            )
            )
            if (emailError != null) {                        // Muestra mensaje si hay error
                Text(emailError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))                    // Espacio

            // ---------- Contraseña (oculta por defecto) ----------
            OutlinedTextField(
                value = contra,                                // Valor actual
                onValueChange = onPassChange,                // Notifica VM
                label = { Text("Contraseña") },              // Etiqueta
                singleLine = true,                           // Una línea
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(), // Toggle mostrar/ocultar
                trailingIcon = {                             // Ícono para alternar visibilidad
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = contraError != null,                 // (Opcional) marcar error
                modifier = Modifier.fillMaxWidth() , // Ancho completo
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LilaPri,
                    focusedLabelColor = LilaPri
                )
            )
            if (contraError != null) {                         // (Opcional) mostrar error
                Text(contraError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))                   // Espacio

            // ---------- Boton para entrar ----------
            Button(
                onClick = onSubmit, // Envía login
                colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                enabled = canSubmit && !isSubmitting,        // Solo si válido y no cargando
                modifier = Modifier.fillMaxWidth()           // Ancho completo
            ) {
                if (isSubmitting) {                          // UI de carga
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Validando...")
                } else {
                    Text("Entrar")
                }
            }

            if (errorMsg != null) {                          // Error global (credenciales)
                Spacer(Modifier.height(8.dp))
                Text(errorMsg, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))                   // Espacio

            // ---------- Boton para ir al registro ----------
            OutlinedButton(onClick = onGoRegister,
                colors = ButtonDefaults.buttonColors(containerColor = LilaPri),
                modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta")
            }
            //fin modificacion de formulario
        }

        //Alerta intrusiva (éxito / error)
        if (success || errorMsg != null) {
            val isSuccess = success
            val userRole = when (useRoleId) {
                1L -> "Cliente"
                2L -> "Administrador"
                3L -> "Trabajador"
                else -> "Rol desconocido"
            }

            AlertDialog(
                onDismissRequest = {
                    onClear()
                    if (isSuccess) onNavigateHome()
                },
                confirmButton = {
                    TextButton(onClick = {
                        onClear()
                        if (isSuccess) onNavigateHome()
                    }) {
                        Text("Aceptar", color = LilaPri)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isSuccess) LilaPri else Color.Red,
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        text = if (isSuccess) "¡Inicio de sesión exitoso!" else "Error de autenticación",
                        color = if (isSuccess) LilaPri else Color.Red
                    )
                },
                text = {
                    Text(
                        text = if (isSuccess)
                            "Bienvenido/a ${userName ?: ""}.\nTu rol es: $userRole."
                        else
                            errorMsg ?: "Error desconocido. Verifica tus credenciales.",
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
