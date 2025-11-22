package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.servicioservice.ServicioServiceAPI
import com.example.uinavegacion.data.remote.servicioservice.dto.CrearServicioRequest
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class ServicioRepositoryApiTest {

    @Test
    fun obtenerServicios_devuelve_lista() = runBlocking {
        val api = mockk<ServicioServiceAPI>()
        val repo = ServicioRepositoryAPI(api)

        val sample = listOf(
            ServicioDTO(
                idServicio = 1,
                nombre = "Corte de pelo",
                descripcion = "Corte clásico",
                precio = 15000,
                categoriaId = 1
            )
        )

        coEvery { api.getAllServicios() } returns Response.success(sample)

        val result = repo.obtenerTodosServicios()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.size)
        assertEquals("Corte de pelo", result.getOrNull()!![0].nombre)
    }

    @Test
    fun crearServicio_devuelve_exito() = runBlocking {
        val api = mockk<ServicioServiceAPI>()
        val repo = ServicioRepositoryAPI(api)

        val req = CrearServicioRequest(
            nombre = "Peinado",
            descripcion = "Peinado elegante",
            precio = 12000,
            categoriaId = 1
        )

        val sample = ServicioDTO(
            idServicio = 5,
            nombre = "Peinado",
            descripcion = "Peinado elegante",
            precio = 12000,
            categoriaId = 1
        )

        coEvery { api.crearServicio(req) } returns Response.success(sample)

        val result = repo.crearServicio(req)

        assertTrue(result.isSuccess)
        assertEquals("Peinado", result.getOrNull()!!.nombre)
        assertEquals(5, result.getOrNull()!!.idServicio)
    }

    @Test
    fun actualizarServicio_devuelve_exito() = runBlocking {
        val api = mockk<ServicioServiceAPI>()
        val repo = ServicioRepositoryAPI(api)

        val req = CrearServicioRequest(
            nombre = "Peinado premium",
            descripcion = "Peinado con productos pro",
            precio = 20000,
            categoriaId = 1
        )

        val sample = ServicioDTO(
            idServicio = 5,
            nombre = "Peinado premium",
            descripcion = "Peinado con productos pro",
            precio = 20000,
            categoriaId = 1
        )

        coEvery { api.actualizarServicio(5, req) } returns Response.success(sample)

        val result = repo.actualizarServicio(5, req)

        assertTrue(result.isSuccess)
        assertEquals("Peinado premium", result.getOrNull()!!.nombre)
        assertEquals(20000, result.getOrNull()!!.precio)
    }

    @Test
    fun eliminarServicio_devuelve_exito() = runBlocking {
        val api = mockk<ServicioServiceAPI>()
        val repo = ServicioRepositoryAPI(api)

        coEvery { api.eliminarServicio(10) } returns Response.success(Unit)

        val result = repo.eliminarServicio(10)

        assertTrue(result.isSuccess)
    }
}
