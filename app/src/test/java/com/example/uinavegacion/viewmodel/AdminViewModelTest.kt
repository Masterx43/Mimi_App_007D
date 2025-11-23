package com.example.uinavegacion.viewmodel

import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.CategoriaRepositoryAPI
import com.example.uinavegacion.data.repository.RolRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepositoryAPI
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val servicioRepo = mockk<ServicioRepositoryAPI>()
    private val categoriaRepo = mockk<CategoriaRepositoryAPI>()
    private val rolRepo = mockk<RolRepositoryAPI>()
    private val userRepo = mockk<UserRepositoryAPI>()

    private lateinit var vm: AdminViewModel

    @Before
    fun setup() {
        // mocks iniciales usados en init { cargarListas() }
        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(emptyList())
        coEvery { categoriaRepo.obtenerTodasCategorias() } returns Result.success(emptyList())
        coEvery { rolRepo.obtenerTodosRoles() } returns Result.success(emptyList())
        coEvery { userRepo.getWorkers() } returns Result.success(emptyList())

        vm = AdminViewModel(servicioRepo, categoriaRepo, rolRepo, userRepo)
    }

    // ------------------------------------------------------------------
    @Test
    fun `cargarListas carga datos correctamente`() = runTest {

        val servicios = listOf(ServicioDTO(1, "Manicure", "desc", 10000, 1))
        val categorias = listOf(CategoriaDTO(1, "unias francesas", "Pintado de unias sutil estilo francesas"))
        val roles = listOf(RolDTO(1, "Admin"))
        val workers = listOf(UserDTO(1, "Ana", "Lopez", "ana@mail.com", "1234", 3))

        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(servicios)
        coEvery { categoriaRepo.obtenerTodasCategorias() } returns Result.success(categorias)
        coEvery { rolRepo.obtenerTodosRoles() } returns Result.success(roles)
        coEvery { userRepo.getWorkers() } returns Result.success(workers)

        vm = AdminViewModel(servicioRepo, categoriaRepo, rolRepo, userRepo)
        advanceUntilIdle()

        val state = vm.uiState.value

        assertEquals(1, state.servicios.size)
        assertEquals(1, state.categorias.size)
        assertEquals(1, state.roles.size)
        assertEquals(1, state.trabajadores.size)
    }

    // ------------------------------------------------------------------
    @Test
    fun `crearServicio exitoso actualiza successMessage`() = runTest {

        coEvery { servicioRepo.crearServicio(any()) } returns Result.success(
            ServicioDTO(1, "Manicure", "desc", 10000, 1)
        )
        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(emptyList())

        vm.crearServicio("Manicure", "desc", 10000, 1)
        advanceUntilIdle()

        assertEquals("Servicio creado con éxito", vm.uiState.value.successMessage)
    }

    // ------------------------------------------------------------------
    @Test
    fun `crearServicio con campos vacios muestra error`() = runTest {

        vm.crearServicio("", "", 0, 1)
        advanceUntilIdle()

        assertEquals("Nombre y descripción son obligatorios", vm.uiState.value.errorMessage)
    }

    // ------------------------------------------------------------------
    @Test
    fun `actualizarServicio exitoso actualiza mensaje`() = runTest {

        coEvery { servicioRepo.actualizarServicio(1, any()) } returns Result.success(
            ServicioDTO(1, "Nuevo", "desc", 12000, 1)
        )
        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(emptyList())

        vm.actualizarServicio(1, "Nuevo", "desc", 12000)
        advanceUntilIdle()

        assertEquals("Servicio actualizado correctamente", vm.uiState.value.successMessage)
        assertNull(vm.uiState.value.servicioAEditar)
    }

    // ------------------------------------------------------------------
    @Test
    fun `eliminarServicio exitoso muestra mensaje`() = runTest {

        coEvery { servicioRepo.eliminarServicio(1) } returns Result.success(Unit)
        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(emptyList())

        vm.eliminarServicio(1)
        advanceUntilIdle()

        assertEquals("Servicio eliminado correctamente", vm.uiState.value.successMessage)
    }

    // ------------------------------------------------------------------
    @Test
    fun `crearCategoria funciona correctamente`() = runTest {

        coEvery { categoriaRepo.crearCategoria(any()) } returns Result.success(
            CategoriaDTO(1, "Pedicure","pintado de unas de pies")
        )
        coEvery { categoriaRepo.obtenerTodasCategorias() } returns Result.success(emptyList())

        vm.crearCategoria("Pedicure")
        advanceUntilIdle()

        assertEquals("Categoría creada correctamente", vm.uiState.value.successMessage)
    }

    // ------------------------------------------------------------------
    @Test
    fun `crearRol actualiza estado correctamente`() = runTest {

        coEvery { rolRepo.insertarRol("Admin") } returns Result.success(
            RolDTO(1, "Admin")
        )
        coEvery { rolRepo.obtenerTodosRoles() } returns Result.success(emptyList())

        vm.crearRol("Admin")
        advanceUntilIdle()

        assertEquals("Rol creado correctamente", vm.uiState.value.successMessage)
    }

    // ------------------------------------------------------------------
    @Test
    fun `crearTrabajador exitoso actualiza successMessage`() = runTest {

        coEvery { userRepo.register(any(), any(), any(), any(), any(), any()) } returns
                Result.success(
                    UserDTO(5, "Juan", "Perez", "jp@mail.com", "123", 3)
                )
        coEvery { userRepo.getWorkers() } returns Result.success(emptyList())

        vm.crearTrabajador("Juan", "Perez", "jp@mail.com", "123", "demo")
        advanceUntilIdle()

        assertEquals("Trabajador creado", vm.uiState.value.successMessage)
    }
}
