package com.example.uinavegacion.data.remote.reservas


import com.example.uinavegacion.data.remote.reservas.dto.CrearReservaRequestDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface ReservaServiceAPI {

    @POST("api/reservas/crear")
    suspend fun crearReserva(
        @Body req: CrearReservaRequestDTO
    ): Response<ReservaResponseDTO>

    @GET("api/reservas/usuario/{id}")
    suspend fun getReservasUsuario(
        @Path("id") idUsuario: Long
    ): Response<List<ReservaResponseDTO>>

    @GET("api/reservas/trabajador/{id}")
    suspend fun getReservasTrabajador(
        @Path("id") idTrabajador: Long
    ): Response<List<ReservaResponseDTO>>

    @GET("api/reservas/{id}")
    suspend fun getReservaPorId(
        @Path("id") idReserva: Long
    ): Response<ReservaResponseDTO>

    @GET("api/reservas/all")
    suspend fun getAllReservas(): Response<List<ReservaResponseDTO>>

    @PATCH("api/reservas/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") idReserva: Long,
        @Body estadoNuevo: String
    ): Response<ReservaResponseDTO>
}
