package com.example.uinavegacion.data.local.entities.estado

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EstadoDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEstado(estado : EstadoEntity) : Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateEstado(estado: EstadoEntity) : Int

    @Query("select * from estados where idEstado = :estadoId LIMIT 1")
    suspend fun getEstadoById(estadoId : Long) : EstadoEntity?

    @Query("select count(*) from estados")
    suspend fun getTotalEstado() : Int

    @Query("select * from estados order by nombre ASC")
    suspend fun getAllEstados() : List<EstadoEntity>

    @Query("delete from estados where idEstado = :estadoId")
    suspend fun deleteEstado(estadoId : Long)
}