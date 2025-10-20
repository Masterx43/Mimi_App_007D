package com.example.uinavegacion.data.local.entities.servicio

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity

@Entity(
    tableName = "servicios",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["idCategoria"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ServicioEntity(
    @PrimaryKey(autoGenerate = true)
    val idServicio : Long = 0L,
    val nombre : String,
    val precio : Int,
    val descripcion : String,
    val categoriaId : Long
)