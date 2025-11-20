package com.example.uinavegacion.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioDao
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.remote.reservas.dto.CrearReservaRequestDTO
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.ReservaRepository
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
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
    val serviciosDisponibles: List<ServicioDTO> = emptyList(),
    val reservaUsuario: List<BookingViewModel.ReservaUsuario> = emptyList(),
    val trabajadores: List<UserDTO> = emptyList(),
    val workerIdSeleccionado: Long?= null

)

class BookingViewModel(
    private val reservaRepository: ReservaRepository, // para guardar la reserva
    private val servicioRepository: ServicioRepository, // para cargar los servicios
    private val userRepository: UserRepository,
    private val reservaRepository2: ReservaRepositoryAPI,
    private val servicioRepository2: ServicioRepositoryAPI,
    private val userRepository2: UserRepositoryTestAPI
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
            val result = servicioRepository2.obtenerTodosServicios()

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

            if (
                s.fecha.isBlank() ||
                s.hora.isBlank() ||
                s.servicioId == null ||
                s.workerIdSeleccionado == null
            ) {
                _uiState.update { it.copy(errorMessage = "Completa todos los campos") }
                return@launch
            }

            val req = CrearReservaRequestDTO(
                idUsuario = userId,
                idServicio = s.servicioId,
                idTrabajador = s.workerIdSeleccionado,
                fecha = s.fecha,
                hora = s.hora
            )

            val result = reservaRepository2.crearReserva(req)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        successMessage = "Reserva creada con éxito",
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
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
            val result = reservaRepository2.obtenerReservasUsuario(userId)

            result.onSuccess { lista ->

                val reservas = lista.map {
                    ReservaUsuario(
                        id = it.idReserva,
                        fecha = it.fecha,
                        hora = it.hora,
                        servicio = "Servicio #${it.idServicio}", // opcional
                        estado = it.estado
                    )
                }

                _uiState.update { it.copy(reservaUsuario = reservas) }

            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    //funcion para recargar servicios
    fun recargarServicios() {
        viewModelScope.launch {
            try {
                val result = servicioRepository2.obtenerTodosServicios()
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
            val result = userRepository2.getWorkers()

            result.onSuccess { lista ->
                _uiState.update { it.copy(trabajadores = lista) }
            }.onFailure {
                _uiState.update { it.copy(errorMessage = "Error al cargar los trabajadores") }
            }
        }
    }




}
