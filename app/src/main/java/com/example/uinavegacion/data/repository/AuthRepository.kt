package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.authservice.RmAuth
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginRequestDTO
import com.example.uinavegacion.data.remote.authservice.dto.AuthLoginResponseDTO

class AuthRepository {

    private val api = RmAuth.api

    suspend fun login(email: String, password: String): Result<AuthLoginResponseDTO> {
        return try {
            val response = api.login(AuthLoginRequestDTO(email, password))

            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Respuesta vacía"))
                    }
                }

                response.code() == 401 -> {
                    Result.failure(Exception("Credenciales incorrectas"))
                }

                response.code() == 400 -> {
                    Result.failure(Exception("Hubo un problema con la conexión"))
                }

                else -> {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    Result.failure(Exception("Error ${response.code()}: $errorMsg"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}