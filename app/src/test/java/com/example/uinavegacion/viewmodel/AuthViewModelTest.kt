package com.example.uinavegacion.viewmodel

import android.util.Patterns
import app.cash.turbine.test
import com.example.uinavegacion.MainDispatcherRule
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO
import com.example.uinavegacion.data.remote.authservice.dto.UserAuthDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.repository.AuthRepository
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.regex.Pattern

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Repos/Prefs necesarios
    private val repoUser = mockk<UserRepository>()
    private val repoAPI = mockk<UserRepositoryTestAPI>()
    private val repoAuth = mockk<AuthRepository>()
    private val prefs = mockk<UserPreferences>(relaxed = true)

    // VM real a testear
    private lateinit var vm: AuthViewModel

    @Before
    fun setup() {
        mockkStatic(Patterns::class)
        every { Patterns.EMAIL_ADDRESS } returns Pattern.compile(".*@.*\\..*")

        vm = AuthViewModel(
            repository = repoUser,
            userPrefs = prefs,
            authRepository = repoAuth,
            repositoryTestAPI = repoAPI
        )
    }

    // ============================================================
    // 1️⃣ TEST: Validación simple de login
    // ============================================================
    @Test
    fun login_campos_validos_habilitan_canSubmit() = runTest {

        vm.onLoginEmailChange("test@mail.com")
        vm.onLoginContraChange("Demo123!")

        val state = vm.login.value

        assertEquals("test@mail.com", state.email)
        assertEquals("Demo123!", state.contra)
        assertTrue(state.canSubmit)
        assertNull(state.emailError)
    }

    // ============================================================
    // 2️⃣ TEST: submitLoginAPI (login por API externa)
    // ============================================================
    @Test
    fun submitLoginAPI_exitoso_actualiza_sesion_y_estado() = runTest {

        val backendUser = UserAuthDTO(
            idUser = 1,
            nombre = "Josefa",
            apellido = "Gomez",
            correo = "test@mail.com",
            rolId = 1
        )

        coEvery { repoAuth.login("test@mail.com", "Demo123!") } returns
                Result.success(
                    AuthLoginResponseDTO(
                        success = true,
                        message = "Login correcto",
                        user = backendUser
                    )
                )

        // Guardado de sesión
        coEvery { prefs.saveLoginState(true, 1, 1) } returns Unit

        vm.onLoginEmailChange("test@mail.com")
        vm.onLoginContraChange("Demo123!")

        vm.submitLoginAPI()
        advanceUntilIdle()

        val loginState = vm.login.value
        val session = vm.session.value

        assertTrue(loginState.success)
        assertEquals(1L, session.userId)
        assertEquals("Josefa", session.userName)
        assertEquals("Gomez", session.userLastName)
        assertEquals("test@mail.com", session.userEmail)
        assertEquals(1L, session.userRoleId)
        assertTrue(session.isLoggedIn)
    }

    // ============================================================
    // 3️⃣ TEST: submitRegisterAPI exitoso
    // ============================================================
    @Test
    fun submitRegisterAPI_exitoso_actualiza_estado() = runTest {

        coEvery {
            repoAPI.register(
                nombre = "Josefa",
                apellido = "Gomez",
                correo = "test@mail.com",
                phone = "987654321",
                password = "Demo123!",
                rolId = 1L
            )
        } returns Result.success(
            UserDTO(
                idUser = 10,
                nombre = "Josefa",
                apellido = "Gomez",
                correo = "test@mail.com",
                phone = "987654321",
                rolId = 1
            )
        )

        vm.onNombreChange("Josefa")
        vm.onApellidoChange("Gomez")
        vm.onEmailChange("test@mail.com")
        vm.onCelChange("987654321")
        vm.onContraChange("Demo123!")
        vm.onConfirmChange("Demo123!")

        assertTrue(vm.register.value.canSubmit)

        vm.submitRegisterAPI()
        advanceUntilIdle()

        val state = vm.register.value

        assertTrue(state.success)
        assertNull(state.errorMsg)
        assertFalse(state.isSubmitting)
    }
}
