package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserUpdateRequestDTO
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserInfoUiState(
    val user: UserDTO? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class UserInfoViewModel(
    private val userApi: UserRepositoryTestAPI
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState: StateFlow<UserInfoUiState> = _uiState

    // Cargar usuario desde el microservicio UserService
    fun cargarUsuario(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }

            val result = userApi.getUserById(userId)

            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        loading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Error al cargar usuario",
                        loading = false
                    )
                }
            }
        }
    }

    // Si después quieres actualizar campos del usuario en el microservicio:
    fun actualizarUsuario(userId: Long, nuevoNombre: String, nuevoApellido: String, nuevoPhone: String?) {
        viewModelScope.launch {

            val updateReq = UserUpdateRequestDTO(
                nombre = nuevoNombre,
                apellido = nuevoApellido,
                phone = nuevoPhone
            )

            val result = userApi.updateUser(userId, updateReq)

            result.onSuccess { actualizado ->
                _uiState.update { it.copy(user = actualizado) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
