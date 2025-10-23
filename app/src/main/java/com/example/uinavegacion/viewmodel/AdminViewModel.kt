package com.example.uinavegacion.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.database.AppDatabase
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity
import com.example.uinavegacion.data.local.entities.rol.RolEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.repository.CategoriaRepository
import com.example.uinavegacion.data.repository.RolRepository
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val servicios: List<ServicioEntity> = emptyList(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val roles: List<RolEntity> = emptyList(),
    val trbajadores: List<UserEntity> = emptyList()
)

class AdminViewModel(
    private val servicioRepository: ServicioRepository,
    private val categoriaRepository: CategoriaRepository,
    private val rolRepository: RolRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        cargarListas()
    }

    private fun cargarListas() {
        viewModelScope.launch {
            try {
                val servicios = servicioRepository.obtenerTodosServicios().getOrDefault(emptyList())
                val categorias = categoriaRepository.obtenerTodasCategorias().getOrDefault(emptyList())
                val roles = rolRepository.obtenerTodosRoles().getOrDefault(emptyList())
                val trabajadores = userRepository.getAllWorkers(3L).getOrDefault(emptyList())

                _uiState.update {
                    it.copy(servicios = servicios, categorias = categorias, roles = roles,)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar datos: ${e.message}") }
            }
        }
    }

    fun crearServicio(nombre: String, descripcion: String, precio: Int, categoriaId: Long) {
        viewModelScope.launch {
            val result = servicioRepository.insertarServicio(
                ServicioEntity(nombre = nombre, descripcion = descripcion, precio = precio, categoriaId = categoriaId)
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Servicio creado con éxito") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al crear servicio") }
            }
        }
    }

    fun crearCategoria(nombre: String) {
        viewModelScope.launch {
            val result = categoriaRepository.insertarCategoria(CategoriaEntity(nombreCategoria = nombre))
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Categoría creada correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al crear categoría") }
            }
        }
    }

    fun crearRol(descripcion: String) {
        viewModelScope.launch {
            val result = rolRepository.insertarRol(RolEntity(descripcion = descripcion))
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Rol creado correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al crear rol") }
            }
        }
    }

    fun crearTrabajador(
        nombre: String,
        apellido: String,
        correo: String,
        telefono: String,
        contrasena: String
    ) {
        viewModelScope.launch {
            try {
                val nuevo = UserEntity(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    phone = telefono,
                    pass = contrasena,
                    rolId = 3L,
                    estadoId = 1L,
                    categoriaId= 1L
                    // rol trabajador
                )
                val result = userRepository.agregarTrabajador(
                    nuevo

                )


                if (result.isSuccess) {
                    _uiState.update { it.copy(successMessage = "Trabajador creado correctamente") }
                    cargarListas()
                } else {
                    _uiState.update { it.copy(errorMessage = "Error al crear trabajador") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error: ${e.message}") }
            }
        }
    }
}
