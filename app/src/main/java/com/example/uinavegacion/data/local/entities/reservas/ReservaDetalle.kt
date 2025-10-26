package com.example.uinavegacion.data.local.entities.reservas

data class ReservaDetalle(
    val idReserva: Long,
    val fechaReserva: String,
    val horaReserva: String?,
    val nombreCliente: String,
    val nombreServicio: String,
    val estadoId: Long
)

