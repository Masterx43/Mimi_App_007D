package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.rol.RolServiceAPI
import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RolRepositoryApiTest{
    @Test
    fun obtenerRoles_devuleve_lista()= runBlocking {
        val api = mockk<RolServiceAPI>()
        val repo = RolRepositoryAPI(api)

        val roles= listOf(
            RolDTO(1, "Cliente"),
            RolDTO(2,"Administrador"),
            RolDTO(3,"Trabajador")
        )

        coEvery { api.getAllRoles() } returns Response.success(roles)

        val result = repo.obtenerTodosRoles()

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull()!!.size)
        assertEquals("Cliente", result.getOrNull()!![0].nombre)
    }
    @Test
    fun crearRol_devuelve_exito()= runBlocking {
        val api= mockk<RolServiceAPI>()
        val repo = RolRepositoryAPI(api)

        val sample = RolDTO(
            id = 10,
            nombre = "Supervisor"
        )
        coEvery {
            api.crearRol(RolDTO(0,"Supervisor"))
        } returns Response.success(sample)

        val result = repo.insertarRol("Supervisor")

        assertTrue(result.isSuccess)
        assertEquals(10,result.getOrNull()!!.id)
        assertEquals("Supervisor", result.getOrNull()!!.nombre)
    }

    @Test
    fun actualizarRol_devuelve_exito() = runBlocking {
        val api = mockk<RolServiceAPI>()
        val repo = RolRepositoryAPI(api)

        val update = RolDTO(
            id= 5,
            nombre= "Jefe de Area"
        )

        coEvery {
            api.actualizarRol(5, RolDTO(5, "Jefe de Area"))
        } returns Response.success(update)

        val result = repo.actualizarRol(5, "Jefe de Area")

        assertTrue(result.isSuccess)
        assertEquals("Jefe de Area", result.getOrNull()!!.nombre)
        assertEquals(5, result.getOrNull()!!.id)
    }

    @Test
    fun eliminarRol_devuelve_exito() = runBlocking {

        val api = mockk<RolServiceAPI>()
        val repo = RolRepositoryAPI(api)

        coEvery { api.eliminarRol(7) } returns Response.success(Unit)

        val result = repo.eliminarRol(7)

        assertTrue(result.isSuccess)
    }

}