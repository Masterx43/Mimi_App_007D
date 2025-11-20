package com.example.uinavegacion.data.remote.servicioservice

import com.example.uinavegacion.data.remote.servicioservice.dto.CrearServicioRequest
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicioServiceAPI {

    @GET("api/servicios")
    suspend fun getAllServicios(): Response<List<ServicioDTO>>

    @GET("api/servicios/{id}")
    suspend fun getServicioById(@Path("id") id: Long): Response<ServicioDTO>

    @POST("api/servicios")
    suspend fun crearServicio(@Body req: CrearServicioRequest): Response<ServicioDTO>

    @PUT("api/servicios/{id}")
    suspend fun actualizarServicio(
        @Path("id") id: Long,
        @Body req: CrearServicioRequest
    ): Response<ServicioDTO>

    @DELETE("api/servicios/{id}")
    suspend fun eliminarServicio(@Path("id") id: Long): Response<Unit>
}