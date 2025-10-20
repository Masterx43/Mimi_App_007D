package com.example.uinavegacion.data.local.entities.reservas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReservaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertReserva(reserva : ReservaEntity) : Long

    @Query("select * from reservas where idReserva = :reservaId limit 1")
    suspend fun getReservaById(reservaId : Long) : ReservaEntity?

    @Query("select count(*) from reservas")
    suspend fun getTotalReservas() : Int

    @Query("select * from reservas order by fechaReserva asc")
    suspend fun getAllReservas() : List<ReservaEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateReserva(reserva: ReservaEntity): Int

    @Query("delete from reservas where idReserva = :reservaId")
    suspend fun deleteReservaById(reservaId: Long) : Int
}