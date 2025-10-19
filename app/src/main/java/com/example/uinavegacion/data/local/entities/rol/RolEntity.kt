package com.example.uinavegacion.data.local.entities.rol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.uinavegacion.data.local.entities.user.UserEntity

@Entity(tableName = "Roles")
data class RolEntity(
    @PrimaryKey(autoGenerate = true)
    val idRol : Long = 0L,
    val descripcion : String
)