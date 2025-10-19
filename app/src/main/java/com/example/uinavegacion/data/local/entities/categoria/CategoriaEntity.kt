package com.example.uinavegacion.data.local.entities.categoria

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class CategoriaEntity (
    @PrimaryKey(autoGenerate = true)
    val idCategoria : Long = 0L,
    val nombreCategoria : String
)