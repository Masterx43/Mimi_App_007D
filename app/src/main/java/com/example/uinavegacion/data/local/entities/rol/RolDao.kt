package com.example.uinavegacion.data.local.entities.rol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RolDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRol(rol : RolEntity) : Long

    @Query("SELECT * FROM ROLES WHERE idRol = :rolId LIMIT 1")
    suspend fun getRolById(rolId : Long) : RolEntity?

    @Query("SELECT COUNT(idRol) from ROLES")
    suspend fun getTotalRols() : Int

    @Query("SELECT * FROM ROLES ORDER BY descripcion ASC")
    suspend fun getAllRols() : List<RolEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateRol(rol: RolEntity) : Int

    @Query("delete from roles where idRol = :rolId")
    suspend fun deleteRol(rolId : Long) : Int
}