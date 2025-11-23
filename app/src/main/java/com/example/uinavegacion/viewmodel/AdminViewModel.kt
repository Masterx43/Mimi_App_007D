package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.categoria.dto.CrearCategoriaRequest
import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import com.example.uinavegacion.data.remote.servicioservice.dto.CrearServicioRequest
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.CategoriaRepositoryAPI
import com.example.uinavegacion.data.repository.RolRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepositoryAPI
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

    val errorServicio: String? = null,
    val successServicio: String? = null,

    val errorCategoria: String? = null,
    val successCategoria: String? = null,

    val errorRol: String? = null,
    val successRol: String? = null,

    val errorTrabajador: String? = null,
    val successTrabajador: String? = null

)


class AdminViewModel(
    private val servicioRepository: ServicioRepositoryAPI,
    private val categoriaRepository: CategoriaRepositoryAPI,
    private val rolRepository: RolRepositoryAPI,
    private val userRepository: UserRepositoryAPI
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


    // SERVICIOS

    fun crearServicio(nombre: String, descripcion: String, precio: Int, categoriaId: Long) {
        viewModelScope.launch {

            if (nombre.isBlank() || descripcion.isBlank()) {
                _uiState.update { it.copy(
                    errorServicio = "Nombre y descripción son obligatorios",
                    successServicio = null) }
                return@launch
            }

            if (precio < 5000 || precio > 200000) {
                _uiState.update { it.copy(
                    errorServicio = "El precio debe estar entre 5.000 y 200.000",
                    successServicio = null) }
                return@launch
            }

            val req = CrearServicioRequest(nombre, descripcion, precio, categoriaId)

            val result = servicioRepository.crearServicio(req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successServicio = "Servicio creado con éxito",
                                            errorServicio = null) }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorServicio = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun actualizarServicio(id: Long, nombre: String, desc: String, precio: Int) {
        viewModelScope.launch {

            if (nombre.isBlank() || desc.isBlank()) {
                _uiState.update { it.copy(errorServicio = "Nombre y descripción no pueden estar vacíos") }
                return@launch
            }

            if (precio < 5000 || precio > 200000) {
                _uiState.update { it.copy(errorServicio = "Precio fuera de rango") }
                return@launch
            }

            val req = CrearServicioRequest(nombre, desc, precio, 1L)

            val result = servicioRepository.actualizarServicio(id, req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successServicio = "Servicio actualizado correctamente",
                                            errorServicio = null) }
                cerrarDialogoEditarServicio()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorServicio = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun eliminarServicio(id: Long) {
        viewModelScope.launch {
            val result = servicioRepository.eliminarServicio(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successServicio = "Servicio eliminado correctamente") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorServicio = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun abrirDialogoEditarServicio(s: ServicioDTO) {
        _uiState.update { it.copy(servicioAEditar = s) }
    }

    fun cerrarDialogoEditarServicio() {
        _uiState.update { it.copy(servicioAEditar = null) }
    }


    // CATEGORÍAS

    fun crearCategoria(nombre: String) {
        viewModelScope.launch {
            val req = CrearCategoriaRequest(nombre)
            val result = categoriaRepository.crearCategoria(req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successCategoria = "Categoría creada correctamente")
                                            }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorCategoria = "El nombre de la categoria es obligatorio ") }
            }
        }
    }

    fun actualizarCategoria(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val req = CrearCategoriaRequest(nuevoNombre)
            val result = categoriaRepository.actualizarCategoria(id, req)

            if (result.isSuccess) {
                _uiState.update { it.copy(successCategoria = "Categoría actualizada") }
                cerrarDialogoEditarCategoria()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorCategoria = "Error al actualizar categoría") }
            }
        }
    }

    fun eliminarCategoria(id: Long) {
        viewModelScope.launch {
            val result = categoriaRepository.eliminarCategoria(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successCategoria = "Categoría eliminada") }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorCategoria = "Error al eliminar categoría") }
            }
        }
    }

    fun abrirDialogoEditarCategoria(c: CategoriaDTO) {
        _uiState.update { it.copy(categoriaAEditar = c) }
    }

    fun cerrarDialogoEditarCategoria() {
        _uiState.update { it.copy(categoriaAEditar = null) }
    }


    // ROLES

    fun crearRol(nombre: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    errorRol = null,
                    successRol = null
                )
            }
            val nombreLimpio = nombre.trim()

            if (nombreLimpio.isEmpty()){
                _uiState.update{
                    it.copy(errorRol = "El nombre del rol es obligatorio")
                }
                return@launch
            }
            if (nombreLimpio.length < 3){
                _uiState.update {
                    it.copy(errorRol = "El nombre debe tener minimo 3 caracteres")
                }
                return@launch
            }
            val soloLetrasRegex= Regex("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+\$")
            if (!soloLetrasRegex.matches(nombreLimpio)){
                _uiState.update{
                    it.copy(errorRol = "El nombre solo puede contener letras")
                }
                return@launch
            }
            val result= rolRepository.insertarRol(nombreLimpio)

            if (result.isSuccess){
                _uiState.update {
                    it.copy(
                        successRol = "Rol creado correctamente",
                        errorRol = null
                    )
                }
                cargarListas()
            }else{
                _uiState.update {
                    it.copy(
                        errorRol = result.exceptionOrNull()?.message ?: "Error al crear rol",
                        successRol = null
                    )
                }
            }

        }
    }

    fun actualizarRol(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val result = rolRepository.actualizarRol(id, nuevoNombre)


            if (result.isSuccess) {
                _uiState.update { it.copy(successRol = "Rol actualizado correctamente",
                                            errorRol = null) }
                cerrarDialogoEditarRol()
                cargarListas()
            } else {
                _uiState.update { it.copy(errorRol = "Error al actualizar rol",
                                            successRol = null) }
            }
        }
    }

    fun eliminarRol(id: Long) {
        viewModelScope.launch {
            val result = rolRepository.eliminarRol(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(successRol = "Rol eliminado correctamente",
                                            errorRol = null) }
                cargarListas()
            } else {
                _uiState.update { it.copy(errorRol = "Error al eliminar rol",
                                            successRol = null) }
            }
        }
    }

    fun abrirDialogoEditarRol(r: RolDTO) {
        _uiState.update { it.copy(rolAEditar = r) }
    }

    fun cerrarDialogoEditarRol() {
        _uiState.update { it.copy(rolAEditar = null) }
    }


    // TRABAJADORES (UserService)

    fun crearTrabajador(nombre: String, apellido: String, correo: String, phone: String, pass: String) {
        viewModelScope.launch {

            _uiState.update{
                it.copy(
                    errorTrabajador = null,
                    successTrabajador = null
                )
            }

            if (nombre.isBlank()|| apellido.isBlank()|| correo.isBlank()||
                phone.isBlank()|| pass.isBlank()
                ){
                _uiState.update {
                    it.copy(errorTrabajador = "Todos los campos son obligatorios")
                }
                return@launch
            }
            val result = userRepository.register(
                nombre,apellido,correo,phone,pass, rolId = 3L
            )

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        successTrabajador = "Trabajador creado",
                        errorTrabajador = null) }

                cargarListas()
            } else {
                _uiState.update {
                    it.copy(
                        errorTrabajador = result.exceptionOrNull()?.message
                            ?:"Error desconocido",
                        successTrabajador = null) }
            }
        }
    }
}

