package com.example.uinavegacion.data.local.entities.reservas

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.uinavegacion.data.local.entities.estado.EstadoEntity
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity
import com.example.uinavegacion.data.local.entities.user.UserEntity
import java.util.Date

@Entity(tableName = "reservas",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["idUser"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstadoEntity::class,
            parentColumns = ["idEstado"],
            childColumns = ["estadoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ServicioEntity::class,
            parentColumns = ["idServicio"],
            childColumns = ["servicioId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ReservaEntity (
    @PrimaryKey(autoGenerate = true)
    val idReserva : Long = 0L,
    val fechaReserva : String,  //campo cambiado temporalmente para probar
    val subtotal : Int,
    val userId : Long,
    val estadoId : Long,
    val servicioId : Long
)