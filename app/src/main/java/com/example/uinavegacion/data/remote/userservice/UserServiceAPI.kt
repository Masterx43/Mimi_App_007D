package com.example.uinavegacion.data.remote.userservice

import com.example.uinavegacion.data.remote.userservice.dto.LoginRequestDTO
import com.example.uinavegacion.data.remote.userservice.dto.RegisterRequestDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserAuthDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserServiceAPI {

    // REGISTRO
    @POST("api/users/register")
    suspend fun register(
        @Body request: RegisterRequestDTO
    ): Response<UserDTO>

    // OBTENER USUARIO POR ID
    @GET("api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<UserDTO>

    // LISTAR TRABAJADORES
    @GET("api/users/workers")
    suspend fun getWorkers(): Response<List<UserDTO>>

    // LISTAR TODOS LOS USUARIOS
    @GET("api/users/all")
    suspend fun getAllUsers(): Response<List<UserDTO>>

    // VALIDAR SI CORREO EXISTE
    @GET("api/users/exists/email/{correo}")
    suspend fun checkEmailExists(
        @Path("correo") correo: String
    ): Response<Boolean>

}