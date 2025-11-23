package com.example.uinavegacion.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImagePainter
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI

data class WorkerUiState(
    val reservas: List<ReservaDetalleDTO> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val loading: Boolean= false
)

class WorkerViewModel(
    private val reservaRepository: ReservaRepositoryAPI
) : ViewModel() {

    val _uiState = MutableStateFlow(WorkerUiState())
    val uiState: StateFlow<WorkerUiState> = _uiState

    fun cargarTodasLasReservas(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, errorMessage = null) }

            val result = reservaRepository.obtenerReservasDetalleTrabajador(id)

            result.onSuccess { lista ->
                _uiState.update {
                    it.copy(
                        reservas = lista,
                        loading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        errorMessage = e.message,
                        loading = false
                    )
                }
            }
        }
    }

    fun marcarCompletada(reservaId: Long, id : Long) {
        viewModelScope.launch {
            val result = reservaRepository.actualizarEstado(reservaId, "Confirmado")
            result.onSuccess {
                _uiState.update { it.copy(successMessage ="Reserva $reservaId marcada como completada\n" +
                        "Se enviara un correo al cliente con la confirmacion de la reserva") }
                cargarTodasLasReservas(id) //recargar la lista
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = "Error al actualizar: ${e.message}") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}