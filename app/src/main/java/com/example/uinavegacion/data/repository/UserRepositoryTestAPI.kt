package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.userservice.RmUsers
import com.example.uinavegacion.data.remote.userservice.UserServiceAPI
import com.example.uinavegacion.data.remote.userservice.dto.RegisterRequestDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserDTO
import com.example.uinavegacion.data.remote.userservice.dto.UserUpdateRequestDTO
import retrofit2.HttpException
import java.io.IOException

class UserRepositoryTestAPI(
    private val api: UserServiceAPI = RmUsers.create(UserServiceAPI::class.java)
) {

    suspend fun register(
        nombre: String,
        apellido: String,
        correo: String,
        phone: String,
        password: String,
        rolId: Long
    ): Result<UserDTO> {
        return try {

            val req = RegisterRequestDTO(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                phone = phone,
                password = password,
                rolId = rolId
            )

            val response = api.register(req)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error en el registro"))
            }

        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión al servidor ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Error HTTP: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ----------------------------------------------------------
    // OBTENER USUARIO POR ID
    // ----------------------------------------------------------
    suspend fun getUserById(id: Long): Result<UserDTO> {
        return try {
            val response = api.getUserById(id)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }

        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ----------------------------------------------------------
    // LISTAR TODOS LOS USUARIOS
    // ----------------------------------------------------------
    suspend fun getAllUsers(): Result<List<UserDTO>> {
        return try {
            val response = api.getAllUsers()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudieron obtener usuarios"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ----------------------------------------------------------
    // LISTAR TRABAJADORES (rolId = 3)
    // ----------------------------------------------------------
    suspend fun getWorkers(): Result<List<UserDTO>> {
        return try {

            val response = api.getWorkers()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudieron cargar trabajadores"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ----------------------------------------------------------
    // VALIDAR SI CORREO EXISTE
    // ----------------------------------------------------------
    suspend fun emailExists(correo: String): Result<Boolean> {
        return try {

            val response = api.checkEmailExists(correo)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo verificar el correo"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(id: Long, req: UserUpdateRequestDTO): Result<UserDTO> {
        return try {
            val response = api.updateUser(id, req)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo actualizar el usuario"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
