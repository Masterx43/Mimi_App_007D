package com.example.uinavegacion.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI

data class WorkerUiState(
    val reservas: List<ReservaDetalleDTO> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class WorkerViewModel(
    private val reservaRepository: ReservaRepositoryAPI   //  ahora usa el repo
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerUiState())
    val uiState: StateFlow<WorkerUiState> = _uiState

    fun cargarTodasLasReservas(id : Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null) }
            val result = reservaRepository.obtenerReservasDetalleTrabajador(id)

            result.onSuccess { lista ->
                _uiState.update { it.copy(reservas = lista) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun marcarCompletada(reservaId: Long, id : Long) {
        viewModelScope.launch {
            val result = reservaRepository.actualizarEstado(reservaId, "CONFIRMADO")
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