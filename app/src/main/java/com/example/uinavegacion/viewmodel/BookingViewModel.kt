package com.example.uinavegacion.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioDao
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.repository.ReservaRepository
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingUiState(
    val nombre: String = "",
    val email: String = "",
    val servicio: String = "",
    val servicioId: Long? = null,
    val precioServicio: Int? = null,
    val fecha: String = "",
    val hora: String = "",
    val isLoading: Boolean= false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val serviciosDisponibles: List<ServicioEntity> = emptyList(),
    val reservaUsuario: List<BookingViewModel.ReservaUsuario> = emptyList(),
    val trabajadores: List<UserEntity> = emptyList(),
    val workerIdSeleccionado: Long?= null

)

class BookingViewModel(
    private val reservaRepository: ReservaRepository, // para guardar la reserva
    private val servicioRepository: ServicioRepository, // para cargar los servicios
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState

    init {
        cargarServicios() // cargamos los servicios de la base
        cargarTrabajadores() // cargamos los trabajadores
    }


    //carga todos los servicios disponibles
    private fun cargarServicios() {
        viewModelScope.launch {

            val result = servicioRepository.obtenerTodosServicios()
            result.onSuccess { lista ->

                _uiState.update { it.copy(serviciosDisponibles = lista) }
            }.onFailure { e ->

                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun onNombreChange(value: String) = _uiState.update { it.copy(nombre = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onServicioChange(nombre: String, id: Long) = _uiState.update { it.copy(servicio = nombre, servicioId = id) }
    fun onFechaChange(value: String) = _uiState.update { it.copy(fecha = value) }
    fun onHoraChange(value: String) = _uiState.update { it.copy(hora = value) }

    fun onWorkerSelected(id: Long) { _uiState.update { it.copy(workerIdSeleccionado = id) } }



    fun registrarReserva(userId: Long) {
        viewModelScope.launch {
            val s = _uiState.value

            // ✔ Validación de campos vacíos
            if (s.nombre.isBlank() || s.email.isBlank() || s.servicio.isBlank() ||
                s.fecha.isBlank() || s.hora.isBlank()
            ) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Completa todos los campos",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (s.workerIdSeleccionado == null) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Debe seleccionar un trabajador",
                        successMessage = null
                    )
                }
                return@launch
            }

            try {
                // VALIDACIÓN DE RESERVA DUPLICADA
                val existeDuplicada = reservaRepository.existeDuplicada(
                    fecha = s.fecha,
                    hora = s.hora,
                    servicioId = s.servicioId ?: 0L,
                    userId = userId
                )

                if (existeDuplicada.getOrDefault(false)) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Ya tienes una reserva para este servicio, fecha y hora.",
                            successMessage = null
                        )
                    }
                    return@launch
                }

                // Si pasa la validación entonces crear reserva

                val reserva = ReservaEntity(
                    fechaReserva = s.fecha,
                    horaReserva = s.hora,
                    subtotal = s.precioServicio ?: 0,
                    userId = userId,
                    estadoId = 1L,
                    servicioId = s.servicioId ?: 1L,
                    workerId = s.workerIdSeleccionado
                )

                reservaRepository.crearReserva(reserva)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Reserva agendada con éxito para ${s.fecha} a las ${s.hora} (${s.servicio}).\n" +
                                "Le llegará un correo cuando un trabajador tome su reserva.",
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al registrar la reserva: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }


    //Listar y cargar las reservas
    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
    //DATA CLASS para el listado de reservas
    data class ReservaUsuario(
        val id: Long,
        val fecha: String,
        val hora: String,
        val servicio: String,
        val estado: String
    )

    //Función para cargar reservas del usuario logueado
    fun cargarReservasUsuario(userId: Long) {
        viewModelScope.launch {
            try {
                val resultado = reservaRepository.obtenerReservasPorUsuario(userId)

                resultado.onSuccess { lista ->
                    val reservas = lista.map {
                        ReservaUsuario(
                            id = it.idReserva,
                            fecha = it.fechaReserva,
                            hora = it.horaReserva ?: "Sin hora",
                            servicio = it.nombreServicio,
                            estado = when (it.estadoId) {
                                1L -> "Pendiente"
                                2L -> "Confirmada"
                                3L -> "Completada"
                                else -> "Desconocido"
                            }
                        )
                    }

                    _uiState.update {
                        it.copy(
                            reservaUsuario = reservas,
                            successMessage = null,
                            errorMessage = null
                        )
                    }
                }.onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar reservas: ${e.message}") }
            }
        }
    }
    //funcion para recargar servicios
    fun recargarServicios() {
        viewModelScope.launch {
            try {
                val result = servicioRepository.obtenerTodosServicios()
                result.onSuccess { lista ->
                    _uiState.update { it.copy(serviciosDisponibles = lista) }
                }.onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al recargar servicios: ${e.message}") }
            }
        }
    }
    fun cargarTrabajadores() {
        viewModelScope.launch {
            val result = userRepository.getAllWorkers(3L) // 3L = Rol Trabajador
            result.onSuccess { lista ->
                _uiState.update { it.copy(trabajadores = lista) }
            }
        }
    }



}
