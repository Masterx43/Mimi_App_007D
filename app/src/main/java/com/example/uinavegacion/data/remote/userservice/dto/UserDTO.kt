package com.example.uinavegacion.data.remote.userservice.dto

data class UserDTO(
    val idUser: Long,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val phone: String,
    val rolId: Long
)
