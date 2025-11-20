package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.categoria.*
import com.example.uinavegacion.data.remote.categoria.dto.CategoriaDTO
import com.example.uinavegacion.data.remote.categoria.dto.CrearCategoriaRequest
import retrofit2.HttpException
import java.io.IOException

class CategoriaRepositoryAPI(
    private val api: CategoriaServiceAPI = RmCategoria.api
) {

    suspend fun obtenerTodasCategorias(): Result<List<CategoriaDTO>> {
        return try {
            val res = api.getAllCategorias()
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else Result.failure(Exception("No se pudieron cargar las categorías"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión"))
        } catch (e: HttpException) {
            Result.failure(Exception("Error HTTP: ${e.code()}"))
        }
    }

    suspend fun crearCategoria(req: CrearCategoriaRequest): Result<CategoriaDTO> {
        return try {
            val res = api.crearCategoria(req)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else Result.failure(Exception("No se pudo crear la categoría"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarCategoria(id: Long, req: CrearCategoriaRequest): Result<CategoriaDTO> {
        return try {
            val res = api.actualizarCategoria(id, req)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else Result.failure(Exception("No se pudo actualizar la categoría"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarCategoria(id: Long): Result<Unit> {
        return try {
            val res = api.eliminarCategoria(id)
            if (res.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("No se pudo eliminar la categoría"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
