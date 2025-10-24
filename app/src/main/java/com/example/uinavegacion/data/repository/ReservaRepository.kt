package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.reservas.ReservaDao
import com.example.uinavegacion.data.local.entities.reservas.ReservaDetalle
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

    suspend fun obtenerTodasLasReservasConDetalles(): Result<List<ReservaDetalle>> = try {
        val reservas = reservaDao.getTodasLasReservasConDetalles()
        if (reservas.isEmpty()) {
            Result.failure(IllegalStateException("No hay reservas registradas"))
        } else {
            Result.success(reservas)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun actualizarReserva(reserva: ReservaEntity): Result<Int> = try {
        val filasAfectadas = reservaDao.updateReserva(reserva)
        if (filasAfectadas > 0) {
            Result.success(filasAfectadas)
        } else {
            Result.failure(IllegalStateException("No se pudo actualizar la reserva (ID no encontrado)"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun actualizarEstadoReservaPorId(
        reservaId: Long,
        nuevoEstado: Long
    ): Result<Int> = try {
        val filas = reservaDao.updateEstadoReservaPorId(reservaId, nuevoEstado)
        if (filas > 0) Result.success(filas)
        else Result.failure(IllegalStateException("No se pudo actualizar (ID $reservaId no existe)"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
