package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.data.repository.CategoriaRepository
import com.example.uinavegacion.data.repository.CategoriaRepositoryAPI
import com.example.uinavegacion.data.repository.RolRepository
import com.example.uinavegacion.data.repository.RolRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI

class AdminViewModelFactory(
    private val servicioRepository: ServicioRepositoryAPI,
    private val categoriaRepository: CategoriaRepositoryAPI,
    private val rolRepository: RolRepositoryAPI,
    private val userRepository: UserRepositoryTestAPI
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(servicioRepository, categoriaRepository, rolRepository,userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
