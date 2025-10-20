package com.example.uinavegacion.data.local.entities.categoria

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(categoria : CategoriaEntity) : Long

    @Query("Select * from CATEGORIA where idCategoria = :idCategorias LIMIT 1")
    suspend fun getCategoriaById(idCategorias : Long) : CategoriaEntity?

    @Query("Select count(*) from categoria")
    suspend fun getTotalCategoria() : Int

    @Query("Select * from categoria order by nombreCategoria ASC")
    suspend fun getAllCategorias() : List<CategoriaEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateCategoria(categoria : CategoriaEntity) : Int

    @Query("delete from categoria where idCategoria = :categoriaId")
    suspend fun deleteCategoriaById(categoriaId : Long) : Int
}