package com.example.uinavegacion.data.remote.rol


import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RolServiceAPI {

    @GET("api/roles")
    suspend fun getAllRoles(): Response<List<RolDTO>>

    @GET("api/roles/{id}")
    suspend fun getRoleById(@Path("id") id: Long): Response<RolDTO>

    @POST("api/roles")
    suspend fun crearRol(@Body rol: RolDTO): Response<RolDTO>

    @PUT("api/roles/{id}")
    suspend fun actualizarRol(
        @Path("id") id: Long,
        @Body rol: RolDTO
    ): Response<RolDTO>

    @DELETE("api/roles/{id}")
    suspend fun eliminarRol(@Path("id") id: Long): Response<Unit>
}


