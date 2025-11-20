package com.example.uinavegacion.data.remote.categoria.dto


data class CategoriaDTO(
    val idCategoria: Long,
    val nombre: String,
    val descripcion: String
)

data class CrearCategoriaRequest(
    val nombre: String
)
