package com.example.uinavegacion.viewmodel

import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
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
class HistorialViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val reservaRepo = mockk<ReservaRepositoryAPI>()
    private lateinit var vm: HistorialViewModel

    @Before
    fun setup() {
        vm = HistorialViewModel(reservaRepo)
    }


    @Test
    fun cargarHistorialUsuario_reservas() = runTest {

        val lista = listOf(
            ReservaDetalleDTO(
                idReserva = 1,
                fecha = "2025-02-15",
                hora = "11:00",
                estado = "pendiente",
                usuario = "Wladimir",
                trabajador = "Ana",
                servicio = "Manicure"
            )
        )

        coEvery { reservaRepo.obtenerReservasDetalleUsuario(10) } returns Result.success(lista)

        vm.cargarHistorialUsuario(10)
        advanceUntilIdle()

        val state = vm.uiState.value

        assertEquals(1, state.reservas.size)
        assertEquals("Manicure", state.reservas[0].servicio)
        assertFalse(state.loading)
        assertNull(state.errorMessage)
    }


    @Test
    fun cargarHistorialUsuario_error() = runTest {

        coEvery { reservaRepo.obtenerReservasDetalleUsuario(10) } returns
                Result.failure(Exception("Error de conexión"))

        vm.cargarHistorialUsuario(10)
        advanceUntilIdle()

        val state = vm.uiState.value

        assertTrue(state.errorMessage!!.contains("Error"))
        assertFalse(state.loading)
        assertTrue(state.reservas.isEmpty())
    }
}
