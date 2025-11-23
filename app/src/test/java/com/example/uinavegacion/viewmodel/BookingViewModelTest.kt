package com.example.uinavegacion.viewmodel

import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.reservas.dto.ReservaResponseDTO
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
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
class BookingViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val reservaRepo = mockk<ReservaRepositoryAPI>()
    private val servicioRepo = mockk<ServicioRepositoryAPI>()
    private val userRepo = mockk<UserRepositoryAPI>()

    private lateinit var vm: BookingViewModel

    @Before
    fun setup() {
        // Mock del init { cargarServicios() y cargarTrabajadores() }
        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(emptyList())
        coEvery { userRepo.getWorkers() } returns Result.success(emptyList())

        vm = BookingViewModel(reservaRepo, servicioRepo, userRepo)
    }


    @Test
    fun cargarServicios() = runTest {

        val servicios = listOf(
            ServicioDTO(1, "Manicure", "desc", 10000, 1)
        )

        coEvery { servicioRepo.obtenerTodosServicios() } returns Result.success(servicios)

        vm.recargarServicios()
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.serviciosDisponibles.size)
        assertEquals("Manicure", vm.uiState.value.serviciosDisponibles[0].nombre)
    }


    @Test
    fun registrarReserva_campos_incompletos_error() = runTest {

        vm.registrarReserva(1)
        advanceUntilIdle()

        assertEquals("Completa todos los campos", vm.uiState.value.errorMessage)
    }


    @Test
    fun registrarReserva_exitosa() = runTest {

        // completar datos
        vm.onFechaChange("2025-02-01")
        vm.onHoraChange("10:00")
        vm.onServicioChange("Manicure", 1)
        vm.onWorkerSelected(3)

        // mock respuesta API
        coEvery { reservaRepo.crearReserva(any()) } returns
                Result.success(
                    ReservaResponseDTO(
                        idReserva = 99,
                        idUsuario = 50,
                        idServicio = 1,
                        idTrabajador = 3,
                        fecha = "2025-02-01",
                        hora = "10:00",
                        estado = "pendiente"
                    )
                )

        vm.registrarReserva(50)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals("Reserva creada con éxito", state.successMessage)
        assertNull(state.errorMessage)
    }


    @Test
    fun cargarTrabajadores_lista() = runTest {

        val workers = listOf(
            UserDTO(1, "Maria", "Lopez", "maria@mail.com", "98765", 3)
        )

        coEvery { userRepo.getWorkers() } returns Result.success(workers)

        vm.cargarTrabajadores()
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.trabajadores.size)
        assertEquals("Maria", vm.uiState.value.trabajadores[0].nombre)
    }
}
