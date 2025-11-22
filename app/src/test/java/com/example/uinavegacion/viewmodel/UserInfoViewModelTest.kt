package com.example.uinavegacion.viewmodel

import app.cash.turbine.test
import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserInfoViewModelTest {

    // Rela obligatoria para test de viewmodel
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val api = mockk<UserRepositoryTestAPI>()
    private val vm = UserInfoViewModel(api)

    @Test
    fun cargarUsuario_devuelve_usuario() = runTest {

        // SAMPLE DE LA API
        val sample = UserDTO(
            idUser = 1,
            nombre = "Josefa",
            apellido = "Gomez",
            correo = "test@mail.com",
            phone = "999999",
            rolId = 1
        )

        // MOCK
        coEvery { api.getUserById(1) } returns Result.success(sample)

        // TEST DEL STATEFLOW
        vm.uiState.test {

            vm.cargarUsuario(1)

            // 1) Estado inicial
            val initial = awaitItem()
            assertNull(initial.user)
            assertFalse(initial.loading)

            // 2) Loading true
            val loading = awaitItem()
            assertTrue(loading.loading)

            // 3) Usuario cargado
            val final = awaitItem()
            assertEquals("Josefa", final.user?.nombre)
            assertFalse(final.loading)
        }
    }
}
