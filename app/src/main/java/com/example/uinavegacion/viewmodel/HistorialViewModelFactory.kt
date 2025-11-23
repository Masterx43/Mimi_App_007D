package com.example.uinavegacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.data.repository.ReservaRepositoryAPI

class HistorialViewModelFactory(
    private val reservaRepository: ReservaRepositoryAPI
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            return HistorialViewModel(reservaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}