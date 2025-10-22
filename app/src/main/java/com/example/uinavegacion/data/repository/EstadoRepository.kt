package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.estado.EstadoDao
import com.example.uinavegacion.data.local.entities.estado.EstadoEntity

class EstadoRepository(private val dao: EstadoDao) {

    suspend fun insertarEstado(estado: EstadoEntity): Result<Long> = runCatching {
        dao.insertEstado(estado)
    }

    suspend fun obtenerEstadoPorId(id: Long): Result<EstadoEntity?> = runCatching {
        dao.getEstadoById(id)
    }

    suspend fun obtenerTodosEstados(): Result<List<EstadoEntity>> = runCatching {
        dao.getAllEstados()
    }

    suspend fun actualizarEstado(estado: EstadoEntity): Result<Int> = runCatching {
        dao.updateEstado(estado)
    }

    suspend fun eliminarEstado(id: Long): Result<Unit> = runCatching {
        dao.deleteEstado(id)
    }

    suspend fun contarEstados(): Result<Int> = runCatching {
        dao.getTotalEstado()
    }
}
