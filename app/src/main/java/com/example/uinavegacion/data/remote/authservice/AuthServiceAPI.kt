package com.example.uinavegacion.data.remote.authservice

import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginRequestDTO
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServiceAPI {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: AuthLoginRequestDTO
    ): Response<AuthLoginResponseDTO>
}