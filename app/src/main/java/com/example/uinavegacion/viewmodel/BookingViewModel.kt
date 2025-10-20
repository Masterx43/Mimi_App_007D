package com.example.uinavegacion.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingUiState(
    val nombre: String = "",
    val email: String = "",
    val servicio: String = "",
    val fecha: String = "",
    val hora: String = "",
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class BookingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState

    fun onNombreChange(value: String) = _uiState.update { it.copy(nombre = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onServicioChange(value: String) = _uiState.update { it.copy(servicio = value) }
    fun onFechaChange(value: String) = _uiState.update { it.copy(fecha = value) }
    fun onHoraChange(value: String) = _uiState.update { it.copy(hora = value) }

    fun registrarReserva() {
        viewModelScope.launch {
            val s = _uiState.value
            if (s.nombre.isBlank() || s.email.isBlank() || s.servicio.isBlank() ||
                s.fecha.isBlank() || s.hora.isBlank()
            ) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Completa todos los campos",
                        successMessage = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        successMessage = "Reserva agendada con éxito para ${s.fecha} a las ${s.hora}",
                        errorMessage = null
                    )
                }
            }
        }
    }
}
