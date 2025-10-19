package com.example.uinavegacion.data.local.entities.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user : UserEntity) : Long

    @Query("select * from usuario where correo = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("select COUNT(*) from usuario")
    suspend fun count(): Int

    @Query("select * from usuario order by nombre ASC")
    suspend fun getAll() : List<UserEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: UserEntity) : Int

    @Query("select * from usuario where rolId = :idRol")
    suspend fun getUsersByRolId(idRol : Long) : List<UserEntity>
}