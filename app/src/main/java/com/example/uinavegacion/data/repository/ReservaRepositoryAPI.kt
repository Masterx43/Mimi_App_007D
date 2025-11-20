package com.example.uinavegacion.data.repository


import com.example.uinavegacion.data.remote.reservas.*
import com.example.uinavegacion.data.remote.reservas.dto.CrearReservaRequestDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaResponseDTO
import retrofit2.HttpException
import java.io.IOException

class ReservaRepositoryAPI(
    private val api: ReservaServiceAPI = RmReservas.api
) {

    suspend fun crearReserva(req: CrearReservaRequestDTO): Result<ReservaResponseDTO> {
        return try {
            val res = api.crearReserva(req)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("No se pudo crear la reserva"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerReservasUsuario(id: Long): Result<List<ReservaResponseDTO>> {
        return try {
            val res = api.getReservasUsuario(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al obtener reservas del usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerReservasTrabajador(id: Long): Result<List<ReservaResponseDTO>> {
        return try {
            val res = api.getReservasTrabajador(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al obtener reservas del trabajador"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPorId(id: Long): Result<ReservaResponseDTO> {
        return try {
            val res = api.getReservaPorId(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Reserva no encontrada"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerTodas(): Result<List<ReservaResponseDTO>> {
        return try {
            val res = api.getAllReservas()
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("No se pudieron obtener las reservas"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerTodasConDetalles(): Result<List<ReservaDetalleDTO>> = runCatching {
        val response = api.getAllDetalle()
        if (response.isSuccessful) response.body() ?: emptyList()
        else throw Exception("Error: ${response.code()}")
    }


    suspend fun actualizarEstado(id: Long, nuevo: String): Result<ReservaResponseDTO> {
        return try {
            val res = api.actualizarEstado(id, nuevo)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("No se pudo actualizar estado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerReservasDetalleTrabajador(id: Long): Result<List<ReservaDetalleDTO>> {
        return try {
            val res = api.getReservasWorker(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al obtener reservas del trabajador"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerReservasDetalleUsuario(id: Long): Result<List<ReservaDetalleDTO>> {
        return try {
            val res = api.getReservasDetalleUsuario(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Error al obtener reservas del usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
