package com.example.uinavegacion.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.uinavegacion.data.repository.ReservaRepository

data class WorkerUiState(
    val reservas: List<ReservaEntity> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class WorkerViewModel(
    private val reservaRepository: ReservaRepository   //  ahora usa el repo
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerUiState())
    val uiState: StateFlow<WorkerUiState> = _uiState

    fun cargarReservas(workerId: Long) {
        viewModelScope.launch {
            val result = reservaRepository.obtenerTodasLasReservas()

            if (result.isSuccess) {
                val lista = result.getOrNull()?.filter { it.userId == workerId } ?: emptyList()
                _uiState.update { it.copy(reservas = lista, errorMessage = null) }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al cargar reservas: ${result.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }

    fun marcarCompletada(reserva: ReservaEntity) {
        viewModelScope.launch {
            try {
                val actualizada = reserva.copy(estadoId = 2L) //  Estado 2 = Completada
                val result = reservaRepository.crearReserva(actualizada)

                if (result.isSuccess) {
                    _uiState.update {
                        it.copy(successMessage = " Reserva ${reserva.idReserva} marcada como completada")
                    }
                    cargarReservas(reserva.userId)
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al actualizar reserva: ${result.exceptionOrNull()?.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al actualizar reserva: ${e.message}")
                }
            }
        }
    }
}