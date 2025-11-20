package com.example.uinavegacion.data.remote.categoria

import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.categoria.dto.CrearCategoriaRequest
import retrofit2.Response
import retrofit2.http.*

interface CategoriaServiceAPI {

    @GET("api/categorias")
    suspend fun getAllCategorias(): Response<List<CategoriaDTO>>

    @GET("api/categorias/{id}")
    suspend fun getCategoriaById(@Path("id") id: Long): Response<CategoriaDTO>

    @POST("api/categorias")
    suspend fun crearCategoria(@Body req: CrearCategoriaRequest): Response<CategoriaDTO>

    @PUT("api/categorias/{id}")
    suspend fun actualizarCategoria(
        @Path("id") id: Long,
        @Body req: CrearCategoriaRequest
    ): Response<CategoriaDTO>

    @DELETE("api/categorias/{id}")
    suspend fun eliminarCategoria(@Path("id") id: Long): Response<Unit>
}
