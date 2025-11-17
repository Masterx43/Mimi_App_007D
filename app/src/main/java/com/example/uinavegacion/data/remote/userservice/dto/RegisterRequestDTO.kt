package com.example.uinavegacion.data.remote.userservice.dto

data class RegisterRequestDTO(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val phone: String,
    val password: String,
    val rolId: Long
)
