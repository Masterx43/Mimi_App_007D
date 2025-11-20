package com.example.uinavegacion.data.repository


import com.example.uinavegacion.data.remote.servicioservice.*
import com.example.uinavegacion.data.remote.servicioservice.dto.CrearServicioRequest
import com.example.uinavegacion.data.remote.servicioservice.dto.ServicioDTO
import retrofit2.HttpException
import java.io.IOException

class ServicioRepositoryAPI(
    private val api: ServicioServiceAPI = RmServicio.api
) {

    suspend fun obtenerTodosServicios(): Result<List<ServicioDTO>> {
        return try {
            val res = api.getAllServicios()
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al cargar servicios"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión al servidor"))
        } catch (e: HttpException) {
            Result.failure(Exception("Error HTTP: ${e.code()}"))
        }
    }

    suspend fun crearServicio(req: CrearServicioRequest): Result<ServicioDTO> {
        return try {
            val res = api.crearServicio(req)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al crear servicio"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarServicio(id: Long, req: CrearServicioRequest): Result<ServicioDTO> {
        return try {
            val res = api.actualizarServicio(id, req)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("No se pudo actualizar servicio"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarServicio(id: Long): Result<Unit> {
        return try {
            val res = api.eliminarServicio(id)
            if (res.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("No se pudo eliminar servicio"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
