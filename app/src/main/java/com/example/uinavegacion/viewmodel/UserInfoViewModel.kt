package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserInfoUiState(
    val user: UserEntity? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class UserInfoViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState: StateFlow<UserInfoUiState> = _uiState

    // Cargar usuario desde el repositorio usando el ID guardado
    fun cargarUsuario(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }

            val result = userRepository.getUserById(userId)
            result.onSuccess { user ->
                _uiState.update { it.copy(user = user, loading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, loading = false) }
            }
        }
    }

    // 🔹 Actualizar datos del usuario
    fun actualizarUsuario(user: UserEntity) {
        viewModelScope.launch {
            val result = userRepository.updateUser(user)
            result.onSuccess {
                _uiState.update { it.copy(user = user) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}