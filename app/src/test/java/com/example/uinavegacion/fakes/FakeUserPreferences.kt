package com.example.uinavegacion.fakes


import com.example.uinavegacion.data.local.storage.IUserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Fake de UserPreferences para pruebas unitarias.
 * Evita usar DataStore real y permite controlar estados desde los tests.
 */

class FakeUserPreferences : IUserPreferences {

    private val _isLogged = MutableStateFlow(false)
    private val _userId = MutableStateFlow<Long?>(null)
    private val _roleId = MutableStateFlow<Long?>(null)

    override val isLoggedIn: StateFlow<Boolean> = _isLogged
    override val userId: StateFlow<Long?> = _userId
    override val userRoleId: StateFlow<Long?> = _roleId

    override suspend fun saveLoginState(isLoggedIn: Boolean, roleId: Long?, userId: Long?) {
        _isLogged.value = isLoggedIn
        _roleId.value = roleId
        _userId.value = userId
    }

    override suspend fun clear() {
        _isLogged.value = false
        _roleId.value = null
        _userId.value = null
    }

    // --- Helpers para tests ---
    suspend fun emitLoggedIn(value: Boolean) {
        _isLogged.emit(value)
    }

    suspend fun emitUserId(id: Long?) {
        _userId.emit(id)
    }

    suspend fun emitRoleId(id: Long?) {
        _roleId.emit(id)
    }
}


