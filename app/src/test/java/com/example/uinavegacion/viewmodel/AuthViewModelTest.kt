package com.example.uinavegacion.viewmodel

import app.cash.turbine.test
import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO
import com.example.uinavegacion.data.remote.authservice.dto.UserAuthDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.AuthRepository
import com.example.uinavegacion.data.repository.UserRepositoryAPI
import com.example.uinavegacion.fakes.FakeUserPreferences
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AuthViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakePrefs: FakeUserPreferences
    private val authRepo = mockk<AuthRepository>()
    private val userApiRepo = mockk<UserRepositoryAPI>()
    private lateinit var vm: AuthViewModel

    @Before
    fun setup() {
        fakePrefs = FakeUserPreferences()
        vm = AuthViewModel(
            userPrefs = fakePrefs,
            authRepository = authRepo,
            repositoryTestAPI = userApiRepo
        )
    }

    @Test
    fun submitLoginAPI_actualiza_session_login_success() = runTest {

        // Datos de prueba
        val fakeUser = UserAuthDTO(
            idUser = 10L,
            nombre = "Bastian",
            apellido = "Gomez",
            correo = "bas@test.com",
            rolId = 1L
        )

        val fakeResponse = AuthLoginResponseDTO(
            success = true,
            message = "OK",
            user = fakeUser
        )

        // Mock principal del login API
        coEvery { authRepo.login("bas@test.com", "1234") } returns Result.success(fakeResponse)

        // Mock necesario por culpa del init { ... }
        coEvery { userApiRepo.getUserById(any()) } returns Result.success(
            UserDTO(
                idUser = 10L,
                nombre = "Bastian",
                apellido = "Gomez",
                correo = "bas@test.com",
                phone = "99999999",
                rolId = 1L
            )
        )

        vm.onLoginEmailChange("bas@test.com")
        vm.onLoginContraChange("1234")

        vm.session.test {
            vm.submitLoginAPI()

            // estado 0 (inicial)
            awaitItem()

            // estado 1: login isSubmitting = true
            awaitItem()

            // estado 2: login completado
            val finalState = awaitItem()
            assertTrue(finalState.isLoggedIn)
            assertEquals(10L, finalState.userId)
            assertEquals("Bastian", finalState.userName)
        }
    }


    @Test
    fun submitRegisterAPI_registra_exitosamente() = runTest {

        // Mock respuesta del API
        val fakeUser = UserDTO(
            idUser = 100,
            nombre = "Bastian",
            apellido = "Gomez",
            correo = "bastian@mail.com",
            phone = "123123123",
            rolId = 1
        )

        coEvery {
            userApiRepo.register(
                nombre = any(),
                apellido = any(),
                correo = any(),
                phone = any(),
                password = any(),
                rolId = 1L
            )
        } returns Result.success(fakeUser)


        vm.onNombreChange("Bastian")
        vm.onApellidoChange("Gomez")
        vm.onEmailChange("bastian@mail.com")
        vm.onCelChange("987654321")
        vm.onContraChange("Pass1234!")
        vm.onConfirmChange("Pass1234!")

        vm.register.test {

            // ejecutar registro
            vm.submitRegisterAPI()

            // estado 0: inicial
            awaitItem()

            // estado 1: isSubmitting = true
            awaitItem()

            // estado 2: success = true
            val final = awaitItem()
            assertTrue(final.success)
            assertNull(final.errorMsg)
        }
    }


    @Test
    fun init_restaura_sesion_userPrefs() = runTest {

        // Usuario que debería cargar el init()
        val fakeUser = UserDTO(
            idUser = 10,
            nombre = "Bastian",
            apellido = "Gomez",
            correo = "test@mail.com",
            phone = "123456789",
            rolId = 1
        )



        coEvery { userApiRepo.getUserById(10L) } returns Result.success(fakeUser)

        fakePrefs.emitLoggedIn(true)
        fakePrefs.emitUserId(10L)

        vm.session.test {

            // estado inicial
            awaitItem()

            // estado restaurado
            val restored = awaitItem()

            assertEquals(true, restored.isLoggedIn)
            assertEquals(10L, restored.userId)
            assertEquals("Bastian", restored.userName)
            assertEquals("Gomez", restored.userLastName)
            assertEquals("test@mail.com", restored.userEmail)
            assertEquals(1L, restored.userRoleId)
        }
    }

    @Test
    fun logout_limpia_preferencias_session() = runTest {

        coEvery { userApiRepo.getUserById(any()) } returns Result.success(
            UserDTO(0, "", "", "", "", 1)
        )

        fakePrefs.saveLoginState(true, 2, 88)

        vm.logout()
        advanceUntilIdle()
        assertFalse(vm.session.value.isLoggedIn)
        assertNull(vm.session.value.userId)
        assertNull(fakePrefs.userId.value)
        assertFalse(fakePrefs.isLoggedIn.value)
    }
}
