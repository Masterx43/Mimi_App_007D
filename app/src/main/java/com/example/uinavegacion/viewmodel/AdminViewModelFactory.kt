package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.data.repository.CategoriaRepository
import com.example.uinavegacion.data.repository.RolRepository
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.UserRepository

class AdminViewModelFactory(
    private val servicioRepository: ServicioRepository,
    private val categoriaRepository: CategoriaRepository,
    private val rolRepository: RolRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(servicioRepository, categoriaRepository, rolRepository,userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
