package com.example.uinavegacion.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.local.database.AppDatabase
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity
import com.example.uinavegacion.data.local.entities.rol.RolEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val servicios: List<ServicioEntity> = emptyList(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val roles: List<RolEntity> = emptyList()
)

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val servicioDao = db.servicioDao()
    private val categoriaDao = db.categoriaDao()
    private val rolDao = db.rolDao()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        cargarListas()
    }

    private fun cargarListas() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        servicios = servicioDao.getAllServicios(),
                        categorias = categoriaDao.getAllCategorias(),
                        roles = rolDao.getAllRols()
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar datos: ${e.message}") }
            }
        }
    }

    fun crearServicio(nombre: String, descripcion: String, precio: Int, categoriaId: Long) {
        viewModelScope.launch {
            try {
                servicioDao.insertServicio(
                    ServicioEntity(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio,
                        categoriaId = categoriaId
                    )
                )
                _uiState.update { it.copy(successMessage = "Servicio creado con éxito") }
                cargarListas()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al crear servicio: ${e.message}") }
            }
        }
    }

    fun crearCategoria(nombre: String) {
        viewModelScope.launch {
            try {
                categoriaDao.insert(CategoriaEntity(nombreCategoria = nombre))
                _uiState.update { it.copy(successMessage = "Categoría creada correctamente") }
                cargarListas()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al crear categoría: ${e.message}") }
            }
        }
    }

    fun crearRol(descripcion: String) {
        viewModelScope.launch {
            try {
                rolDao.insertRol(RolEntity(descripcion = descripcion))
                _uiState.update { it.copy(successMessage = "Rol creado correctamente") }
                cargarListas()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al crear rol: ${e.message}") }
            }
        }
    }
}
