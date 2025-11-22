package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.authservice.AuthServiceAPI
import com.example.uinavegacion.data.remote.authservice.RmAuth
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginRequestDTO
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO
import com.example.uinavegacion.data.remote.authservice.dto.UserAuthDTO
import com.example.uinavegacion.data.remote.userservice.UserServiceAPI
import com.example.uinavegacion.data.remote.userservice.dto.RegisterRequestDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    @Test
    fun login_devuelve_exito() = runBlocking {
        // 1. Mockear objeto estático RmAuth
        mockkObject(RmAuth)

        // 2. Crear API falsa
        val apiMock = mockk<AuthServiceAPI>()

        // 3. RmAuth.api devolverá API falsa
        every { RmAuth.api } returns apiMock

        // 4. Respuesta simulada del backend
        val sample = AuthLoginResponseDTO(
            success = true,
            message = "Login correcto",
            user = UserAuthDTO(
                idUser = 1,
                nombre = "Josefa",
                apellido = "Nunez",
                correo = "test@mail.com",
                rolId = 1
            )
        )

        // 5. Simulación de llamada login()
        coEvery { apiMock.login(any()) } returns Response.success(sample)

        // 6. Probar el repositorio REAL
        val repo = AuthRepository()
        val result = repo.login("test@mail.com", "1234")

        // Validaciones
        assertTrue(result.isSuccess)
        assertEquals("Josefa", result.getOrNull()!!.user!!.nombre)
        assertEquals(true, result.getOrNull()!!.success)
    }

}
