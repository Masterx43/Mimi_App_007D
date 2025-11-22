package com.example.uinavegacion.data.local.storage

import kotlinx.coroutines.flow.Flow

interface IUserPreferences {
    val isLoggedIn: Flow<Boolean>
    val userId: Flow<Long?>
    val userRoleId: Flow<Long?>

    suspend fun saveLoginState(isLoggedIn: Boolean, roleId: Long?, userId: Long?)
    suspend fun clear()
}
