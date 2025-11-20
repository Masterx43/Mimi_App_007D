package com.example.uinavegacion.data.repository



import com.example.uinavegacion.data.remote.rol.RmRol
import com.example.uinavegacion.data.remote.rol.RolServiceAPI
import com.example.uinavegacion.data.remote.rol.dto.RolDTO
import retrofit2.HttpException
import java.io.IOException

class RolRepositoryAPI(
    private val api: RolServiceAPI = RmRol.api
) {

    suspend fun obtenerTodosRoles(): Result<List<RolDTO>> = runCatching {
        val res = api.getAllRoles()
        if (!res.isSuccessful || res.body() == null)
            throw Exception("Error al obtener roles")
        res.body()!!
    }

    suspend fun insertarRol(nombre: String): Result<RolDTO> = runCatching {
        val res = api.crearRol(RolDTO(0, nombre))
        if (!res.isSuccessful || res.body() == null)
            throw Exception("Error al crear rol")
        res.body()!!
    }

    suspend fun actualizarRol(id: Long, nombre: String): Result<RolDTO> = runCatching {
        val res = api.actualizarRol(id, RolDTO(id, nombre))
        if (!res.isSuccessful || res.body() == null)
            throw Exception("Error al actualizar rol")
        res.body()!!
    }

    suspend fun eliminarRol(id: Long): Result<Unit> = runCatching {
        val res = api.eliminarRol(id)
        if (!res.isSuccessful)
            throw Exception("Error al eliminar rol")
    }
}

