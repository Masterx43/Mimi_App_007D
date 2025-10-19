package com.example.uinavegacion.data.local.entities.servicio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServicioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertServicio(servicio : ServicioEntity) : Int

    @Query("select * from servicios where idServicio = :servicioId LIMIT 1")
    suspend fun getServicioById(servicioId : Long) : ServicioEntity?

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateServicio(servicio : ServicioEntity) : Int

    @Query("select count(*) from servicios")
    suspend fun getTotalServicios() : Int

    @Query("select * from servicios order by nombre ASC")
    suspend fun getAllServicios() : List<ServicioEntity>

    @Query("DELETE FROM servicios WHERE idServicio = :servicioId")
    suspend fun deleteServicioPorId(servicioId: Long): Int
}