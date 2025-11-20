package com.example.uinavegacion.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.database.AppDatabase
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity
import com.example.uinavegacion.data.local.entities.rol.RolEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.local.entities.user.UserEntity
import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.categoria.dto.CrearCategoriaRequest
import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import com.example.uinavegacion.data.remote.servicioservice.dto.CrearServicioRequest
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.CategoriaRepository
import com.example.uinavegacion.data.repository.CategoriaRepositoryAPI
import com.example.uinavegacion.data.repository.RolRepository
import com.example.uinavegacion.data.repository.RolRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val servicios: List<ServicioDTO> = emptyList(),
    val categorias: List<CategoriaDTO> = emptyList(),
    val roles: List<RolDTO> = emptyList(),
    val trabajadores: List<UserDTO> = emptyList(),

    val servicioAEditar: ServicioDTO? = null,
    val categoriaAEditar: CategoriaDTO? = null,
    val rolAEditar: RolDTO? = null,
)


class AdminViewModel(
    private val servicioRepository: ServicioRepositoryAPI,
    private val categoriaRepository: CategoriaRepositoryAPI,
    private val rolRepository: RolRepositoryAPI,
    private val userRepository: UserRepositoryTestAPI
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
                val trabajadores = userRepository.getWorkers().getOrDefault(emptyList())

                _uiState.update {
                    it.copy(
                        servicios = servicios,
                        categorias = categorias,
                        roles = roles,
                        trabajadores = trabajadores
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar datos: ${e.message}") }
            }
        }
    }

    // ---------------------------------------------------------
    // SERVICIOS
    // ---------------------------------------------------------
    fun crearServicio(nombre: String, descripcion: String, precio: Int, categoriaId: Long) {
        viewModelScope.launch {

            if (nombre.isBlank() || descripcion.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Nombre y descripción son obligatorios") }
                return@launch
            }

            if (precio < 5000 || precio > 200000) {
                _uiState.update { it.copy(errorMessage = "El precio debe estar entre 5.000 y 200.000") }
                return@launch
            }

            val req = CrearServicioRequest(nombre, descripcion, precio, categoriaId)

            val result = servicioRepository.crearServicio(req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Servicio creado con éxito") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun actualizarServicio(id: Long, nombre: String, desc: String, precio: Int) {
        viewModelScope.launch {

            if (nombre.isBlank() || desc.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Nombre y descripción no pueden estar vacíos") }
                return@launch
            }

            if (precio < 5000 || precio > 200000) {
                _uiState.update { it.copy(errorMessage = "Precio fuera de rango") }
                return@launch
            }

            val req = CrearServicioRequest(nombre, desc, precio, 1L)

            val result = servicioRepository.actualizarServicio(id, req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Servicio actualizado correctamente") }
                cerrarDialogoEditarServicio()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun eliminarServicio(id: Long) {
        viewModelScope.launch {
            val result = servicioRepository.eliminarServicio(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Servicio eliminado correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun abrirDialogoEditarServicio(s: ServicioDTO) {
        _uiState.update { it.copy(servicioAEditar = s) }
    }

    fun cerrarDialogoEditarServicio() {
        _uiState.update { it.copy(servicioAEditar = null) }
    }

    // ---------------------------------------------------------
    // CATEGORÍAS
    // ---------------------------------------------------------
    fun crearCategoria(nombre: String) {
        viewModelScope.launch {
            val req = CrearCategoriaRequest(nombre)
            val result = categoriaRepository.crearCategoria(req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Categoría creada correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al crear categoría") }
            }
        }
    }

    fun actualizarCategoria(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val req = CrearCategoriaRequest(nuevoNombre)
            val result = categoriaRepository.actualizarCategoria(id, req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Categoría actualizada") }
                cerrarDialogoEditarCategoria()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al actualizar categoría") }
            }
        }
    }

    fun eliminarCategoria(id: Long) {
        viewModelScope.launch {
            val result = categoriaRepository.eliminarCategoria(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Categoría eliminada") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al eliminar categoría") }
            }
        }
    }

    fun abrirDialogoEditarCategoria(c: CategoriaDTO) {
        _uiState.update { it.copy(categoriaAEditar = c) }
    }

    fun cerrarDialogoEditarCategoria() {
        _uiState.update { it.copy(categoriaAEditar = null) }
    }

    // ---------------------------------------------------------
    // ROLES
    // ---------------------------------------------------------
    fun crearRol(nombre: String) {
        viewModelScope.launch {
            val result = rolRepository.insertarRol(nombre)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Rol creado correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al crear rol") }
            }
        }
    }

    fun actualizarRol(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val result = rolRepository.actualizarRol(id, nuevoNombre)

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Rol actualizado correctamente") }
                cerrarDialogoEditarRol()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al actualizar rol") }
            }
        }
    }

    fun eliminarRol(id: Long) {
        viewModelScope.launch {
            val result = rolRepository.eliminarRol(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Rol eliminado correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = "Error al eliminar rol") }
            }
        }
    }

    fun abrirDialogoEditarRol(r: RolDTO) {
        _uiState.update { it.copy(rolAEditar = r) }
    }

    fun cerrarDialogoEditarRol() {
        _uiState.update { it.copy(rolAEditar = null) }
    }

    // ---------------------------------------------------------
    // TRABAJADORES (UserService)
    // ---------------------------------------------------------
    fun crearTrabajador(nombre: String, apellido: String, correo: String, phone: String, pass: String) {
        viewModelScope.launch {

            val result = userRepository.register(
                nombre, apellido, correo, phone, pass, rolId = 3L
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Trabajador creado") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }
}

