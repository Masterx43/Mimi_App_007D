package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.servicio.ServicioDao
import com.example.uinavegacion.data.local.entities.servicio.ServicioEntity

class ServicioRepository(private val dao: ServicioDao) {

    suspend fun insertarServicio(servicio: ServicioEntity): Result<Long> = runCatching {
        dao.insertServicio(servicio)
    }

    suspend fun obtenerServicioPorId(id: Long): Result<ServicioEntity?> = runCatching {
        dao.getServicioById(id)
    }

    suspend fun obtenerTodosServicios(): Result<List<ServicioEntity>> = runCatching {
        dao.getAllServicios()
    }

    suspend fun actualizarServicio(servicio: ServicioEntity): Result<Int> = runCatching {
        dao.updateServicio(servicio)
    }

    suspend fun eliminarServicio(id: Long): Result<Int> = runCatching {
        dao.deleteServicioPorId(id)
    }

    suspend fun contarServicios(): Result<Int> = runCatching {
        dao.getTotalServicios()
    }
}
