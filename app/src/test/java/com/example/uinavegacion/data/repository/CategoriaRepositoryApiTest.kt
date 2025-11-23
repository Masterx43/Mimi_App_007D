package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.categoria.CategoriaServiceAPI
import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.categoria.dto.CrearCategoriaRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class CategoriaRepositoryApiTest{

    //listar categorias
    @Test
    fun obtenerTodasCategorias() = runBlocking {

        val api = mockk<CategoriaServiceAPI>()
        val repo = CategoriaRepositoryAPI(api)

        val categorias = listOf(
            CategoriaDTO(1, "Adulto", "Servicios para adultos"),
            CategoriaDTO(2, "Niño", "Servicios para niños")
        )

        coEvery { api.getAllCategorias() } returns Response.success(categorias)

        val result = repo.obtenerTodasCategorias()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()!!.size)
        assertEquals("Adulto", result.getOrNull()!![0].nombre)
        assertEquals("Servicios para adultos", result.getOrNull()!![0].descripcion)
    }

    //crear categorias
    @Test
    fun crearCategoria_devuelve_exito() = runBlocking {

        val api = mockk<CategoriaServiceAPI>()
        val repo = CategoriaRepositoryAPI(api)

        val sample = CategoriaDTO(
            idCategoria = 10,
            nombre = "Hombre",
            descripcion = "Servicios masculinos"
        )

        coEvery { api.crearCategoria(any()) } returns Response.success(sample)

        val req = CrearCategoriaRequest(nombre = "Hombre")

        val result = repo.crearCategoria(req)

        assertTrue(result.isSuccess)
        assertEquals("Hombre", result.getOrNull()!!.nombre)
        assertEquals(10, result.getOrNull()!!.idCategoria)
    }

    //Actualizar categorias
    @Test
    fun actualizarCategoria_devuelve_exito() = runBlocking {

        val api = mockk<CategoriaServiceAPI>()
        val repo = CategoriaRepositoryAPI(api)

        val updated = CategoriaDTO(
            idCategoria = 5,
            nombre = "Adulto Mayor",
            descripcion = "Servicios para personas mayores"
        )

        coEvery { api.actualizarCategoria(5, any()) } returns Response.success(updated)

        val req = CrearCategoriaRequest(nombre = "Adulto Mayor")

        val result = repo.actualizarCategoria(5, req)

        assertTrue(result.isSuccess)
        assertEquals("Adulto Mayor", result.getOrNull()!!.nombre)
    }

    //eliminar categorias
    @Test
    fun eliminarCategoria_devuelve_exito() = runBlocking {

        val api = mockk<CategoriaServiceAPI>()
        val repo = CategoriaRepositoryAPI(api)

        coEvery { api.eliminarCategoria(3) } returns Response.success(Unit)

        val result = repo.eliminarCategoria(3)

        assertTrue(result.isSuccess)
    }


}
