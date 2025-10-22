package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.rol.RolDao
import com.example.uinavegacion.data.local.entities.rol.RolEntity

class RolRepository(private val dao: RolDao) {

    suspend fun insertarRol(rol: RolEntity): Result<Long> = runCatching {
        dao.insertRol(rol)
    }

    suspend fun obtenerRolPorId(id: Long): Result<RolEntity?> = runCatching {
        dao.getRolById(id)
    }

    suspend fun obtenerTodosRoles(): Result<List<RolEntity>> = runCatching {
        dao.getAllRols()
    }

    suspend fun actualizarRol(rol: RolEntity): Result<Int> = runCatching {
        dao.updateRol(rol)
    }

    suspend fun eliminarRol(id: Long): Result<Int> = runCatching {
        dao.deleteRol(id)
    }

    suspend fun contarRoles(): Result<Int> = runCatching {
        dao.getTotalRols()
    }
}