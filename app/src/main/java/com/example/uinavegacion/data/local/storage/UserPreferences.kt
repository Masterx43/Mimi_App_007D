package com.example.uinavegacion.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name= "user_prefs")

class UserPreferences(private val context: Context){
    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")
    private val roleIdKey = longPreferencesKey("role_id")

    suspend fun saveLoginState(isLoggedIn: Boolean, roleId: Long?){
        context.dataStore.edit { prefs->
            prefs[isLoggedInKey]= isLoggedIn
            if (roleId !=null) prefs[roleIdKey]=roleId
            else prefs.remove(roleIdKey)
        }
    }

    suspend fun setLoggedIn(value: Boolean){
        context.dataStore.edit{ prefs->
            prefs[isLoggedInKey]= value

        }
    }

    val isLoogedIn: Flow<Boolean> = context.dataStore.data
        .map{prefs -> prefs[isLoggedInKey] ?: false}

    val userRoleId: Flow<Long?> = context.dataStore.data
        .map { prefs -> prefs[roleIdKey] }

    suspend fun clear(){
        context.dataStore.edit { prefs->
            prefs.clear()
        }
    }
}