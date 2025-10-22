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
}