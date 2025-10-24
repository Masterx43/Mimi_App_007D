package com.example.uinavegacion.data.local.entities.reservas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReservaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertReserva(reserva: ReservaEntity): Long

    @Query("SELECT * FROM reservas WHERE idReserva = :reservaId LIMIT 1")
    suspend fun getReservaById(reservaId: Long): ReservaEntity?

    @Query("SELECT COUNT(*) FROM reservas")
    suspend fun getTotalReservas(): Int

    @Query("SELECT * FROM reservas ORDER BY fechaReserva ASC")
    suspend fun getAllReservas(): List<ReservaEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateReserva(reserva: ReservaEntity): Int

    @Query("DELETE FROM reservas WHERE idReserva = :reservaId")
    suspend fun deleteReservaById(reservaId: Long): Int

    @Query("""
    SELECT 
        r.idReserva, 
        r.fechaReserva, 
        u.nombre AS nombreCliente, 
        s.nombre AS nombreServicio, 
        r.estadoId
    FROM reservas AS r
    INNER JOIN usuario AS u ON u.idUser = r.userId
    INNER JOIN servicios AS s ON s.idServicio = r.servicioId
    ORDER BY r.fechaReserva ASC
""")
    suspend fun getTodasLasReservasConDetalles(): List<ReservaDetalle>

    @Query("UPDATE reservas SET estadoId = :nuevoEstado WHERE idReserva = :reservaId")
    suspend fun updateEstadoReservaPorId(reservaId: Long, nuevoEstado: Long): Int
}