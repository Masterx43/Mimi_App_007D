package com.example.uinavegacion.data.remote.authservice.dto

data class AuthLoginResponseDTO(
    val success: Boolean,
    val message: String,
    val user: UserAuthDTO?   // puede ser null si falla el login
)

