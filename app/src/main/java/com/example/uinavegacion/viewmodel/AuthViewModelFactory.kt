package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel                              // Tipo base ViewModel
import androidx.lifecycle.ViewModelProvider                      // Factory de ViewModels
import com.example.uinavegacion.data.local.storage.IUserPreferences
import com.example.uinavegacion.data.repository.AuthRepository
import com.example.uinavegacion.data.repository.UserRepositoryAPI

// Factory simple para crear AuthViewModel con su UserRepository.
class AuthViewModelFactory(
    private val userPrefs: IUserPreferences,
    private val authRepository: AuthRepository,
    private val repositoryTest : UserRepositoryAPI
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")                                   // Evitar warning de cast genérico
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Si solicitan AuthViewModel, lo creamos con el repo.
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userPrefs, authRepository,repositoryTest) as T
        }
        // Si piden otra clase, lanzamos error descriptivo.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}