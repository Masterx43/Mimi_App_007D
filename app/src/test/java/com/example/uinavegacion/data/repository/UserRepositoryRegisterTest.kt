package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.userservice.UserServiceAPI
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserUpdateRequestDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class UserRepositoryRegisterTest {

    @Test
    fun register_devuelve_exito() = runBlocking {

        // Mockear API real
        val api = mockk<UserServiceAPI>()
        val repo = UserRepositoryAPI(api)

        //Respuesta simulada de la API (lo que devuelve tu backend)
        val sampleResponse = UserDTO(
            idUser = 99,
            nombre = "Josefa",
            apellido = "Test",
            correo = "test@mail.com",
            phone = "999999",
            rolId = 1
        )

        // Mockear llamada
        coEvery { api.register(any()) } returns Response.success(sampleResponse)

        // Ejecutar función real
        val result = repo.register(
            nombre = "Josefa",
            apellido = "Test",
            correo = "test@mail.com",
            phone = "999999",
            password = "1234",
            rolId = 1
        )

        // 5. Validaciones
        assertTrue(result.isSuccess)
        assertEquals("Josefa", result.getOrNull()!!.nombre)
        assertEquals(99L, result.getOrNull()!!.idUser)
    }
    @Test
    fun getUserById_devuelve_usuario() = runBlocking {
        val api = mockk<UserServiceAPI>()
        val repo = UserRepositoryAPI(api)

        val sample = UserDTO(
            idUser = 10,
            nombre = "Ana",
            apellido = "Lopez",
            correo = "ana@mail.com",
            phone = "111222",
            rolId = 1
        )

        coEvery { api.getUserById(10) } returns Response.success(sample)

        val result = repo.getUserById(10)

        assertTrue(result.isSuccess)
        assertEquals("Ana", result.getOrNull()!!.nombre)
        assertEquals(10, result.getOrNull()!!.idUser)
    }

    @Test
    fun emailExists_true() = runBlocking {
        val api = mockk<UserServiceAPI>()
        val repo = UserRepositoryAPI(api)

        coEvery { api.checkEmailExists("test@mail.com") } returns Response.success(true)

        val result = repo.emailExists("test@mail.com")

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun getWorkers_devuelve_lista() = runBlocking {
        val api = mockk<UserServiceAPI>()
        val repo = UserRepositoryAPI(api)

        val workers = listOf(
            UserDTO(1,"Ana","Lopez","ana@mail.com","111",3),
            UserDTO(2,"Pedro","Mendez","pedro@mail.com","222",3)
        )

        coEvery { api.getWorkers() } returns Response.success(workers)

        val result = repo.getWorkers()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()!!.size)
    }

    @Test
    fun updateUser_devuelve_exito() = runBlocking {

        val api = mockk<UserServiceAPI>()
        val repo = UserRepositoryAPI(api)

        val updateRequest = UserUpdateRequestDTO(
            nombre = "Josefa Edit",
            apellido = "Test Edit",
            phone = "555555"
        )

        val updatedUser = UserDTO(
            idUser = 99,
            nombre = "Josefa Edit",
            apellido = "Test Edit",
            correo = "nuevo@mail.com",
            phone = "555555",
            rolId = 1
        )

        coEvery { api.updateUser(99, updateRequest) } returns Response.success(updatedUser)

        val result = repo.updateUser(99, updateRequest)

        assertTrue(result.isSuccess)
        assertEquals("Josefa Edit", result.getOrNull()!!.nombre)
        assertEquals("nuevo@mail.com", result.getOrNull()!!.correo)
    }


}
