package com.example.uinavegacion.viewmodel

import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaResponseDTO
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WorkerViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val reservaRepo = mockk<ReservaRepositoryAPI>()
    private lateinit var vm: WorkerViewModel

    @Before
    fun setup() {
        vm = WorkerViewModel(reservaRepo)
    }

    // ---------------------------------------------------------
    @Test
    fun `cargarTodasLasReservas carga lista correctamente`() = runTest {

        val reservas = listOf(
            ReservaDetalleDTO(
                idReserva = 1,
                fecha = "2025-01-10",
                hora = "10:00",
                estado = "PENDIENTE",
                usuario = "Ana",
                trabajador = "Carlos",
                servicio = "Manicure"
            )
        )

        coEvery { reservaRepo.obtenerReservasDetalleTrabajador(5) }
            .returns(Result.success(reservas))

        vm.cargarTodasLasReservas(5)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(1, state.reservas.size)
        assertEquals("Ana", state.reservas[0].usuario)
        assertNull(state.errorMessage)
    }

    // ---------------------------------------------------------
    @Test
    fun `cargarTodasLasReservas con error actualiza errorMessage`() = runTest {

        coEvery { reservaRepo.obtenerReservasDetalleTrabajador(5) }
            .returns(Result.failure(Exception("Fallo servidor")))

        vm.cargarTodasLasReservas(5)
        advanceUntilIdle()

        assertEquals("Fallo servidor", vm.uiState.value.errorMessage)
    }

    // ---------------------------------------------------------
    @Test
    fun `marcarCompletada actualiza successMessage y recarga lista`() = runTest {

        // RESERVA COMPLETADA:
        val response = ReservaResponseDTO(
            idReserva = 10,
            idUsuario = 2,
            idServicio = 5,
            idTrabajador = 3,
            fecha = "2025-03-01",
            hora = "11:00",
            estado = "CONFIRMADO"
        )

        coEvery { reservaRepo.actualizarEstado(10, "CONFIRMADO") }
            .returns(Result.success(response))

        // Lista recargada después
        val updated = listOf(
            ReservaDetalleDTO(
                idReserva = 10,
                fecha = "2025-03-01",
                hora = "11:00",
                estado = "CONFIRMADO",
                usuario = "Luis",
                trabajador = "Maria",
                servicio = "Depilación"
            )
        )

        coEvery { reservaRepo.obtenerReservasDetalleTrabajador(3) }
            .returns(Result.success(updated))

        vm.marcarCompletada(reservaId = 10, id = 3)
        advanceUntilIdle()

        val state = vm.uiState.value

        assertTrue(state.successMessage!!.contains("Reserva 10 marcada como completada"))
        assertEquals(1, state.reservas.size)
        assertEquals("CONFIRMADO", state.reservas[0].estado)
    }

    // ---------------------------------------------------------
    @Test
    fun `marcarCompletada con error actualiza errorMessage`() = runTest {

        coEvery { reservaRepo.actualizarEstado(99, "CONFIRMADO") }
            .returns(Result.failure(Exception("Imposible actualizar")))

        vm.marcarCompletada(99, 2)
        advanceUntilIdle()

        assertEquals("Error al actualizar: Imposible actualizar",
            vm.uiState.value.errorMessage)
    }

    // ---------------------------------------------------------
    @Test
    fun `clearMessages limpia successMessage y errorMessage`() {

        vm.apply {
            _uiState.update {
                it.copy(successMessage = "OK", errorMessage = "ERR")
            }
            clearMessages()
        }

        val state = vm.uiState.value
        assertNull(state.successMessage)
        assertNull(state.errorMessage)
    }
}
