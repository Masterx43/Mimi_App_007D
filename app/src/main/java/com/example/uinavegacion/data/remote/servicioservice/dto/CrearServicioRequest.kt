package com.example.uinavegacion.data.remote.servicioservice.dto

data class CrearServicioRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoriaId: Long
)