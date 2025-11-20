package com.example.uinavegacion.data.remote.servicioservice.dto

data class ServicioDTO(
    val idServicio: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoriaId: Long
)
