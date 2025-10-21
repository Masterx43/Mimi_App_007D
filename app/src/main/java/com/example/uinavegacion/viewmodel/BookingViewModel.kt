package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioDao
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingUiState(
    val nombre: String = "",
    val email: String = "",
    val servicio: String = "",
    val servicioId: Long? = null,
    val fecha: String = "",
    val hora: String = "",
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val serviciosDisponibles: List<ServicioEntity> = emptyList()
)

class BookingViewModel(
    private val reservaRepository: ReservaRepository, // para guardar la reserva
    private val servicioDao: ServicioDao, // para cargar los servicios
    private val userId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState

    init {
        cargarServicios() // cargamos los servicios de la base
    }

    private fun cargarServicios() {
        viewModelScope.launch {
            try {
                val lista = servicioDao.getAllServicios()
                _uiState.update { it.copy(serviciosDisponibles = lista) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar servicios") }
            }
        }
    }

    fun onNombreChange(value: String) = _uiState.update { it.copy(nombre = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onServicioChange(nombre: String, id: Long) = _uiState.update { it.copy(servicio = nombre, servicioId = id) }
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
                try {
                    // Crear y guardar la reserva en la base de datos
                    val reserva = ReservaEntity(
                        fechaReserva = s.fecha,
                        subtotal = 10000, // ejemplo, puedes calcular con el precio del servicio
                        userId = userId,
                        estadoId = 1L,
                        servicioId = s.servicioId ?: 1L
                    )
                    reservaRepository.crearReserva(reserva)

                    _uiState.update {
                        it.copy(
                            successMessage = "Reserva agendada con éxito para ${s.fecha} a las ${s.hora} (${s.servicio})",
                            errorMessage = null
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al registrar la reserva: ${e.message}",
                            successMessage = null
                        )
                    }
                }
            }
        }
    }
}
