package com.example.uinavegacion.data.remote.reservas.dto

data class CrearReservaRequestDTO(
    val idUsuario: Long,
    val idServicio: Long,
    val idTrabajador: Long,
    val fecha: String,
    val hora: String
)

data class ReservaResponseDTO(
    val idReserva: Long,
    val idUsuario: Long,
    val idServicio: Long,
    val idTrabajador: Long,
    val fecha: String,
    val hora: String,
    val estado: String
)

data class ReservaDetalleDTO(
    val idReserva: Long,
    val fecha: String,
    val hora: String,
    val estado: String,
    val usuario: String,
    val trabajador: String,
    val servicio: String
)

