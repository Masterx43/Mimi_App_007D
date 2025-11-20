package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.authservice.RmAuth
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginRequestDTO
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO

class AuthRepository {

    private val api = RmAuth.api

    suspend fun login(email: String, password: String): Result<AuthLoginResponseDTO> {
        return try {
            val response = api.login(AuthLoginRequestDTO(email, password))

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error de autenticación"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}