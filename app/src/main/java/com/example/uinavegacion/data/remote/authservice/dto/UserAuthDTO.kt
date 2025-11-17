package com.example.uinavegacion.data.remote.authservice.dto

data class UserAuthDTO(
    val idUser: Long,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val rolId: Long
)
