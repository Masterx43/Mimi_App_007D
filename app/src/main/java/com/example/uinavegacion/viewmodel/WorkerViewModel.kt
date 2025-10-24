package com.example.uinavegacion.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.uinavegacion.data.local.entities.reservas.ReservaDetalle
import com.example.uinavegacion.data.repository.ReservaRepository

data class WorkerUiState(
    val reservas: List<ReservaDetalle> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class WorkerViewModel(
    private val reservaRepository: ReservaRepository   //  ahora usa el repo
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerUiState())
    val uiState: StateFlow<WorkerUiState> = _uiState

    fun cargarTodasLasReservas() {
        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null) }
            val result = reservaRepository.obtenerTodasLasReservasConDetalles()

            result.onSuccess { lista ->
                _uiState.update { it.copy(reservas = lista) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun marcarCompletada(reservaId: Long) {
        viewModelScope.launch {
            val result = reservaRepository.actualizarEstadoReservaPorId(reservaId, nuevoEstado = 2L)
            result.onSuccess {
                _uiState.update { it.copy(successMessage = " Reserva $reservaId marcada como completada") }
                cargarTodasLasReservas() //recargar la lista
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = "Error al actualizar: ${e.message}") }
            }
        }
    }
}