package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel                              // Tipo base ViewModel
import androidx.lifecycle.ViewModelProvider                      // Factory de ViewModels
import com.example.uinavegacion.data.repository.ReservaRepository // Repositorio a inyectar

// Factory simple para crear AuthViewModel con su UserRepository.
class BookingViewModelFactory(
    private val repository: ReservaRepository                      // Dependencia que inyectaremos
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")                                   // Evitar warning de cast genérico
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Si solicitan AuthViewModel, lo creamos con el repo.
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(repository) as T
        }
        // Si piden otra clase, lanzamos error descriptivo.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}