package com.example.uinavegacion.data.local.entities.estado

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estados")
data class EstadoEntity (
    @PrimaryKey(autoGenerate = true)
    val idEstado : Long = 0L,
    val nombre : String
)