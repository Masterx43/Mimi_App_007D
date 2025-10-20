package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.reservas.ReservaDao
import com.example.uinavegacion.data.local.entities.reservas.ReservaEntity

class ReservaRepository(
    private val reservaDao: ReservaDao
) {

    suspend fun crearReserva(reserva: ReservaEntity): Result<Long> = try {
        Result.success(reservaDao.insertReserva(reserva))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun obtenerTodasLasReservas(): Result<List<ReservaEntity>> = try {
        Result.success(reservaDao.getAllReservas())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun eliminarReserva(id: Long): Result<Int> = try {
        val filas = reservaDao.deleteReservaById(id)
        if (filas > 0) Result.success(filas)
        else Result.failure(IllegalStateException("No se encontró la reserva"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
