package com.example.uinavegacion.data.local.entities.user

import androidx.room.*
import com.example.uinavegacion.data.local.entities.estado.EstadoEntity
import com.example.uinavegacion.data.local.entities.rol.RolEntity

@Entity(
    tableName = "usuario",
    foreignKeys = [
        ForeignKey(
            entity = RolEntity::class,
            parentColumns = ["idRol"],
            childColumns = ["rolId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstadoEntity::class,
            parentColumns = ["idEstado"],
            childColumns = ["estadoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["rolId"])]
)
data class UserEntity (
    @PrimaryKey(true)
    val idUser : Long = 0L,
    val nombre : String,
    val apellido : String,
    val correo : String,
    val pass : String,
    val direccion : String,
    val phone : String,
    val rolId : Long,
    val categoriaId : Long,
    val estadoId : Long
)