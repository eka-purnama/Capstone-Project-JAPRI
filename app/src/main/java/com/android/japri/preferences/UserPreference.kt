package com.android.japri.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserSessionData) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[USERNAME] = user.username
            preferences[TOKEN] = user.token
            preferences[ROLE] = user.role
        }
    }

    fun getSession(): Flow<UserSessionData> {
        return dataStore.data.map { preferences ->
            UserSessionData(
                preferences[ID_KEY] ?: "",
                preferences[USERNAME] ?: "",
                preferences[TOKEN] ?: "",
                preferences[ROLE] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val USERNAME = stringPreferencesKey("username")
        private val TOKEN = stringPreferencesKey("token")
        private val ROLE = stringPreferencesKey("role")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}