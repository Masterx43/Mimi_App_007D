package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.entities.categoria.CategoriaDao
import com.example.uinavegacion.data.local.entities.categoria.CategoriaEntity

class CategoriaRepository(private val dao: CategoriaDao) {

    suspend fun insertarCategoria(categoria: CategoriaEntity): Result<Long> = runCatching {
        dao.insert(categoria)
    }

    suspend fun obtenerCategoriaPorId(id: Long): Result<CategoriaEntity?> = runCatching {
        dao.getCategoriaById(id)
    }

    suspend fun obtenerTodasCategorias(): Result<List<CategoriaEntity>> = runCatching {
        dao.getAllCategorias()
    }

    suspend fun actualizarCategoria(categoria: CategoriaEntity): Result<Int> = runCatching {
        dao.updateCategoria(categoria)
    }

    suspend fun eliminarCategoria(id: Long): Result<Int> = runCatching {
        dao.deleteCategoriaById(id)
    }

    suspend fun contarCategorias(): Result<Int> = runCatching {
        dao.getTotalCategoria()
    }
}
