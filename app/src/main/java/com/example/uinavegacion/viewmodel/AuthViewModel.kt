package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.storage.IUserPreferences
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.AuthRepository
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
import com.example.uinavegacion.domain.validation.validateCelDigitsOnly
import com.example.uinavegacion.domain.validation.validateConfirm
import com.example.uinavegacion.domain.validation.validateEmail
import com.example.uinavegacion.domain.validation.validateNombreLettersOnly
import com.example.uinavegacion.domain.validation.validateStrongPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val email: String="",
    val contra: String="",
    val emailError: String? = null,
    val contraError: String? = null,
    val isSubmitting: Boolean= false,
    val canSubmit: Boolean= false,
    val success: Boolean=false,
    val errorMsg: String?= null

)

data class RegisterUiState(
    val nombre: String= "",
    val apellido: String = "",
    val email: String= "",
    val cel: String= "",
    val contra: String="",
    val confirm: String="",

    val nombreError: String?=null,
    val apellidoError: String? = null,
    val emailError: String? = null,
    val celError: String?= null,
    val contraError: String?= null,
    val confirmError: String?=null,

    val isSubmitting: Boolean= false,
    val canSubmit: Boolean= false,
    val success: Boolean= false,
    val errorMsg: String?= null

)

data class SessionUiState(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val userName: String? = null,
    val userLastName: String? = null,
    val userEmail: String? = null,
    val userPhone: String? = null,
    val userRoleId: Long? = null,
)
class AuthViewModel(
    private val userPrefs: IUserPreferences,
    private val authRepository: AuthRepository,
    private val repositoryTestAPI: UserRepositoryTestAPI
): ViewModel(){

    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login

    private val _register = MutableStateFlow(RegisterUiState())
    val register: StateFlow<RegisterUiState> = _register

    private val _session = MutableStateFlow(SessionUiState())
    val session: StateFlow<SessionUiState> = _session

    init {
        viewModelScope.launch {
            userPrefs.isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    val userId = userPrefs.userId.firstOrNull()
                    if (userId != null) {
                        val result = repositoryTestAPI.getUserById(userId)
                        result.onSuccess { user ->
                            _session.update {
                                SessionUiState(
                                    isLoggedIn = true,
                                    userId = user.idUser,
                                    userName = user.nombre,
                                    userLastName = user.apellido,
                                    userEmail = user.correo,
                                    userPhone = user.phone,
                                    userRoleId = user.rolId
                                )
                            }
                            println(" Sesión restaurada correctamente -> ${user.correo}")
                        }
                        result.onFailure {
                            println(" Error al restaurar sesión: ${it.message}")
                        }
                    }
                }
            }
        }
    }


    fun onLoginEmailChange(value: String){
        _login.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeLoginCanSubmit()
    }
    fun onLoginContraChange(value: String){
        _login.update { it.copy(contra = value) }
        recomputeLoginCanSubmit()
    }

    private fun recomputeLoginCanSubmit(){
        val s= _login.value
        val can= s.emailError==null && s.email.isNotBlank() && s.contra.isNotBlank()
        _login.update { it.copy(canSubmit = can) }
    }


    fun clearLoginResults(){
        _login.update { it.copy(success = false, errorMsg = null) }
    }

    fun onNombreChange(value: String) {
        val limpio = value.filter { it.isLetter() || it.isWhitespace() }
        _register.update { it.copy(nombre = limpio, nombreError = validateNombreLettersOnly(limpio)) }
        recomputeRegisterCanSubmit()
    }

    fun onApellidoChange(value: String) {
        val limpio = value.filter { it.isLetter() || it.isWhitespace() }
        _register.update { it.copy(apellido = limpio, apellidoError = validateNombreLettersOnly(limpio)) }
        recomputeRegisterCanSubmit()
    }

    fun onEmailChange(value: String) {
        _register.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeRegisterCanSubmit()
    }

    fun onCelChange(value: String) {
        val soloNumeros = value.filter { it.isDigit() }
        _register.update { it.copy(cel = soloNumeros, celError = validateCelDigitsOnly(soloNumeros)) }
        recomputeRegisterCanSubmit()
    }

    fun onContraChange(value: String) {
        _register.update { it.copy(contra = value, contraError = validateStrongPassword(value)) }
        _register.update { it.copy(confirmError = validateConfirm(it.contra, it.confirm)) }
        recomputeRegisterCanSubmit()
    }

    fun onConfirmChange(value: String) {
        _register.update { it.copy(confirm = value, confirmError = validateConfirm(it.contra, value)) }
        recomputeRegisterCanSubmit()
    }

    private fun recomputeRegisterCanSubmit() {
        val s = _register.value
        val noErrors = listOf(s.nombreError,s.apellidoError , s.emailError, s.celError, s.contraError, s.confirmError).all { it == null }
        val filled = listOf(s.nombre, s.apellido, s.email, s.cel, s.contra, s.confirm).all { it.isNotBlank() }
        _register.update { it.copy(canSubmit = noErrors && filled) }
    }



    fun clearRegisterResult() {
        _register.update { it.copy(success = false, errorMsg = null) }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.clear()
            _session.update {
                it.copy(
                    isLoggedIn = false,
                    userId = null,
                    userEmail = null,
                    userName = null,
                    userRoleId = null
                )
            }
        }
    }

    fun submitLoginAPI() {
        val s = _login.value
        if (!s.canSubmit || s.isSubmitting) return

        viewModelScope.launch {
            _login.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }

            val result = authRepository.login(s.email, s.contra)

            if (result.isSuccess) {
                val response = result.getOrNull()

                if (response?.success == true && response.user != null) {
                    val user = response.user

                    _session.update {
                        SessionUiState(
                            isLoggedIn = true,
                            userId = user.idUser,
                            userName = user.nombre,
                            userLastName = user.apellido,
                            userPhone = null,
                            userEmail = user.correo,
                            userRoleId = user.rolId
                        )
                    }

                    userPrefs.saveLoginState(true, user.rolId, user.idUser)

                    _login.update { it.copy(isSubmitting = false, success = true) }
                } else {
                    _login.update {
                        it.copy(
                            isSubmitting = false,
                            success = false,
                            errorMsg = response?.message ?: "Credenciales incorrectas"
                        )
                    }
                }
            } else {
                _login.update {
                    it.copy(
                        isSubmitting = false,
                        success = false,
                        errorMsg = result.exceptionOrNull()?.message ?: "Error de autenticación"
                    )
                }
            }
        }
    }


    fun submitRegisterAPI() {
        val s = _register.value
        if (!s.canSubmit || s.isSubmitting) return

        viewModelScope.launch {
            _register.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }

            val result = repositoryTestAPI.register(
                nombre = s.nombre.trim(),
                apellido = s.apellido.trim(),
                correo = s.email.trim(),
                phone = s.cel.trim(),
                password = s.contra,
                rolId = 1L   //Usuario normal por defecto
            )

            if (result.isSuccess) {
                _register.update {
                    it.copy(isSubmitting = false, success = true)
                }
            } else {
                _register.update {
                    it.copy(
                        isSubmitting = false,
                        success = false,
                        errorMsg = result.exceptionOrNull()?.message ?: "No se pudo registrar"
                    )
                }
            }
        }
    }


}



