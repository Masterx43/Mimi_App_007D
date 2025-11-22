package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel                              // Tipo base ViewModel
import androidx.lifecycle.ViewModelProvider                      // Factory de ViewModels
import com.example.uinavegacion.data.repository.ReservaRepository // Repositorio a inyectar
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI
import com.example.uinavegacion.data.repository.ServicioRepository
import com.example.uinavegacion.data.repository.ServicioRepositoryAPI
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.data.repository.UserRepositoryTestAPI

// Factory simple para crear AuthViewModel con su UserRepository.
class BookingViewModelFactory(
    private val reservaRepository2: ReservaRepositoryAPI,
    private val servicioRepository2: ServicioRepositoryAPI,
    private val userRepository2: UserRepositoryTestAPI
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")                                   // Evitar warning de cast genérico
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Si solicitan AuthViewModel, lo creamos con el repo.
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(
                reservaRepository2,
                servicioRepository2,
                userRepository2
            ) as T
        }
        // Si piden otra clase, lanzamos error descriptivo.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}