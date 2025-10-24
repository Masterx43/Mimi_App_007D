package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.user.UserDao
import com.example.uinavegacion.data.local.entities.user.UserEntity

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = userDao.getByEmail(email)                         // Busca usuario
        return if (user != null && user.pass == password) {      // Verifica pass
            Result.success(user)                                     // Éxito
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas")) // Error
        }
    }

    suspend fun register(
        name: String,
        apellido : String,
        email: String,
        phone: String,
        password: String
    ): Result<Long> {
        val exists = userDao.getByEmail(email) != null               // ¿Correo ya usado?
        if (exists) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        val id = userDao.insert(                                     // Inserta nuevo
            UserEntity(
                nombre = name,
                apellido = apellido,
                correo = email,
                phone = phone,                                       // Teléfono incluido
                pass = password,
                rolId = 1L,
                categoriaId = 1L,
                estadoId = 1L
            )
        )
        return Result.success(id)                                    // Devuelve ID generado
    }
    suspend fun agregarTrabajador(user : UserEntity) : Result<Long>{
        val exists = userDao.getByEmail(user.correo) != null
        if (exists) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        val id = userDao.insert(user)
        return Result.success(id)
    }

    suspend fun getUserById(userId: Long): Result<UserEntity> = runCatching {
        val user = userDao.getUserById(userId)
            ?: throw IllegalArgumentException("Usuario con ID $userId no encontrado")
        user
    }
    suspend fun getAllWorkers(
        rolId: Long
    ): Result<List<UserEntity>>{
        val trabajadores= userDao.getUsersByRolId(rolId)
        if(trabajadores.isNullOrEmpty()){
            return Result.failure(IllegalStateException("No hay trabajadores"))
        }
        return Result.success(trabajadores)
    }

    suspend fun updateUser(user: UserEntity): Result<Int> = runCatching {
        val rows = userDao.updateUser(user)
        if (rows == 0) throw IllegalStateException("No se encontró el usuario para actualizar")
        rows
    }
}