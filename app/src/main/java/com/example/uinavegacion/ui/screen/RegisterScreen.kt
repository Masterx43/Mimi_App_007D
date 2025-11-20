package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.ui.theme.Blanco
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.viewmodel.AuthViewModel


@Composable
fun RegisterScreenVm(
    onRegisteredNavigateLogin:() -> Unit,
    onGoLogin: () -> Unit,
    vm : AuthViewModel
){
    val state by vm.register.collectAsStateWithLifecycle()


    RegisterScreen(
        nombre= state.nombre,
        apellido = state.apellido,
        email= state.email,
        cel=state.cel,
        contra=state.contra,
        confirm=state.confirm,

        nombreError=state.nombreError,
        apellidoError = state.apellidoError,
        emailError=state.emailError ,
        celError= state.celError,
        confirmError=state.confirmError,
        contraError = state.contraError,

        isSubmitting= state.isSubmitting ,
        canSubmit= state.canSubmit,
        errorMsg= state.errorMsg,

        onNombreChange= vm::onNombreChange,
        onApellidoChange = vm::onApellidoChange,
        onEmailChange= vm::onEmailChange,
        onCelChange= vm::onCelChange,
        onContraChange= vm::onContraChange,
        onConfirmChange= vm::onConfirmChange,

        onSubmit = vm::submitRegisterAPI,
        onGoLogin= onGoLogin


    )
    if (state.success || state.errorMsg != null) {
        val isSuccess = state.success

        AlertDialog(
            onDismissRequest = {
                vm.clearRegisterResult()
                if (isSuccess) onRegisteredNavigateLogin()
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearRegisterResult()
                    if (isSuccess) onRegisteredNavigateLogin()
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
                    text = if (isSuccess) "¡Registro exitoso!" else "Error en el registro",
                    color = if (isSuccess) LilaPri else Color.Red
                )
            },
            text = {
                Text(
                    text = if (isSuccess)
                        "Tu cuenta se ha creado correctamente.\nAhora puedes iniciar sesión."
                    else
                        state.errorMsg ?: "Ocurrió un error inesperado.",
                    color = Color.Black
                )
            },
            containerColor = Color.White,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
@Composable // Pantalla Registro (solo navegación)
private fun RegisterScreen(
    nombre:String,
    apellido : String,
    email: String,
    cel:String,
    contra:String,
    confirm:String,

    nombreError:String?,
    apellidoError:String?,
    emailError:String?,
    celError:String?,
    confirmError:String?,
    contraError: String?,

    isSubmitting: Boolean,
    canSubmit: Boolean,
    errorMsg:String?,

    onNombreChange:(String)-> Unit,
    onApellidoChange:(String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCelChange: (String) -> Unit,
    onContraChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,

    onSubmit: () -> Unit,
    onGoLogin: () -> Unit     // Acción alternativa a Login
) {

    var showPass by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo
            .background(Blanco) // Fondo
            .padding(16.dp)// Margen
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center // Centro
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { //Estructura vertical
            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineSmall //Título
            )
            Spacer(Modifier.height(12.dp)) //Separación
            Text(
                text = "Registrese para poder utilizar nuestros servicios"
            )
            OutlinedTextField(
                value = nombre,                                // Valor actual
                onValueChange = onNombreChange,                // Notifica VM (filtra y valida)
                label = { Text("Nombre") },                  // Etiqueta
                singleLine = true,                           // Una línea
                isError = nombreError != null,                 // Marca error
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text         // Teclado de texto
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (nombreError != null) {                         // Muestra error
                Text(nombreError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(8.dp)) //Separación

            OutlinedTextField(
                value = apellido,
                onValueChange = onApellidoChange,
                label = { Text("Apellido") },
                singleLine = true,
                isError = apellidoError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            if (apellidoError != null) {
                Text(
                    apellidoError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,                               // Valor actual
                onValueChange = onEmailChange,               // Notifica VM (valida)
                label = { Text("Email") },                   // Etiqueta
                singleLine = true,                           // Una línea
                isError = emailError != null,                // Marca error
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email        // Teclado de email
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) {                        // Muestra error
                Text(emailError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))                    // Espacio

            // ---------- Celular (solo números). El VM ya filtra a dígitos ----------
            OutlinedTextField(
                value = cel,                               // Valor actual (solo dígitos)
                onValueChange = onCelChange,               // Notifica VM (filtra y valida)
                label = { Text("Teléfono") },                // Etiqueta
                singleLine = true,                           // Una línea
                isError = celError != null,                // Marca error
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number       // Teclado numérico
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (celError != null) {                        // Muestra error
                Text(celError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))                    // Espacio

            // ---------- PASSWORD (segura) ----------
            OutlinedTextField(
                value = contra,                                // Valor actual
                onValueChange = onContraChange,                // Notifica VM (valida fuerza)
                label = { Text("Contraseña") },              // Etiqueta
                singleLine = true,                           // Una línea
                isError = contraError != null,                 // Marca error
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(), // Oculta/mostrar
                trailingIcon = {                             // Icono para alternar visibilidad
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.Email else Icons.Filled.Email,
                            contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (contraError != null) {                         // Muestra error
                Text(contraError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))                    // Espacio

            // ---------- CONFIRMAR PASSWORD ----------
            OutlinedTextField(
                value = confirm,                             // Valor actual
                onValueChange = onConfirmChange,             // Notifica VM (valida igualdad)
                label = { Text("Confirmar contraseña") },    // Etiqueta
                singleLine = true,                           // Una línea
                isError = confirmError != null,              // Marca error
                visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(), // Oculta/mostrar
                trailingIcon = {                             // Icono para alternar visibilidad
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(
                            imageVector = if (showConfirm) Icons.Filled.Email else Icons.Filled.Email,
                            contentDescription = if (showConfirm) "Ocultar confirmación" else "Mostrar confirmación"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmError != null) {                      // Muestra error
                Text(confirmError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))                   // Espacio

            // ---------- Boton para registrar ----------
            Button(
                onClick = onSubmit,
                enabled = canSubmit && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)               //más grande y visible
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LilaPri,    // tu color principal
                    contentColor = Color.White
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Creando cuenta…")
                } else {
                    Text(
                        "Registrar",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


            if (errorMsg != null) {                          // Error global (ej: usuario duplicado)
                Spacer(Modifier.height(8.dp))
                Text(errorMsg, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))                   // Espacio

            // ---------- Boton para ir al login ----------
            OutlinedButton(onClick = onGoLogin, modifier = Modifier.fillMaxWidth()) {
                Text("Ir a Login")
            }
        }
    }
}