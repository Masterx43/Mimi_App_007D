package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaDetalle
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.repository.ReservaRepository
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
data class HistorialUiState(
    val reservas: List<ReservaDetalleDTO> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)



class HistorialViewModel(
    private val reservaRepository: ReservaRepositoryAPI
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState: StateFlow<HistorialUiState> = _uiState

    fun cargarHistorialUsuario(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, errorMessage = null) }

            val result = reservaRepository.obtenerReservasDetalleUsuario(userId)

            result.onSuccess { listaDTO ->

                _uiState.update {
                    it.copy(
                        reservas = listaDTO,
                        loading = false,
                        errorMessage = null
                    )
                }

            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}